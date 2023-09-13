package es.kiwi.model.wemedia.pojos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 频道信息表
 */
@Data
@Entity
@ApiModel("频道信息表")
@Table(name = "wm_channel")
public class WmChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 频道名称
     */
    @Column(name = "name")
    @ApiModelProperty("频道名称")
    private String name;

    /**
     * 频道描述
     */
    @ApiModelProperty("频道描述")
    @Column(name = "description")
    private String description;

    /**
     * 是否默认频道
     * 1: 默认 true
     * 0: 非默认 false
     */
    @ApiModelProperty("是否默认频道")
    @Column(name = "is_default")
    private Boolean isDefault;

    /**
     * 是否启用
     * 1：启用
     * 0：禁用
     */
    @Column(name = "status")
    private Boolean status;

    /**
     * 默认排序
     */
    @Column(name = "ord")
    @ApiModelProperty("默认排序")
    private Integer ord;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column(name = "created_time")
    private Date createdTime;

}
