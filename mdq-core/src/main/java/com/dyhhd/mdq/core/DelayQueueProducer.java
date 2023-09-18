package com.dyhhd.mdq.core;

import com.dyhhd.mdq.log.Log;
import com.dyhhd.mdq.log.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * 生产者
 *
 * @author lv ning
 */
public class DelayQueueProducer implements Producer {

    private static final Log log = LogFactory.getLog(DelayQueueProducer.class);

    protected volatile Map<String, DelayQueue<Worker>> mdq = new ConcurrentHashMap<>();

    private static final Object LOCK = new Object();

    /**
     * 获取延迟队列
     *
     * @param queue 队列名称
     * @return 延迟队列
     */
    @Override
    public DelayQueue<Worker> getQueue(String queue) {
        return get(queue);
    }

    /**
     * 添加任务信息
     *
     * @param queue  队列名称
     * @param worker 队列内容
     */
    @Override
    public void addWorker(String queue, Worker worker) {
        DelayQueue<Worker> dqueue = get(queue);
        dqueue.put(worker);
        if (log.isDebugEnabled()) {
            log.debug("queue " + queue + " worker " + worker);
        }
    }

    /**
     * 添加任务信息
     *
     * @param queue 队列名称
     * @param time  时间
     * @param body  队列内容
     */
    @Override
    public void addWorker(String queue, long time, Object body) {
        WorkerInstance<Object> worker = new WorkerInstance<>(time);
        worker.setBody(body);

        addWorker(queue, worker);
    }

    /**
     * 获取延迟队列
     *
     * @param queue 队列名称
     * @return 延迟队列
     */
    protected DelayQueue<Worker> get(String queue) {
        DelayQueue<Worker> dqueue = mdq.get(queue);

        if (null == dqueue) {
            synchronized (LOCK) {
                dqueue = mdq.get(queue);
                if (null == dqueue) {
                    dqueue = new DelayQueue<>();
                    mdq.put(queue, dqueue);
                }
            }
        }

        return dqueue;
    }
}
