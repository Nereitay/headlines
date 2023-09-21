package es.kiwi.schedule.service.impl;

import es.kiwi.model.schedule.dtos.Task;
import es.kiwi.schedule.ScheduleApplication;
import es.kiwi.schedule.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class TaskServiceImplTest {

    @Autowired
    private TaskService taskService;

    @Test
    public void addTask() {
        for (int i = 0; i < 5; i++) {
            Task task = new Task();
            task.setTaskType(100 + i);
            task.setPriority(50);
            task.setParameters("task test".getBytes());
            task.setExecuteTime(new Date().getTime() + 5000 * i); // 运行时间不同，redis存储的地方不同

            long taskId = taskService.addTask(task);
            System.out.println(taskId);
        }

    }

    @Test
    public void cancelTask() {
        taskService.cancelTask(576358275319201792L);
    }

    @Test
    public void pollTask() {
        Task task = taskService.poll(100, 50);
        System.out.println(task);
    }
}