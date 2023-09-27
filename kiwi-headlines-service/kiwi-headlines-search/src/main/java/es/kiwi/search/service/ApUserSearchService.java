package es.kiwi.search.service;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.search.dtos.HistorySearchDto;

public interface ApUserSearchService {

    /**
     * 保存用户搜索历史纪录
     * @param keyword
     * @param userId
     */
    void insert(String keyword, Integer userId);

    /**
     * 查询搜索历史
     * @return
     */
    ResponseResult findUserSearch();

    /**
     * 删除历史记录
     * @param dto
     * @return
     */
    ResponseResult delUserSearch(HistorySearchDto dto);
}
