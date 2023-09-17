package es.kiwi.wemedia.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import es.kiwi.apis.article.IArticleClient;
import es.kiwi.common.aliyun.GreenImageScan;
import es.kiwi.common.aliyun.GreenTextScan;
import es.kiwi.file.service.FileStorageService;
import es.kiwi.model.article.dtos.ArticleDto;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.wemedia.mapstruct.mappers.WmNewsMapper;
import es.kiwi.model.wemedia.pojos.WmChannel;
import es.kiwi.model.wemedia.pojos.WmNews;
import es.kiwi.model.wemedia.pojos.WmUser;
import es.kiwi.wemedia.repository.WmChannelRepository;
import es.kiwi.wemedia.repository.WmNewsRepository;
import es.kiwi.wemedia.repository.WmUserRepository;
import es.kiwi.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {

    @Autowired
    private WmNewsRepository wmNewsRepository;
    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private GreenImageScan greenImageScan;
    @Autowired
    private IArticleClient articleClient;
    @Autowired
    private WmChannelRepository wmChannelRepository;
    @Autowired
    private WmUserRepository wmUserRepository;

    @Override
    public void autoScanWmNews(Integer id) {

        // 1. 查询自媒体文章
        Optional<WmNews> wmNewsOpt = wmNewsRepository.findById(id);
        WmNews wmNews = wmNewsOpt.orElseThrow(() -> new RuntimeException("WmNewsAutoScanServiceImpl-文章不存在"));
        if (wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())) {
            //从内容中提取纯文本内容和图片
            Map<String, Object> textAndImages = handleTextAndImages(wmNews);
            //2.审核文本内容  阿里云接口
            boolean isTextScan = handleTextScan((String) textAndImages.get("content"), wmNews);
            if (!isTextScan) {
                return;
            }
            //3.审核图片  阿里云接口
            boolean isImageScan = handleImageScan((List<String>) textAndImages.get("images"), wmNews);
            /*if (!isImageScan) {
                return;
            }*/

            //4.审核成功，保存app端的相关的文章数据
            ResponseResult responseResult = saveAppArticle(wmNews);
            if (!responseResult.getCode().equals(200)) {
                throw new RuntimeException("WmNewsAutoScanServiceImpl-文章审核，保存app端相关文章数据失败");
            }
            //回填article_id
            wmNews.setArticleId((Long)responseResult.getData());
            updateWmNews(wmNews, (short) 9, "审核成功");
        }

    }

    /**
     * 保存app端相关的文章数据
     * @param wmNews
     */
    private ResponseResult saveAppArticle(WmNews wmNews) {
        // 属性拷贝
        ArticleDto dto = WmNewsMapper.INSTANCE.pojoToArticleAto(wmNews);
        // 频道
        Optional<WmChannel> wmChannelOpt = wmChannelRepository.findById(wmNews.getChannelId());
        wmChannelOpt.ifPresent(wmChannel -> dto.setChannelName(wmChannel.getName()));
        // 作者姓名
        Optional<WmUser> wmUserOpt = wmUserRepository.findById(wmNews.getUserId());
        wmUserOpt.ifPresent(wmUser -> dto.setAuthorName(wmUser.getName()));
        // 创建时间
        dto.setCreatedTime(new Date());

        return articleClient.saveArticle(dto);
    }

    /**
     * 审核图片
     * @param images
     * @param wmNews
     * @return
     */
    private boolean handleImageScan(List<String> images, WmNews wmNews) {
        boolean flag = true;

        if (CollectionUtils.isEmpty(images)) {
            return flag;
        }

        // 下载图片
        //图片去重
        images = images.stream().distinct().collect(Collectors.toList());

        List<byte[]> imageList = new ArrayList<>();
        for (String image : images) {
            byte[] bytes = fileStorageService.downLoadFile(image);
            imageList.add(bytes);
        }
        // 审核图片
        try {
            Map map = greenImageScan.imageScan(imageList);
            if (map != null) {
                // 审核失败
                if (map.get("suggestion").equals("block")) {
                    flag = false;
                    updateWmNews(wmNews, (short) 1, "当前文章中存在违规内容");
                }
                // 不确定信息 需要人工审核
                if (map.get("suggestion").equals("review")) {
                    flag = false;
                    updateWmNews(wmNews, (short) 3, "当前文章中存在不确定内容");
                }
            }
        } catch (Exception e) {
            flag = false;
            log.error("WmNewsAutoScanServiceImpl-greenImageScan.imageScan异常");
        }

        return flag;
    }

    /**
     * 审核纯文本内容
     * @param content
     * @param wmNews
     * @return
     */
    private boolean handleTextScan(String content, WmNews wmNews) {
        boolean flag = true;

        if ((wmNews.getTitle() + content).length() == 0) {
            return flag;
        }

        try {
            Map map = greenTextScan.greeTextScan(wmNews.getTitle() + "-" + content);
            if (map != null) {
                // 审核失败
                if (map.get("suggestion").equals("block")) {
                    flag = false;
                    updateWmNews(wmNews, (short) 1, "当前文章中存在违规内容");
                }
                // 不确定信息 需要人工审核
                if (map.get("suggestion").equals("review")) {
                    flag = false;
                    updateWmNews(wmNews, (short) 3, "当前文章中存在不确定内容");
                }
            }
        } catch (Exception e) {
            flag = false;
            log.error("WmNewsAutoScanServiceImpl-greenTextScan.greeTextScan异常");
        }

        return flag;
    }

    /**
     * 修改文章内容
     * @param wmNews
     * @param status
     * @param reason
     */
    private void updateWmNews(WmNews wmNews, short status, String reason) {
        wmNews.setStatus(status).setReason(reason);
        wmNewsRepository.save(wmNews);
    }

    /**
     * 1. 从自媒体内容中提取纯文本内容和图片
     * 2. 提取文章的封面图片
     * @param wmNews
     * @return
     */
    private Map<String, Object> handleTextAndImages(WmNews wmNews) {
        // 存储纯文本内容
        StringBuilder stringBuilder = new StringBuilder();
        // 存储图片内容
        List<String> images = new ArrayList<>();
        // 1. 从自媒体内容中提取纯文本内容和图片
        if (StringUtils.isNotBlank(wmNews.getContent())) {
            ObjectMapper objectMapper = new ObjectMapper();
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Map.class);
            try {
                List<Map<String, String>> maps = objectMapper.readValue(wmNews.getContent(), collectionType);
                for (Map<String, String> map : maps) {
                    if (map.get("type").equals("text")) {
                        stringBuilder.append(map.get("value"));
                    }
                    if (map.get("type").equals("image")) {
                        images.add(map.get("value"));
                    }
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        // 2. 提取文章的封面图片
        if (StringUtils.isNotBlank(wmNews.getImages())) {
            String[] split = wmNews.getImages().split(",");
            images.addAll(Arrays.asList(split));
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("content", stringBuilder.toString());
        resultMap.put("images", images);
        return resultMap;
    }
}
