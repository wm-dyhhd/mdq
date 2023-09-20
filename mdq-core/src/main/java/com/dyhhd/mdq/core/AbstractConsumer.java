package com.dyhhd.mdq.core;

import com.dyhhd.mdq.log.Log;
import com.dyhhd.mdq.log.LogFactory;

import java.util.concurrent.DelayQueue;

/**
 * 消费者
 *
 * @author lv ning
 */
public abstract class AbstractConsumer implements Consumer {

    protected static Log log = LogFactory.getLog(AbstractConsumer.class);

    /**
     * 是否再运行
     */
    private volatile boolean run = true;

    protected DelayQueue<Worker> delayQueue;

    protected Producer producer;

    private String queue;

    public AbstractConsumer(String queue) {
        this.queue = queue;
    }

    public AbstractConsumer(Producer producer, String queue) {
        this(producer.getQueue(queue));
        this.queue = queue;
        this.producer = producer;
    }

    public AbstractConsumer(DelayQueue<Worker> delayQueue) {
        this.delayQueue = delayQueue;
    }

    @Override
    public void run() {
        if (log.isDebugEnabled()) {
            log.debug("begin run consumer...");
        }
        while (run) {
            try {
                Worker worker = delayQueue.take();
                if (log.isDebugEnabled()) {
                    log.debug("worker info :" + worker);
                }
                run(worker);
            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
                log.error("worker error ", e);
            }
        }
    }

    protected abstract void run(Worker worker);

    @Override
    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public DelayQueue<Worker> getDelayQueue() {
        return delayQueue;
    }

    public void setDelayQueue(DelayQueue<Worker> delayQueue) {
        this.delayQueue = delayQueue;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    @Override
    public boolean isRun() {
        return run;
    }

    @Override
    public void start() {
        if (!isRun()) {
            if (log.isDebugEnabled()) {
                log.debug("start run consumer");
            }
            // 不在运行
            run = true;
        }
    }

    @Override
    public void stop() {
        if (isRun()) {
            if (log.isDebugEnabled()) {
                log.debug("stop run consumer");
            }
            // 在运行
            run = false;
        }
    }
}
