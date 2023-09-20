package com.dyhhd.mdq.core;

import java.util.Collection;
import java.util.List;

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
}
