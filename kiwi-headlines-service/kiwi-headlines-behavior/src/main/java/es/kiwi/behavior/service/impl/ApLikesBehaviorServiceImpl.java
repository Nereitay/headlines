package es.kiwi.behavior.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.behavior.service.ApLikesBehaviorService;
import es.kiwi.common.constants.BehaviorConstants;
import es.kiwi.common.constants.HotArticleConstants;
import es.kiwi.common.redis.CacheService;
import es.kiwi.model.behavior.dtos.LikesBehaviorDto;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.AppHttpCodeEnum;
import es.kiwi.model.msg.UpdateArticleMsg;
import es.kiwi.model.user.pojos.ApUser;
import es.kiwi.utils.thread.AppThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ApLikesBehaviorServiceImpl implements ApLikesBehaviorService {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    /**
     * 存储喜欢数据
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult like(LikesBehaviorDto dto) {
        //1.检查参数
        if (dto == null || dto.getArticleId() == null || checkParam(dto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.是否登录
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        UpdateArticleMsg msg = new UpdateArticleMsg();
        msg.setArticleId(dto.getArticleId());
        msg.setType(UpdateArticleMsg.UpdateArticleType.LIKES);

        ObjectMapper objectMapper = new ObjectMapper();
        if (dto.getOperation() == 0) {//3.点赞  保存数据
            Object obj = cacheService.hGet(
                    BehaviorConstants.LIKE_BEHAVIOR + dto.getArticleId().toString(),
                    user.getId().toString());
            if (obj != null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "已点赞");
            }
            // 保存当前key
            log.info("保存当前key:{} ,{}, {}", dto.getArticleId(), user.getId(), dto);
            try {
                cacheService.hPut(BehaviorConstants.LIKE_BEHAVIOR + dto.getArticleId().toString(),
                        user.getId().toString(), objectMapper.writeValueAsString(dto));
                msg.setAdd(1);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                log.error("{} - JsonProcessingException : {}", this.getClass().getName(), e.getMessage());
            }
        } else {// 删除当前key
            log.info("删除当前key:{}, {}", dto.getArticleId(), user.getId());
            cacheService.hDelete(BehaviorConstants.LIKE_BEHAVIOR + dto.getArticleId().toString(),
                    user.getId().toString());
            msg.setAdd(-1);
        }
        // 发送消息， 数据聚合
        try {
            kafkaTemplate.send(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC, objectMapper.writeValueAsString(msg));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("{} - JsonProcessingException : {}", this.getClass().getName(), e.getMessage());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 检查参数
     *
     * @return
     */
    private boolean checkParam(LikesBehaviorDto dto) {
        return (dto.getType() > 2
                || dto.getType() < 0
                || dto.getOperation() > 1
                || dto.getOperation() < 0);
    }
}
