package com.dyhhd.mdq.core;

/**
 * 消费者
 *
 * @author lv ning
 */
public interface Consumer extends Runnable {

    /**
     * 获取队列名称
     *
     * @return 队列名称
     */
    String getQueue();

    /**
     * 是否在运行
     */
    boolean isRun();

    /**
     * 启动
     */
    void start();

    /**
     * 停止
     */
    void stop();
}
