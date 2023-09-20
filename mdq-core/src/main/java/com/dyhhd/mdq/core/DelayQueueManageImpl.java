package com.dyhhd.mdq.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 延迟队列管理器
 *
 * @author lv ning
 */
public class DelayQueueManageImpl implements DelayQueueManage {

    /**
     * 线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 保存消费者
     */
    private final List<Consumer> consumers = new ArrayList<>();

    public DelayQueueManageImpl() {
    }

    public DelayQueueManageImpl(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public void executes(Collection<Consumer> consumers) {
        if (null != consumers) {
            consumers.forEach(this::execute);
        }
    }

    @Override
    public void execute(Consumer consumer) {
        if (this.consumers.contains(consumer)) {
            return;
        }
        this.threadPoolExecutor.execute(consumer);
        this.consumers.add(consumer);
    }

    /**
     * 停止运行
     */
    @Override
    public void shutdown() {
        this.consumers.forEach(Consumer::stop);
    }

    /**
     * 停止运行
     */
    @Override
    public void activate() {
        this.consumers.forEach(Consumer::start);
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor daemonThreadPoolExecutor) {
        this.threadPoolExecutor = daemonThreadPoolExecutor;
    }
}
