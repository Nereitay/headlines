package es.kiwi.article.service;

import es.kiwi.model.article.dtos.ArticleDto;
import es.kiwi.model.article.dtos.ArticleHomeDto;
import es.kiwi.model.article.dtos.ArticleInfoDto;
import es.kiwi.model.article.pojos.ApArticle;
import es.kiwi.model.common.dtos.ResponseResult;

import java.util.Date;
import java.util.List;

public interface ApArticleService {


    ResponseResult load(ArticleHomeDto dto, Short type);

    ResponseResult load2(ArticleHomeDto dto, Short type, boolean firstPage);

    List<ApArticle> loadArticleList(ArticleHomeDto dto, Short type);

    ResponseResult saveArticle(ArticleDto dto);

    /**
     * 加载文章详情 数据回显
     * @param dto
     * @return
     */
    ResponseResult loadArticleBehavior(ArticleInfoDto dto);

    List<ApArticle> findArticleListByLast5Days(Date dayParam);
}
