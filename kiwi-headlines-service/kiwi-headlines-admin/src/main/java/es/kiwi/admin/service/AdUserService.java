package es.kiwi.admin.service;

import es.kiwi.model.admin.dtos.AdUserDto;
import es.kiwi.model.common.dtos.ResponseResult;

public interface AdUserService {
    /**
     * 登录
     * @param dto
     * @return
     */
    ResponseResult login(AdUserDto dto);
}
