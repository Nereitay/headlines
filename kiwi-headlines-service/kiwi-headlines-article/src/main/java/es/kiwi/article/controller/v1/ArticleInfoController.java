package es.kiwi.article.controller.v1;

import es.kiwi.article.service.ApArticleService;
import es.kiwi.model.article.dtos.ArticleInfoDto;
import es.kiwi.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/article")
public class ArticleInfoController {

    @Autowired
    private ApArticleService apArticleService;

    @PostMapping("/load_article_behavior")
    public ResponseResult loadArticleBehavior(@RequestBody ArticleInfoDto dto) {
        return apArticleService.loadArticleBehavior(dto);
    }
}
