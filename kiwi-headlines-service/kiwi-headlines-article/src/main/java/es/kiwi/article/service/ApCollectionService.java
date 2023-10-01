package es.kiwi.article.service;

import es.kiwi.model.article.dtos.CollectionBehaviorDto;
import es.kiwi.model.common.dtos.ResponseResult;

public interface ApCollectionService {
    /**
     * 收藏
     * @param dto
     * @return
     */
    ResponseResult collection(CollectionBehaviorDto dto);
}
