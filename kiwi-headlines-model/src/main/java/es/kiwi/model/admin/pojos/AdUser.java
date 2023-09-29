package es.kiwi.model.admin.pojos;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 管理员用户信息表
 */
@Data
@Entity
@Table(name = "ad_user")
public class AdUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 登录用户名
     */
    @Column(name = "name")
    private String name;

    /**
     * 登录密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 盐
     */
    @Column(name = "salt")
    private String salt;

    /**
     * 昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 头像
     */
    @Column(name = "image")
    private String image;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 状态
     * 0 暂时不可用
     * 1 永久不可用
     * 9 正常可用
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 最后一次登录时间
     */
    @Column(name = "login_time")
    private Date loginTime;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

}
