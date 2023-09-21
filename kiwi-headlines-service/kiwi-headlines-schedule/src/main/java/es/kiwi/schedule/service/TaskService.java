package es.kiwi.schedule.service;

import es.kiwi.model.schedule.dtos.Task;

public interface TaskService {

    /**
     * 添加延迟任务
     * @param task
     * @return
     */
    long addTask(Task task);

    /**
     * 取消任务
     * @param taskId
     * @return
     */
    boolean cancelTask(long taskId);

    /**
     * 按照类型和优先级拉取任务
     * @param type
     * @param priority
     * @return
     */
    Task poll(int type, int priority);


}
