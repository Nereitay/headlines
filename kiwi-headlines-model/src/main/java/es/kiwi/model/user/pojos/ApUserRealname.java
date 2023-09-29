package es.kiwi.model.user.pojos;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * APP实名认证信息表
 */
@Data
@Entity
@Table(name = "ap_user_realname")
public class ApUserRealname implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 账号ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 资源名称
     */
    @Column(name = "idno")
    private String idno;

    /**
     * 正面照片
     */
    @Column(name = "font_image")
    private String fontImage;

    /**
     * 背面照片
     */
    @Column(name = "back_image")
    private String backImage;

    /**
     * 手持照片
     */
    @Column(name = "hold_image")
    private String holdImage;

    /**
     * 活体照片
     */
    @Column(name = "live_image")
    private String liveImage;

    /**
     * 状态
     * 0 创建中
     * 1 待审核
     * 2 审核失败
     * 9 审核通过
     */
    @Column(name = "status")
    private Short status;

    /**
     * 拒绝原因
     */
    @Column(name = "reason")
    private String reason;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 提交时间
     */
    @Column(name = "submited_time")
    private Date submitedTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;

}
