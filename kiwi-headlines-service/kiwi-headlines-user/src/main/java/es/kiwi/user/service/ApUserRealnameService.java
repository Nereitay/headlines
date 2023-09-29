package es.kiwi.user.service;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.user.dtos.AuthDto;

public interface ApUserRealnameService {
    /**
     * 按照状态分页查询用户列表
     * @param dto
     * @return
     */
    ResponseResult loadListByStatus(AuthDto dto);

    /**
     *
     * @param dto
     * @param status  2 审核失败    9 审核成功
     * @return
     */
    ResponseResult updateStatus(AuthDto dto, Short status);
}
