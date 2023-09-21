package es.kiwi.wemedia.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.apis.schedule.IScheduleClient;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.TaskTypeEnum;
import es.kiwi.model.schedule.dtos.Task;
import es.kiwi.model.wemedia.pojos.WmNews;
import es.kiwi.utils.common.ProtostuffUtil;
import es.kiwi.wemedia.service.WmNewsAutoScanService;
import es.kiwi.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {

    @Autowired
    private IScheduleClient scheduleClient;
    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    /**
     * 添加任务到延迟队列中
     *
     * @param id          文章的id
     * @param publishTime 发布时间， 可以作为任务的执行时间
     */
    @Override
    @Async
    public void addNewsToTask(Integer id, Date publishTime) {
        log.info("添加任务到延迟服务中---begin");

        Task task = new Task();
        task.setExecuteTime(publishTime.getTime());
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        WmNews wmNews = new WmNews();
        wmNews.setId(id);
        task.setParameters(ProtostuffUtil.serialize(wmNews)); // 序列化

        scheduleClient.addTask(task);

        log.info("添加任务到延迟服务中---end");
    }

    /**
     * 消费任务， 审核文章
     */
    @Override
    @Scheduled(fixedRate = 1000) // 每秒拉取一次任务
    public void scanNewsByTask() {

        log.info("消费任务，审核文章");

        ResponseResult responseResult = scheduleClient.poll(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(), TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        if (responseResult.getCode().equals(200) && responseResult.getData() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Task task = objectMapper.readValue(objectMapper.writeValueAsString(responseResult.getData()), Task.class);
                WmNews wmNews = ProtostuffUtil.deserialize(task.getParameters(), WmNews.class);
                wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
            } catch (Exception e) {
                log.error("WmNewsTaskServiceImpl - scanNewsByTask json转换异常");
                throw new RuntimeException(e);
            }
        }
    }
}
