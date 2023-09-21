package es.kiwi.model.schedule.mapstruct.mappers;

import es.kiwi.model.schedule.dtos.Task;
import es.kiwi.model.schedule.pojos.Taskinfo;
import es.kiwi.model.schedule.pojos.TaskinfoLogs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskinfoMapper {
    TaskinfoMapper INSTANCE = Mappers.getMapper(TaskinfoMapper.class);

    @Mappings(
            @Mapping(target = "executeTime",
                    expression = "java(new java.util.Date(task.getExecuteTime()))")
    )
    Taskinfo taskToTaskinfo(Task task);

    TaskinfoLogs taskinfoToTaskinfoLogs(Taskinfo taskinfo);

    @Mappings(
            @Mapping(target = "executeTime",
                    expression = "java(taskinfoLogs.getExecuteTime().getTime())")
    )
    Task taskinfoLogsToTask(TaskinfoLogs taskinfoLogs);

    @Mappings(
            @Mapping(target = "executeTime",
                    expression = "java(taskinfo.getExecuteTime().getTime())")
    )
    Task taskinfoToTask(Taskinfo taskinfo);
}
