package es.kiwi.wemedia.service;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.wemedia.dtos.WmLoginDto;

public interface WmUserService {

    /**
     * 自媒体端登录
     * @param dto
     * @return
     */
   ResponseResult login(WmLoginDto dto);
}
