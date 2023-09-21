package es.kiwi.schedule.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.common.constants.ScheduleConstants;
import es.kiwi.common.redis.CacheService;
import es.kiwi.model.schedule.dtos.Task;
import es.kiwi.model.schedule.mapstruct.mappers.TaskinfoMapper;
import es.kiwi.model.schedule.pojos.Taskinfo;
import es.kiwi.model.schedule.pojos.TaskinfoLogs;
import es.kiwi.schedule.repository.TaskinfoLogsRepository;
import es.kiwi.schedule.repository.TaskinfoRepository;
import es.kiwi.schedule.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskinfoRepository taskinfoRepository;
    @Autowired
    private TaskinfoLogsRepository taskinfoLogsRepository;
    @Autowired
    private CacheService cacheService;

    @Override
    public long addTask(Task task) {
        // 1.添加任务到数据库中
        boolean success = addTaskToDB(task);

        if (success) {
            // 2.添加任务到redis
            addTaskToCache(task);
        }

        return task.getTaskId();
    }

    /**
     * 取消任务
     *
     * @param taskId
     * @return
     */
    @Override
    public boolean cancelTask(long taskId) {
        boolean flag = false;
        //删除任务，更新日志
        Task task = updateDB(taskId, ScheduleConstants.CANCELLED);

        //删除redis的数据
        if (task != null) {
            removeTaskFromCache(task);
            flag = true;
        }
        return flag;
    }

    /**
     * 按照类型和优先级拉取任务
     *
     * @param type
     * @param priority
     * @return
     */
    @Override
    public Task poll(int type, int priority) {
        Task task = null;

        try {
            String key = type + "_" + priority;
            // 从redis中拉取数据 pop
            String task_json = cacheService.lRightPop(ScheduleConstants.TOPIC + key);
            if (StringUtils.isNotBlank(task_json)) {
                task = new ObjectMapper().readValue(task_json, Task.class);
                // 修改数据库信息
                updateDB(task.getTaskId(), ScheduleConstants.EXECUTED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("poll task exception");
        }

        return task;
    }

    /**
     * 未来数据定时刷新
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh() {
        /*分布式锁解决集群下的方法抢占执行
        *   1.在分布式系统环境下，一个方法在同一时间只能被一个机器的一个线程执行
        *   2.主要是通过redis的sexnx(set if not exists)特性完成分布式锁的功能
        *       A获取到锁以后其他客户端不能操作，只能等待A释放锁以后，其他客户端才能操作
        * */
        String token = cacheService.tryLock("FUTURE_TASK_SYNC", 1000 * 30);

        if (StringUtils.isNotBlank(token)) {
            log.info("{}执行了未来数据刷新的定时任务", System.currentTimeMillis() / 1000);
            // 获取所有未来数据集合的key值
            Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");
            for (String futureKey : futureKeys) { // future_100_50
                // 获取当前数据的key topic
                String topicKey = ScheduleConstants.TOPIC + futureKey.split(ScheduleConstants.FUTURE)[1];
                // 按照key和分值查询符合条件的数据
                Set<String> tasks = cacheService.zRangeByScore(futureKey, 0, System.currentTimeMillis());
                // 同步数据
                if (!CollectionUtils.isEmpty(tasks)) {
                    cacheService.refreshWithPipeline(futureKey, topicKey, tasks);
                    log.info("成功的将{}下的当前需要执行的任务数据刷新到{}下", futureKey, topicKey);
                }
            }
        }
    }

    /**
     * 数据库任务定时同步到redis
     */
    @PostConstruct // 微服务启动后做同步操作
    @Scheduled(cron = "0 */5 * * * ?")
    public void reloadData() {
        // 清理缓存中的数据 list zset
        clearCache();

        // 查询符合条件的任务 小于未来5分钟的数据
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        List<Taskinfo> taskinfoList = taskinfoRepository.findByExecuteTimeLessThan(calendar.getTime());

        // 把任务添加到redis
        for (Taskinfo taskinfo : taskinfoList) {
            Task task = TaskinfoMapper.INSTANCE.taskinfoToTask(taskinfo);
            addTaskToCache(task);
        }

        log.info("数据库任务同步到了redis");
    }

    /**
     * 清理缓存中的数据
     */
    public void clearCache() {
        Set<String> topicKeys = cacheService.scan(ScheduleConstants.TOPIC + "*");
        Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");
        cacheService.delete(topicKeys);
        cacheService.delete(futureKeys);
    }

    /**
     * 删除redis中的数据
     * @param task
     */
    private void removeTaskFromCache(Task task) {
        String key = task.getTaskType() + "_" + task.getPriority();
        String taskJson = null;
        try {
            taskJson = new ObjectMapper().writeValueAsString(task);
        } catch (JsonProcessingException e) {
            log.error("TaskServiceImpl - addTaskToCache json转换异常");
            throw new RuntimeException(e);
        }
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            cacheService.lRemove(ScheduleConstants.TOPIC + key, 0, taskJson);
        } else {
            cacheService.zRemove(ScheduleConstants.FUTURE + key, taskJson);
        }
    }

    /**
     * 删除任务，更新任务日志
     * @param taskId
     * @param status
     * @return
     */
    private Task updateDB(long taskId, int status) {
        Task task = null;
        try {
            // 删除任务
            taskinfoRepository.deleteById(taskId);

            // 更新任务日志
            Optional<TaskinfoLogs> taskinfoLogsOpt = taskinfoLogsRepository.findById(taskId);
            task = new Task();
            if (taskinfoLogsOpt.isPresent()) {
                TaskinfoLogs taskinfoLogs = taskinfoLogsOpt.get();
                taskinfoLogs.setStatus(status);
                taskinfoLogsRepository.save(taskinfoLogs);
                task = TaskinfoMapper.INSTANCE.taskinfoLogsToTask(taskinfoLogs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("task cancel exception taskId={}", taskId);
        }

        return task;

    }

    /**
     * 把任务添加到redis中
     * @param task
     */
    private void addTaskToCache(Task task) {

        String key = task.getTaskType() + "_" + task.getPriority();
        String taskJson = null;
        try {
            taskJson = new ObjectMapper().writeValueAsString(task);
        } catch (JsonProcessingException e) {
            log.error("TaskServiceImpl - addTaskToCache json转换异常");
            throw new RuntimeException(e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        long nextScheduleTime = calendar.getTimeInMillis();

        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            // 2.1 如果任务的执行时间小于等于当前时间，存入list
            cacheService.lLeftPush(ScheduleConstants.TOPIC + key, taskJson);
        } else if (task.getExecuteTime() <= nextScheduleTime) {
            // 2.2 如果任务的执行时间大于当前时间 && 小于等于预设时间（未来5分钟） 存入zset中
            cacheService.zAdd(ScheduleConstants.FUTURE + key, taskJson, task.getExecuteTime());
        }


    }

    /**
     * 添加任务到数据库中
     * @param task
     * @return
     */
    private boolean addTaskToDB(Task task) {

        boolean flag = false;

        try {
            // 保存任务表
            Taskinfo taskinfo = TaskinfoMapper.INSTANCE.taskToTaskinfo(task);
            taskinfo = taskinfoRepository.save(taskinfo);
            //设置taskID
            task.setTaskId(taskinfo.getTaskId());

            // 保存任务日志表
            TaskinfoLogs taskinfoLogs = TaskinfoMapper.INSTANCE.taskinfoToTaskinfoLogs(taskinfo);
            taskinfoLogs.setVersion(1);
            taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
            taskinfoLogsRepository.save(taskinfoLogs);

            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }
}
