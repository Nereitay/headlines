package es.kiwi.wemedia.service;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.wemedia.dtos.ChannelDto;
import es.kiwi.model.wemedia.pojos.WmChannel;

public interface WmChannelService {

    /**
     * 查询所有频道
     * @return
     */
    ResponseResult findAll();

    /**
     * 查询
     * @param dto
     * @return
     */
    ResponseResult findByNameAndPage(ChannelDto dto);

    /**
     * 新增
     * @param wmChannel
     * @return
     */
    ResponseResult insert(WmChannel wmChannel);

    /**
     * 修改
     * @param wmChannel
     * @return
     */
    ResponseResult update(WmChannel wmChannel);

    /**
     * 删除
     * @param id
     * @return
     */
    ResponseResult delete(Integer id);
}
