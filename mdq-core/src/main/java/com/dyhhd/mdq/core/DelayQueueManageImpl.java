package com.dyhhd.mdq.core;

import com.dyhhd.mdq.thread.ThreadFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 延迟队列管理器
 *
 * @author lv ning
 */
public class DelayQueueManageImpl implements DelayQueueManage {

    /**
     * 线程池
     */
    private ThreadFactory threadFactory;

    /**
     * 保存消费者
     */
    private final List<Consumer> consumers = new ArrayList<>();

    /**
     * 保存线程信息
     */
    private final List<Thread> threads = new ArrayList<>();

    public DelayQueueManageImpl() {
    }

    public DelayQueueManageImpl(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
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

        Thread thread = this.threadFactory.newThread(consumer);
        thread.start();

        this.consumers.add(consumer);
        this.threads.add(thread);
    }

    /**
     * 停止运行
     */
    @Override
    public void shutdown() {
        // 这个会让线程停止运行
        this.consumers.forEach(Consumer::stop);
//        this.threads.forEach(Thread::stop);
    }

    /**
     * 启动
     */
    @Override
    public void activate() {
        this.consumers.forEach(Consumer::start);
        this.threads.forEach(Thread::start);
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }
}
