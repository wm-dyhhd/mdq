package com.dyhhd.mdq.core;

import java.util.Collection;

/**
 * 延迟队列管理器
 *
 * @author lv ning
 */
public interface DelayQueueManage {

    /**
     * 执行队列
     *
     * @param consumers 队列信息
     */
    void executes(Collection<Consumer> consumers);

    /**
     * 执行队列
     *
     * @param consumer 队列信息
     */
    void execute(Consumer consumer);

    /**
     * 停止运行
     */
    void shutdown();

    /**
     * 启动
     */
    void activate();

    /**
     * 立马停止
     */
    @Deprecated
    void showdownNow();
}
