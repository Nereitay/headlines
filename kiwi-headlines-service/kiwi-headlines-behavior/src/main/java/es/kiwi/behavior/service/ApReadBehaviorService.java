package es.kiwi.behavior.service;

import es.kiwi.model.behavior.dtos.ReadBehaviorDto;
import es.kiwi.model.common.dtos.ResponseResult;

public interface ApReadBehaviorService {
    /**
     * 保存阅读行为
     * @param dto
     * @return
     */
    ResponseResult readBehavior(ReadBehaviorDto dto);
}
