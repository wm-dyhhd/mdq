package com.dyhhd.mdq.core;

/**
 * ack 异常 回调
 *
 * @author lv ning
 */
@FunctionalInterface
public interface AckFailCallback {

    /**
     * 异常回调
     *
     * @param queue  队列名称
     * @param worker worker
     * @param retry  重试次数
     */
    void callback(String queue, Worker worker, int retry);
}
