package es.kiwi.behavior.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.behavior.service.ApReadBehaviorService;
import es.kiwi.common.constants.BehaviorConstants;
import es.kiwi.common.redis.CacheService;
import es.kiwi.model.behavior.dtos.ReadBehaviorDto;
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
public class ApReadBehaviorServiceImpl implements ApReadBehaviorService {

    @Autowired
    private CacheService cacheService;


    /**
     * 保存阅读行为
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult readBehavior(ReadBehaviorDto dto) {
        //1.检查参数
        if (dto == null || dto.getArticleId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.是否登录
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        //更新阅读次数
        String readBehaviorJson = (String) cacheService.hGet(
                BehaviorConstants.READ_BEHAVIOR + dto.getArticleId().toString(), user.getId().toString());
        ObjectMapper objectMapper = new ObjectMapper();
        if (StringUtils.isNotBlank(readBehaviorJson)) {
            try {
                ReadBehaviorDto readBehaviorDto = objectMapper.readValue(readBehaviorJson, ReadBehaviorDto.class);
                dto.setCount((short) (readBehaviorDto.getCount() + dto.getCount()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                log.error("{} - JsonProcessingException : {}", this.getClass().getName(), e.getMessage());
            }
        }
        // 保存当前key
        log.info("保存当前key:{} {} {}", dto.getArticleId(), user.getId(), dto);
        try {
            cacheService.hPut(
                    BehaviorConstants.READ_BEHAVIOR + dto.getArticleId().toString(),
                    user.getId().toString(), objectMapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("{} - JsonProcessingException : {}", this.getClass().getName(), e.getMessage());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
