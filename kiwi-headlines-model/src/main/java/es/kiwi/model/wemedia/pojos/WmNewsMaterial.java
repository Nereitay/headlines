package es.kiwi.model.wemedia.pojos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 自媒体图文引用素材信息表
 */
@Data
@Entity
@ApiModel("自媒体图文引用素材信息表")
@Table(name = "wm_news_material")
public class WmNewsMaterial implements Serializable {

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
     * 素材ID
     */
    @ApiModelProperty("素材ID")
    @Column(name = "material_id")
    private Integer materialId;

    /**
     * 图文ID
     */
    @Column(name = "news_id")
    @ApiModelProperty("图文ID")
    private Integer newsId;

    /**
     * 引用类型
     * 0 内容引用
     * 1 主图引用
     */
    @Column(name = "type")
    @ApiModelProperty("引用类型")
    private Short type;

    /**
     * 引用排序
     */
    @Column(name = "ord")
    @ApiModelProperty("引用排序")
    private Short ord;
}
