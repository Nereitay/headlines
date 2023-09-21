package es.kiwi.model.schedule.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "taskinfo_logs")
public class TaskinfoLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @Id
    @ApiModelProperty("任务id")
    @Column(name = "task_id", nullable = false)
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
    /**
     * 版本号,用乐观锁
     */
    @Version
    @ApiModelProperty("版本号,用乐观锁")
    @Column(name = "version", nullable = false)
    private Integer version;


    /**
     * 状态 0=初始化状态 1=EXECUTED 2=CANCELLED
     */
    @Column(name = "status")
    @ApiModelProperty("状态 0=初始化状态 1=EXECUTED 2=CANCELLED")
    private Integer status;

}
