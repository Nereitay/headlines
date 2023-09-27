package es.kiwi.search.service;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.search.dtos.UserSearchDto;

public interface ApAssociateWordsService {

    /**
     * 搜索联想词
     * @param dto
     * @return
     */
    ResponseResult search(UserSearchDto dto);

}
