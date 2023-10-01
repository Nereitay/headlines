package es.kiwi.behavior.service;

import es.kiwi.model.behavior.dtos.LikesBehaviorDto;
import es.kiwi.model.common.dtos.ResponseResult;

public interface ApLikesBehaviorService {
    /**
     * 存储喜欢数据
     * @param dto
     * @return
     */
    ResponseResult like(LikesBehaviorDto dto);
}
