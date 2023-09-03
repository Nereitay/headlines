package es.kiwi.model.user.pojos;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>
 * APP用户信息表
 * </p>
 */
@Entity
@Table(name = "ap_user", schema = "headlines_user")
@Data
public class ApUser {

    /**
     * 主键
     */
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 密码、通信等加密盐
     */
    @Column(name = "salt")
    private String salt;

    /**
     * 用户名
     */
    @Column(name = "name")
    private String name;

    /**
     * 密码,md5加密
     */
    @Column(name = "password")
    private String password;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 头像
     */
    @Column(name = "image")
    private String image;


    /**
     *  0 男
        1 女
        2 未知
     */
    @Column(name = "sex")
    private Boolean sex;

    /**
     *  0 未
        1 是
     */
    @Column(name = "is_certification")
    private Boolean isCertification;

    /**
     * 是否身份认证
     */
    @Column(name = "is_identity_authentication")
    private Boolean isIdentityAuthentication;

    /**
     *  0正常
        1锁定
     */
    @Column(name = "status")
    private Boolean status;

    /**
     *  0 普通用户
        1 自媒体人
        2 大V
     */
    @Column(name = "flag")
    private Short flag;

    /**
     * 注册时间
     */
    @Column(name = "created_time")
    private Date createdTime;


}
