package es.kiwi.behavior.service;

import es.kiwi.model.behavior.dtos.UnlikesBehaviorDto;
import es.kiwi.model.common.dtos.ResponseResult;

public interface ApUnlikesBehaviorService {
    /**
     * 不喜欢
     * @param dto
     * @return
     */
    ResponseResult unlike(UnlikesBehaviorDto dto);
}
