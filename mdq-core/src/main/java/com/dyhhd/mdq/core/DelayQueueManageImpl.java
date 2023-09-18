package com.dyhhd.mdq.core;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 延迟队列管理器
 *
 * @author lv ning
 */
public class DelayQueueManageImpl implements DelayQueueManage {

    private ThreadPoolExecutor threadPoolExecutor;

    public DelayQueueManageImpl() {
        this(null);
    }

    public DelayQueueManageImpl(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public void executes(List<Consumer> consumers) {
        if (null != consumers) {
            consumers.forEach(this::execute);
        }
    }

    @Override
    public void execute(Consumer consumer) {
        this.threadPoolExecutor.submit(consumer);
    }

    /**
     * 停止运行
     */
    @Override
    public void shutdown() {
        this.threadPoolExecutor.shutdown();
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor daemonThreadPoolExecutor) {
        this.threadPoolExecutor = daemonThreadPoolExecutor;
    }
}
