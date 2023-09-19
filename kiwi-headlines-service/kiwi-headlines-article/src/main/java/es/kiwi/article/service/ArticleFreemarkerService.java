package es.kiwi.article.service;

import es.kiwi.model.article.pojos.ApArticle;

public interface ArticleFreemarkerService {

    /**
     * 生成静态文件上传到minio中
     * @param apArticle
     * @param content
     */
    void buildArticleToMinIO(ApArticle apArticle, String content);
}
