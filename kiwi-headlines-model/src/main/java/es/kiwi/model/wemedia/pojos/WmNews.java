package es.kiwi.model.wemedia.pojos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 自媒体图文内容信息表
 */
@Data
@Entity
@ApiModel("自媒体图文内容信息表")
@Table(name = "wm_news")
@Accessors(chain = true)
public class WmNews implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @ApiModelProperty("主键")
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 自媒体用户ID
     */
    @Column(name = "user_id")
    @ApiModelProperty("自媒体用户ID")
    private Integer userId;

    /**
     * 标题
     */
    @Column(name = "title")
    @ApiModelProperty("标题")
    private String title;

    /**
     * 图文内容
     */
    @Column(name = "content")
    @ApiModelProperty("图文内容")
    private String content;

    /**
     * 文章布局
     * 0 无图文章
     * 1 单图文章
     * 3 多图文章
     */
    @Column(name = "type")
    @ApiModelProperty("文章布局")
    private Short type;

    /**
     * 图文频道ID
     */
    @ApiModelProperty("图文频道ID")
    @Column(name = "channel_id")
    private Integer channelId;

    @Column(name = "labels")
    private String labels;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 提交时间
     */
    @ApiModelProperty("提交时间")
    @Column(name = "submited_time")
    private Date submitedTime;

    /**
     * 当前状态
     * 0 草稿
     * 1 提交（待审核）
     * 2 审核失败
     * 3 人工审核
     * 4 人工审核通过
     * 8 审核通过（待发布）
     * 9 已发布
     */
    @Column(name = "status")
    @ApiModelProperty("当前状态")
    private Short status;

    /**
     * 定时发布时间，不定时则为空
     */
    @Column(name = "publish_time")
    @ApiModelProperty("定时发布时间，不定时则为空")
    private Date publishTime;

    /**
     * 拒绝理由
     */
    @Column(name = "reason")
    @ApiModelProperty("拒绝理由")
    private String reason;

    /**
     * 发布库文章ID
     */
    @Column(name = "article_id")
    @ApiModelProperty("发布库文章ID")
    private Long articleId;

    /**
     * 封面图片
     * 图片用逗号分隔
     */
    @Column(name = "images")
    @ApiModelProperty("图片用逗号分隔")
    private String images;

    @Column(name = "enable")
    private Short enable;

    /**
     * 状态枚举类
     */
    public enum Status{
        NORMAL((short)0),SUBMIT((short)1),FAIL((short)2),ADMIN_AUTH((short)3),ADMIN_SUCCESS((short)4),SUCCESS((short)8),PUBLISHED((short)9);
        final short code;
        Status(short code){
            this.code = code;
        }
        public short getCode(){
            return this.code;
        }
    }

}
