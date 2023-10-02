package es.kiwi.model.article.vos;

import es.kiwi.model.article.pojos.ApArticle;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HotArticleVo extends ApArticle {

    /**
     * 文章分值
     */
    private Integer score;
}
