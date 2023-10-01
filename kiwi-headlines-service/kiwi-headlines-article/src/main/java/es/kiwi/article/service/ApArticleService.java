package es.kiwi.article.service;

import es.kiwi.model.article.dtos.ArticleDto;
import es.kiwi.model.article.dtos.ArticleHomeDto;
import es.kiwi.model.article.dtos.ArticleInfoDto;
import es.kiwi.model.article.pojos.ApArticle;
import es.kiwi.model.common.dtos.ResponseResult;

import java.util.List;

public interface ApArticleService {


    ResponseResult load(ArticleHomeDto dto, Short type);

    List<ApArticle> loadArticleList(ArticleHomeDto dto, Short type);

    ResponseResult saveArticle(ArticleDto dto);

    /**
     * 加载文章详情 数据回显
     * @param dto
     * @return
     */
    ResponseResult loadArticleBehavior(ArticleInfoDto dto);
}
