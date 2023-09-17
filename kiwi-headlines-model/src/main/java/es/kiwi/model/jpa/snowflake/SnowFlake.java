package es.kiwi.model.jpa.snowflake;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 雪花算法类
 */
@Slf4j
@Component
public class SnowFlake implements ApplicationContextAware {

    private static ApplicationContext _applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SnowFlake._applicationContext = applicationContext;
    }

    public static SnowFlake getBean() {
        return _applicationContext.getBean(SnowFlake.class);
    }

    /**
     * 起始的时间戳, 可以根据业务需求更改
     */
    private final long twepoch = 1557825652094L;

    /*每一部分占用的位数*/
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;
    private final long sequenceBits = 12L;

    /*每一部分的最大值 结果是31*/
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private final long maxSequence = -1L ^ (-1L << sequenceBits);

    /*每一部分向左的位移*/
    private final long workerIdShift = sequenceBits; //12
    private final long datacenterIdShift = sequenceBits + workerIdBits;//12 + 5
    private final long timestampShift = sequenceBits + workerIdBits + datacenterIdBits;// 12 + 5 + 5

    @Value("${snowflake.datacenter-id:1}")
    private long datacenterId; // 数据中心ID
    @Value("${snowflake.worker-id:0}")
    private long workerId; // 机器ID

    private long sequence = 0L; // 序列号
    private long lastTimestamp = -1L; // 上一次时间戳

    @PostConstruct
    public void init() {
        String msg;
        if (workerId > maxWorkerId || workerId < 0) {
            log.error("worker Id can't be greater than {} or less than 0", maxWorkerId);
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            log.error("datacenter Id can't be greater than {} or less than 0", maxDatacenterId);
        }
    }

    public String nextIdString() {
        long id = nextId();
        return String.valueOf(id);
    }

    /**
     * 获取下一个ID(该方法是线程安全的)
     * @return
     */
    public synchronized long nextId() {
        // 获取当前时间的时间戳，单位毫秒
        long timestamp = timeGen();
        // 当前时间戳如果小于上次时间戳，则表示时间戳获取出现异常
        if (timestamp < lastTimestamp) {
            throw new HibernateException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        // 获取当前时间戳如果等于上次时间戳（同一毫秒内），则在序列号加一；否则序列号赋值为0，从0开始。
        if (timestamp == lastTimestamp) {
            /*
            * 逻辑：意思是说一个毫秒内最多只能有4096个数字，无论你传递多少进来，
            *      这个位运算保证始终就是在4096这个范围内，避免你自己传递个sequence超过4096这个范围
            * */
            sequence = (sequence + 1) & maxSequence;
            /*逻辑：当某一毫秒的时间产生的id数超过4095， 系统会进入等待，直到下一毫秒， 系统继续产生ID*/
            if (sequence == 0L) {
                timestamp = tilNextMillis();
            }
        } else {
            sequence = 0L;
        }
        // 将上次时间戳值刷新（逻辑：记录一下最近一次生成id的时间戳，单位是毫秒）
        lastTimestamp = timestamp;

        /*
        * 核心逻辑：生成一个64bit的id：
        *         先将当前时间戳左移，放到41 bit那儿
        *         先机房id左移，放到5 bit那儿
        *         先机器id左移，放到5 bit那儿
        *         将序号放最后12 bit
        *         最后拼接起来成一个64 bit的二进制数字，转换成10进制就是个long型
        * */
        return (timestamp - twepoch) << timestampShift
                | datacenterId << datacenterIdShift
                | workerId << workerIdShift
                | sequence;
    }

    private long tilNextMillis() {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获取系统时间戳
     * @return 14位
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

}
