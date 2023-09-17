package es.kiwi.model.article.pojos;

import es.kiwi.model.jpa.snowflake.AbstractBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * APP已发布文章配置表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "ap_article_config")
@NoArgsConstructor
public class ApArticleConfig extends AbstractBaseEntity {

    public ApArticleConfig(Long articleId) {
        this.articleId = articleId;
        this.isDelete = false;
        this.isDown = false;
        this.isForward = true;
        this.isComment = true;
    }

    /**
     * 文章ID
     */
    @Column(name = "article_id")
    private Long articleId;

    /**
     * 是否可评论
     */
    @Column(name = "is_comment")
    private Boolean isComment;

    /**
     * 是否转发
     */
    @Column(name = "is_forward")
    private Boolean isForward;

    /**
     * 是否下架
     */
    @Column(name = "is_down")
    private Boolean isDown;

    /**
     * 是否已删除
     */
    @Column(name = "is_delete")
    private Boolean isDelete;

}
