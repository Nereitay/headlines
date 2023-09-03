package es.kiwi.model.article.pojos;

import lombok.Data;

import javax.persistence.*;

/**
 * APP已发布文章配置表
 */
@Data
@Entity
@Table(name = "ap_article_config")
public class ApArticleConfig {

    /**
     * 主键
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
