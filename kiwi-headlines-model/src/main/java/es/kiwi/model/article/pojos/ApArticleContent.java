package es.kiwi.model.article.pojos;

import es.kiwi.model.jpa.snowflake.AbstractBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * APP已发布文章内容表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "ap_article_content")
public class ApArticleContent extends AbstractBaseEntity {

    /**
     * 文章ID
     */
    @Column(name = "article_id")
    private Long articleId;

    /**
     * 文章内容
     */
    @Column(name = "content")
    private String content;

}
