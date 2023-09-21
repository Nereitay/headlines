package es.kiwi.model.schedule.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "taskinfo")
public class Taskinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @Id
    @ApiModelProperty("任务id")
    @Column(name = "task_id", nullable = false)
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "es.kiwi.model.jpa.snowflake.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "snowFlakeIdGenerator")
    private Long taskId;

    /**
     * 执行时间
     */
    @ApiModelProperty("执行时间")
    @Column(name = "execute_time", nullable = false)
    private Date executeTime;

    /**
     * 参数
     */
    @ApiModelProperty("参数")
    @Column(name = "parameters")
    private byte[] parameters;

    /**
     * 优先级
     */
    @ApiModelProperty("优先级")
    @Column(name = "priority", nullable = false)
    private Integer priority;

    /**
     * 任务类型
     */
    @ApiModelProperty("任务类型")
    @Column(name = "task_type", nullable = false)
    private Integer taskType;

}
