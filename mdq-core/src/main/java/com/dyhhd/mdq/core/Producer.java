package com.dyhhd.mdq.core;

import com.dyhhd.mdq.core.Worker;

import java.util.concurrent.DelayQueue;

/**
 * 生产者
 *
 * @author lv ning
 */
public interface Producer {

    /**
     * 获取延迟队列
     *
     * @param queue 队列名称
     * @return 延迟队列
     */
    DelayQueue<Worker> getQueue(String queue);

    /**
     * 添加任务信息
     *
     * @param queue  队列名称
     * @param worker 队列内容
     */
    void addWorker(String queue, Worker worker);

    /**
     * 添加任务信息
     *
     * @param queue 队列名称
     * @param time  时间
     * @param body  队列内容
     */
    void addWorker(String queue, long time, Object body);
}
