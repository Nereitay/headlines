package es.kiwi.article.service;

import es.kiwi.model.article.dtos.ArticleHomeDto;
import es.kiwi.model.article.pojos.ApArticle;
import es.kiwi.model.common.dtos.ResponseResult;

import java.util.List;

public interface ApArticleService {


    ResponseResult load(ArticleHomeDto dto, Short type);

    List<ApArticle> loadArticleList(ArticleHomeDto dto, Short type);

}
