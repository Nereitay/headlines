package es.kiwi.wemedia.service;

import es.kiwi.model.common.dtos.ResponseResult;

public interface WmChannelService {

    /**
     * 查询所有频道
     * @return
     */
    ResponseResult findAll();
}
