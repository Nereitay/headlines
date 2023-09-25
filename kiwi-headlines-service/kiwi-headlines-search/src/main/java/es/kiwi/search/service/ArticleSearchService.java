package es.kiwi.search.service;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.search.dtos.UserSearchDto;

import java.io.IOException;

public interface ArticleSearchService {
    /**
     * es文章分页检索
     * @param dto
     * @return
     */
    ResponseResult search(UserSearchDto dto) throws IOException;
}
