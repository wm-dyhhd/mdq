package com.dyhhd.mdq.core;

import com.dyhhd.mdq.thread.ThreadFactory;

import java.util.*;

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
     * 保存消费者线程信息
     */
    private final List<ThreadConsumer> threadConsumers = new ArrayList<>();

    /**
     * 保存所有的消费者信息
     */
    private final Set<Consumer> consumers = new HashSet<>();

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
        this.consumers.add(consumer);

        Thread thread = this.threadFactory.newThread(consumer);
        thread.start();

        threadConsumers.add(new ThreadConsumer(consumer, thread));
    }

    /**
     * 停止运行
     */
    @Override
    public void shutdown() {
        // 这个会让线程停止运行
        for (ThreadConsumer threadConsumer : this.threadConsumers) {
            threadConsumer.consumer.stop();
        }
    }

    /**
     * 启动
     */
    @Override
    public void activate() {
        for (ThreadConsumer threadConsumer : this.threadConsumers) {
            threadConsumer.consumer.start();
            Thread thread = threadConsumer.thread;
            if (null == thread || !thread.isAlive()) {
                thread = this.threadFactory.newThread(threadConsumer.consumer);
                thread.start();

                threadConsumer.thread = thread;
            }
        }
    }

    /**
     * 立马停止
     */
    @Deprecated
    @Override
    public void showdownNow() {
        // 这个会让线程停止运行
        for (ThreadConsumer threadConsumer : this.threadConsumers) {
            threadConsumer.consumer.stop();
            Thread thread = threadConsumer.thread;
            if (null != thread && thread.isAlive()) {
                thread.interrupt();
            }
        }
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    private static class ThreadConsumer {

        final Consumer consumer;

        Thread thread;

        public ThreadConsumer(Consumer consumer) {
            this.consumer = consumer;
        }

        public ThreadConsumer(Consumer consumer, Thread thread) {
            this.consumer = consumer;
            this.thread = thread;
        }
    }
}
