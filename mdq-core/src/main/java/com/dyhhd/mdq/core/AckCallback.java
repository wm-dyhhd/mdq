package com.dyhhd.mdq.core;

/**
 * ack 回调
 *
 * @author lv ning
 */
public interface AckCallback {

    /**
     * 提交
     */
    void commit(Worker worker);

    /**
     * 重试
     */
    void retry(Worker worker, Ack ack);

    /**
     * 异常
     */
    void abnormal(Worker worker, int retry);
}
