package es.kiwi.wemedia.service.impl;

import es.kiwi.file.service.FileStorageService;
import es.kiwi.model.common.dtos.PageResponseResult;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.AppHttpCodeEnum;
import es.kiwi.model.wemedia.dtos.WmMaterialDto;
import es.kiwi.model.wemedia.pojos.WmMaterial;
import es.kiwi.utils.thread.WmThreadLocalUtils;
import es.kiwi.wemedia.repository.WmMaterialRepository;
import es.kiwi.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
@Slf4j
public class WmMaterialServiceImpl implements WmMaterialService {

    @Autowired
    private WmMaterialRepository wmMaterialRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        // 1. 检查参数
        if (multipartFile == null || multipartFile.isEmpty()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2. 上传图片到minIO中
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        String postfix = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));

        String fileId = null;
        try {
            fileId = fileStorageService.uploadImgFile(
                    "", fileName + postfix, multipartFile.getInputStream());
            log.info("上传图片到MinIO中，fileId:{}", fileId);
        } catch (IOException e) {
            log.error("{}-上传文件失败", this.getClass().getName());
            throw new RuntimeException(e);
        }
        //3. 保存到数据库中
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(WmThreadLocalUtils.getUser().getId())
                .setUrl(fileId)
                .setIsCollection((short) 0)
                .setType((short) 0)
                .setCreatedTime(new Date());
        wmMaterialRepository.save(wmMaterial);

        //4. 返回结果
        return ResponseResult.okResult(wmMaterial);
    }

    @Override
    public ResponseResult findList(WmMaterialDto dto) {
        // 1. 检查参数
        dto.checkParam();

        //2. 分页查询 ：是否收藏，按照用户查询，时间倒序
        Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), sort);
        Specification<WmMaterial> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.getIsCollection() != null && dto.getIsCollection() == 1) {
                predicates.add(criteriaBuilder.equal(root.get("isCollection"), dto.getIsCollection()));
            }

            predicates.add(criteriaBuilder.equal(root.get("userId"), WmThreadLocalUtils.getUser().getId()));

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<WmMaterial> page = wmMaterialRepository.findAll(spec, pageable);

        //3. 返回结果
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotalElements());
        responseResult.setData(page.getContent());
        return responseResult;
    }
}
