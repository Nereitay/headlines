package es.kiwi.model.article.pojos;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 文章信息表，存储已发布的文章
 */
@Data
@Entity
@Table(name = "ap_article")
public class ApArticle {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 文章作者的ID
     */
    @Column(name = "author_id")
    private Long authorId;

    /**
     * 作者昵称
     */
    @Column(name = "author_name")
    private String authorName;

    /**
     * 文章所属频道ID
     */
    @Column(name = "channel_id")
    private Integer channelId;

    /**
     * 频道名称
     */
    @Column(name = "channel_name")
    private String channelName;

    /**
     * 文章布局
     * 0 无图文章
     * 1 单图文章
     * 2 多图文章
     */
    @Column(name = "layout")
    private Short layout;

    /**
     * 文章标记
     * 0 普通文章
     * 1 热点文章
     * 2 置顶文章
     * 3 精品文章
     * 4 大V 文章
     */
    @Column(name = "flag")
    private Byte flag;

    /**
     * 文章图片
     * 多张逗号分隔
     */
    @Column(name = "images")
    private String images;

    /**
     * 文章标签最多3个 逗号分隔
     */
    @Column(name = "labels")
    private String labels;

    /**
     * 点赞数量
     */
    @Column(name = "likes")
    private Integer likes;

    /**
     * 收藏数量
     */
    @Column(name = "collection")
    private Integer collection;

    /**
     * 评论数量
     */
    @Column(name = "comment")
    private Integer comment;

    /**
     * 阅读数量
     */
    @Column(name = "views")
    private Integer views;

    /**
     * 省市
     */
    @Column(name = "province_id")
    private Integer provinceId;

    /**
     * 市区
     */
    @Column(name = "city_id")
    private Integer cityId;

    /**
     * 区县
     */
    @Column(name = "county_id")
    private Integer countyId;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 发布时间
     */
    @Column(name = "publish_time")
    private Date publishTime;

    /**
     * 同步状态
     */
    @Column(name = "sync_status")
    private Boolean syncStatus;

    /**
     * 来源
     */
    @Column(name = "origin")
    private Boolean origin;

    /**
     * 静态页面地址
     */
    @Column(name = "static_url")
    private String staticUrl;

}
