package es.kiwi.model.wemedia.pojos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 自媒体图文素材信息表
 */
@Data
@Entity
@ApiModel("自媒体图文素材信息表")
@Table(name = "wm_material")
@Accessors(chain = true)
public class WmMaterial implements Serializable {

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
     * 图片地址
     */
    @Column(name = "url")
    @ApiModelProperty("图片地址")
    private String url;

    /**
     * 素材类型
     * 0 图片
     * 1 视频
     */
    @Column(name = "type")
    @ApiModelProperty("素材类型")
    private Short type;

    /**
     * 是否收藏
     */
    @ApiModelProperty("是否收藏")
    @Column(name = "is_collection")
    private Short isCollection;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column(name = "created_time")
    private Date createdTime;
}
