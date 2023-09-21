package es.kiwi.schedule.test;

import es.kiwi.common.redis.CacheService;
import es.kiwi.model.schedule.dtos.Task;
import es.kiwi.schedule.ScheduleApplication;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private CacheService cacheService;

    @Test
    public void testList() {

        // 在list左边添加元素
//        cacheService.lLeftPush("list_001", "Hello, Redis!");

        // 在list右边获取元素并删除
        String list001 = cacheService.lRightPop("list_001");
        System.out.println(list001);

    }

    @Test
    public void testZset() {
        // 添加数据到zset中 分值
        /*cacheService.zAdd("zset_key_001", "hello zset 001", 1000);
        cacheService.zAdd("zset_key_001", "hello zset 002", 8888);
        cacheService.zAdd("zset_key_001", "hello zset 003", 7777);
        cacheService.zAdd("zset_key_001", "hello zset 004", 999999);*/
        // 按照分值获取数据
        Set<String> zsetKey001 = cacheService.zRangeByScore("zset_key_001", 0, 8888);
        System.out.println(zsetKey001);
    }

    @Test
    public void testKeys() {
        Set<String> keys = cacheService.keys("future_*");
        System.out.println(keys);

        System.out.println(cacheService.scan("future_*"));
    }

    @Test
    public  void testPipe1() throws IOException {
        long start =System.currentTimeMillis();
        for (int i = 0; i <10000 ; i++) {
            Task task = new Task();
            task.setTaskType(1001);
            task.setPriority(1);
            task.setExecuteTime(new Date().getTime());
            cacheService.lLeftPush("1001_1", new ObjectMapper().writeValueAsString(task));
        }
        System.out.println("耗时"+(System.currentTimeMillis()- start));
    }


    @Test
    public void testPipe2(){
        long start  = System.currentTimeMillis();
        //使用管道技术
        List<Object> objectList = cacheService.getstringRedisTemplate().executePipelined(new RedisCallback<Object>() {
            @Nullable
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (int i = 0; i <10000 ; i++) {
                    Task task = new Task();
                    task.setTaskType(1001);
                    task.setPriority(1);
                    task.setExecuteTime(new Date().getTime());
                    try {
                        redisConnection.lPush("1001_1".getBytes(), new ObjectMapper().writeValueAsString(task).getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return null;
            }
        });
        System.out.println("使用管道技术执行10000次自增操作共耗时:"+(System.currentTimeMillis()-start)+"毫秒");
    }
}
