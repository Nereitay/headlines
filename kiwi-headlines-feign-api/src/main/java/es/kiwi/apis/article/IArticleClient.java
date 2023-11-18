package es.kiwi.apis.article;

import es.kiwi.apis.article.fallback.IArticleClientFallback;
import es.kiwi.model.article.dtos.ArticleDto;
import es.kiwi.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "headlines-article", fallback = IArticleClientFallback.class)
public interface IArticleClient {
    @PostMapping("api/v1/article/save")
    ResponseResult saveArticle(@RequestBody ArticleDto dto);

    @GetMapping("/api/v1/article/findArticleConfigByArticleId/{articleId}")
    ResponseResult findArticleConfigByArticleId(@PathVariable("articleId") Long articleId);
}
