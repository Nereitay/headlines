package es.kiwi.apis.article;

import es.kiwi.model.article.dtos.ArticleDto;
import es.kiwi.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "headlines-article")
public interface IArticleClient {
    @PostMapping("api/v1/article/save")
    ResponseResult saveArticle(@RequestBody ArticleDto dto);
}
