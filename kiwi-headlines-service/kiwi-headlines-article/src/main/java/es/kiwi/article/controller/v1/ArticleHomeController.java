package es.kiwi.article.controller.v1;

import es.kiwi.model.article.dtos.ArticleHomeDto;
import es.kiwi.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/article")
public class ArticleHomeController {

    /**
     * 加载首页
     * @param articleHomeDto
     * @return
     */
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto articleHomeDto) {

        return null;
    }

    /**
     * 加载更多
     * @param articleHomeDto
     * @return
     */
    @PostMapping("/loadmore")
    public ResponseResult loadmore(@RequestBody ArticleHomeDto articleHomeDto) {

        return null;
    }

    /**
     * 加载最新
     * @param articleHomeDto
     * @return
     */
    @PostMapping("/loadlatest")
    public ResponseResult loadlatest(@RequestBody ArticleHomeDto articleHomeDto) {

        return null;
    }
}
