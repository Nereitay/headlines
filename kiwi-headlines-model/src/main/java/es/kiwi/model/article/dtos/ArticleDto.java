package es.kiwi.model.article.dtos;

import es.kiwi.model.article.pojos.ApArticle;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleDto extends ApArticle {
    /**
     * 文章内容
     */
    private String content;
}