package es.kiwi.model.wemedia.pojos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 自媒体用户信息表
 */
@Data
@Entity
@ApiModel("自媒体用户信息表")
@Table(name = "wm_user")
public class WmUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @ApiModelProperty("主键")
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ap_user_id")
    private Integer apUserId;

    @Column(name = "ap_author_id")
    private Integer apAuthorId;

    /**
     * 登录用户名
     */
    @Column(name = "name")
    @ApiModelProperty("登录用户名")
    private String name;

    /**
     * 登录密码
     */
    @ApiModelProperty("登录密码")
    @Column(name = "password")
    private String password;

    /**
     * 盐
     */
    @Column(name = "salt")
    @ApiModelProperty("盐")
    private String salt;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    @Column(name = "nickname")
    private String nickname;

    /**
     * 头像
     */
    @Column(name = "image")
    @ApiModelProperty("头像")
    private String image;

    /**
     * 归属地
     */
    @ApiModelProperty("归属地")
    @Column(name = "location")
    private String location;

    /**
     * 手机号
     */
    @Column(name = "phone")
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 状态
     * 0 暂时不可用
     * 1 永久不可用
     * 9 正常可用
     */
    @Column(name = "status")
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 邮箱
     */
    @Column(name = "email")
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 账号类型
     * 0 个人
     * 1 企业
     * 2 子账号
     */
    @Column(name = "type")
    @ApiModelProperty("账号类型")
    private Integer type;

    /**
     * 运营评分
     */
    @Column(name = "score")
    @ApiModelProperty("运营评分")
    private Integer score;

    /**
     * 最后一次登录时间
     */
    @Column(name = "login_time")
    @ApiModelProperty("最后一次登录时间")
    private Date loginTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column(name = "created_time")
    private Date createdTime;
}
