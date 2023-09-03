package es.kiwi.model.article.pojos;

import lombok.Data;

import javax.persistence.*;

/**
 * APP已发布文章内容表
 */
@Data
@Entity
@Table(name = "ap_article_content")
public class ApArticleContent {

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
     * 文章内容
     */
    @Column(name = "content")
    private String content;

}
