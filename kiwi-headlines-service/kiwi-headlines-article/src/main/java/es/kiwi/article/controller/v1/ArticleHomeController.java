package es.kiwi.article.controller.v1;

import es.kiwi.article.service.ApArticleService;
import es.kiwi.common.constants.ArticleConstants;
import es.kiwi.model.article.dtos.ArticleHomeDto;
import es.kiwi.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/article")
public class ArticleHomeController {
    @Autowired
    private ApArticleService apArticleService;

    /**
     * 加载首页
     * @param articleHomeDto
     * @return
     */
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto articleHomeDto) {

//        return apArticleService.load(articleHomeDto, ArticleConstants.LOADTYPE_LOAD_MORE);
        return apArticleService.load2(articleHomeDto, ArticleConstants.LOADTYPE_LOAD_MORE, true);
    }

    /**
     * 加载更多
     * @param articleHomeDto
     * @return
     */
    @PostMapping("/loadmore")
    public ResponseResult loadmore(@RequestBody ArticleHomeDto articleHomeDto) {

        return apArticleService.load(articleHomeDto, ArticleConstants.LOADTYPE_LOAD_MORE);
    }

    /**
     * 加载最新
     * @param articleHomeDto
     * @return
     */
    @PostMapping("/loadnew")
    public ResponseResult loadlatest(@RequestBody ArticleHomeDto articleHomeDto) {

        return apArticleService.load(articleHomeDto, ArticleConstants.LOADTYPE_LOAD_LATEST);
    }
}
