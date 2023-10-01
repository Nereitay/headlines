package es.kiwi.behavior.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.behavior.service.ApUnlikesBehaviorService;
import es.kiwi.common.constants.BehaviorConstants;
import es.kiwi.common.redis.CacheService;
import es.kiwi.model.behavior.dtos.UnlikesBehaviorDto;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.AppHttpCodeEnum;
import es.kiwi.model.user.pojos.ApUser;
import es.kiwi.utils.thread.AppThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApUnlikesBehaviorServiceImpl implements ApUnlikesBehaviorService {

    @Autowired
    private CacheService cacheService;

    /**
     * 不喜欢
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult unlike(UnlikesBehaviorDto dto) {
        if (dto.getArticleId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        if (dto.getType() == 0) {
            log.info("保存当前key:{} ,{}, {}", dto.getArticleId(), user.getId(), dto);
            try {
                cacheService.hPut(BehaviorConstants.UN_LIKE_BEHAVIOR + dto.getArticleId().toString(),
                        user.getId().toString(), new ObjectMapper().writeValueAsString(dto));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                log.error("{} - JsonProcessingException : {}", this.getClass().getName(), e.getMessage());
            }
        } else {
            log.info("删除当前key:{} ,{}, {}", dto.getArticleId(), user.getId(), dto);
            cacheService.hDelete(BehaviorConstants.UN_LIKE_BEHAVIOR + dto.getArticleId().toString(),
                    user.getId().toString());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
