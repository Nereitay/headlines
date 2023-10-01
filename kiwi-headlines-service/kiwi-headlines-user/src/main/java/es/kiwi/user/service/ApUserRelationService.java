package es.kiwi.user.service;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.user.dtos.UserRelationDto;

public interface ApUserRelationService {
    /**
     * 用户关注/取消关注
     * @param dto
     * @return
     */
    ResponseResult follow(UserRelationDto dto);
}
