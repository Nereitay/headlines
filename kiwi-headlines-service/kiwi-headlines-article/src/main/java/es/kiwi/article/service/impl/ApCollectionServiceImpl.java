package es.kiwi.article.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.article.service.ApCollectionService;
import es.kiwi.common.constants.BehaviorConstants;
import es.kiwi.common.redis.CacheService;
import es.kiwi.model.article.dtos.CollectionBehaviorDto;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.AppHttpCodeEnum;
import es.kiwi.model.user.pojos.ApUser;
import es.kiwi.utils.thread.AppThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApCollectionServiceImpl implements ApCollectionService {

    @Autowired
    private CacheService cacheService;

    /**
     * 收藏
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult collection(CollectionBehaviorDto dto) {
        //条件判断
        if (dto == null || dto.getEntryId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //判断是否登录
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //查询
        String collectionJson = (String) cacheService.hGet(
                BehaviorConstants.COLLECTION_BEHAVIOR + user.getId(), dto.getEntryId().toString());
        if (StringUtils.isNotBlank(collectionJson) && dto.getOperation() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "已收藏");
        }
        //收藏
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String dtoJson = objectMapper.writeValueAsString(dto);
            if (dto.getOperation() == 0) {
                log.info("文章收藏，保存key:{},{},{}", dto.getEntryId(), user.getId().toString(), dtoJson);
                cacheService.hPut(BehaviorConstants.COLLECTION_BEHAVIOR + user.getId(), dto.getEntryId().toString(), dtoJson);
            } else {
                //取消收藏
                log.info("文章收藏，删除key:{},{},{}", dto.getEntryId(), user.getId().toString(), dtoJson);
                cacheService.hDelete(BehaviorConstants.COLLECTION_BEHAVIOR + user.getId(), dto.getEntryId().toString());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("{} - JsonProcessingException : {}", this.getClass().getName(), e.getMessage());
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
