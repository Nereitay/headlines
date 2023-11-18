package es.kiwi.article.service;

import es.kiwi.model.common.dtos.ResponseResult;

import java.util.Map;

public interface ApArticleConfigService {
    /**
     * 修改文章配置
     * @param map
     */
    void updateByMap(Map map);

    ResponseResult findByArticleId(Long articleId);
}
