package es.kiwi.wemedia.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import es.kiwi.common.constants.WemediaConstants;
import es.kiwi.common.exception.CustomException;
import es.kiwi.model.common.dtos.PageResponseResult;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.AppHttpCodeEnum;
import es.kiwi.model.wemedia.dtos.WmNewsDto;
import es.kiwi.model.wemedia.dtos.WmNewsPageReqDto;
import es.kiwi.model.wemedia.mapstruct.mappers.WmNewsMapper;
import es.kiwi.model.wemedia.pojos.WmMaterial;
import es.kiwi.model.wemedia.pojos.WmNews;
import es.kiwi.utils.thread.WmThreadLocalUtils;
import es.kiwi.wemedia.repository.WmMaterialRepository;
import es.kiwi.wemedia.repository.WmNewsRepository;
import es.kiwi.wemedia.service.WmNewsAutoScanService;
import es.kiwi.wemedia.service.WmNewsMaterialService;
import es.kiwi.wemedia.service.WmNewsService;
import es.kiwi.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@EnableAsync //开启异步调用
public class WmNewsServiceImpl implements WmNewsService {

    @Autowired
    private WmNewsRepository wmNewsRepository;
    @Autowired
    private WmNewsMaterialService wmNewsMaterialService;
    @Autowired
    private WmMaterialRepository wmMaterialRepository;
    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;
    @Autowired
    private WmNewsTaskService wmNewsTaskService;
    @Override
    public ResponseResult findList(WmNewsPageReqDto dto) {
        // 1. 检查参数
        // 分页检查
        dto.checkParam();

        // 2. 分页条件查询
        //发布时间倒序查询
        Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), sort);

        Specification<WmNews> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //频道精确查询
            if (dto.getChannelId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("channelId"), dto.getChannelId()));
            }
            //状态精确查询
            if (dto.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), dto.getStatus()));
            }
            //时间范围查询
            if (dto.getBeginPubDate() != null && dto.getEndPubDate() != null) {
                predicates.add(criteriaBuilder.between(root.get("publishTime"), dto.getBeginPubDate(), dto.getEndPubDate()));
            }
            //关键字模糊查询
            if (StringUtils.isNotBlank(dto.getKeyword())) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + dto.getKeyword() + "%"));
            }
            //查询当前登录用户的文章
            predicates.add(criteriaBuilder.equal(root.get("userId"), WmThreadLocalUtils.getUser().getId()));

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        Page<WmNews> page = wmNewsRepository.findAll(spec, pageable);

        // 3. 结果返回
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotalElements());
        responseResult.setData(page.getContent());
        return responseResult;
    }

    @Override
    public ResponseResult submitNews(WmNewsDto dto) {
        //0.条件判断
        if (dto == null || dto.getContent() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //1.保存或修改文章
        //属性拷贝
        WmNews wmNews = WmNewsMapper.INSTANCE.dtoToPojo(dto);
        saveOrUpdateWmNews(wmNews);

        //2.判断是否为草稿  如果为草稿结束当前方法
        if (dto.getStatus().equals(WmNews.Status.NORMAL.getCode())) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }

        //3.不是草稿，保存文章内容图片与素材的关系
        // 获取到文章中的图片信息
        List<String> materials = extractUrlInfo(dto.getContent());
        saveRelativeInfoForContent(materials, wmNews.getId());

        //4.不是草稿，保存文章封面图片与素材的关系，如果当前布局是自动，需要匹配封面图片
        saveRelativeInfoForCover(dto, wmNews, materials);

        // 审核文章
//        wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        wmNewsTaskService.addNewsToTask(wmNews.getId(), wmNews.getPublishTime());


        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 第一个功能：如果当前封面类型为自动，则设置封面类型的数据
     * 匹配规则：
     * 1，如果内容图片大于等于1，小于3  单图  type 1
     * 2，如果内容图片大于等于3  多图  type 3
     * 3，如果内容没有图片，无图  type 0
     *
     * 第二个功能：保存封面图片与素材的关系
     * @param dto
     * @param wmNews
     * @param materials
     */
    private void saveRelativeInfoForCover(WmNewsDto dto, WmNews wmNews, List<String> materials) {
        List<String> images = dto.getImages();
        //如果当前封面类型为自动，则设置封面类型的数据
        if (dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)) {
            //多图
            if (materials.size() >= 3) {
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                images = materials.stream().limit(3).collect(Collectors.toList());
            } else if (materials.size() >= 1) { //单图
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                images = materials.stream().limit(1).collect(Collectors.toList());
            } else {
                //无图
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            }
            //修改文章
            if (!CollectionUtils.isEmpty(images)) {
                wmNews.setImages(StringUtils.join(images, ","));
            }
            wmNewsRepository.save(wmNews);

            // 保存封面图片与素材的关系
            if (!CollectionUtils.isEmpty(images)) {
                saveRelativeInfo(images, wmNews.getId(), WemediaConstants.WM_COVER_REFERENCE);
            }
        }




    }

    /**
     * 处理文章内容图片与素材的关系
     * @param materials
     * @param id
     */
    private void saveRelativeInfoForContent(List<String> materials, Integer id) {
        saveRelativeInfo(materials, id, WemediaConstants.WM_CONTENT_REFERENCE);
    }

    /**
     * 保存文章图片与素材关系到数据库中
     * @param materials
     * @param newsId
     * @param type
     */
    private void saveRelativeInfo(List<String> materials, Integer newsId, Short type) {
        if (CollectionUtils.isEmpty(materials)) {
            return;
        }

        // 通过图片url查询素材的id
        List<WmMaterial> dbMaterials = wmMaterialRepository.findByUrlIn(materials);
        List<Integer> idList = dbMaterials.stream().map(WmMaterial::getId).collect(Collectors.toList());

        //判断素材是否有效
        if (CollectionUtils.isEmpty(dbMaterials) || materials.size() != dbMaterials.size()) {
            //手动抛出异常  第一个功能：能够提示调用者素材失效了，第二个功能，进行数据的回滚
            throw new CustomException(AppHttpCodeEnum.MATERIAL_REFERENCE_FAILURE);
        }

        //批量保存
        wmNewsMaterialService.saveRelations(idList, newsId, type);
    }

    /**
     * 提取文章内容中的图片信息
     * @param content
     * @return
     */
    private List<String> extractUrlInfo(String content){
        List<String> materials = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Map.class);
            List<Map<String, String>> maps = mapper.readValue(content, collectionType);
            for (Map<String, String> map : maps) {
                if (map.get("type").equals("image")) {
                    String imgUrl = map.get("value");
                    materials.add(imgUrl);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return materials;
    }


    private void saveOrUpdateWmNews(WmNews wmNews) {
        //补全属性
        wmNews.setUserId(WmThreadLocalUtils.getUser().getId())
                .setCreatedTime(new Date())
                .setSubmitedTime(new Date())
                .setEnable((short) 1);//默认上架

        if (wmNews.getId() != null) {
            // 删除文章图片与素材的关系
            wmNewsMaterialService.deleteAllFromNewsId(wmNews.getId());
        }
        wmNewsRepository.saveAndFlush(wmNews);
    }
}
