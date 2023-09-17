package es.kiwi.model.jpa.snowflake;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 实体类的基类
 */
@MappedSuperclass
public abstract class AbstractBaseEntity implements Serializable {

    @Id
    // strategy 表示生成策略实现类的全路径名
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "es.kiwi.model.jpa.snowflake.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "snowFlakeIdGenerator")
    @Column(name = "id", length = 18)
    private Long id;

    /**
     * 获取主键id
     * @return id 如果是Long类型，前端js能处理的长度(16)低于java(18)，可以改成String防止精度丢失
     */
    public Long getId() {
        return id;
    }

    /**
     *设置主键id
     * @param id 主键id
     */
    public void setId(Long id) {
        this.id = id;
    }
}
