package com.dyhhd.mdq.core;

/**
 * ack
 *
 * @author lv ning
 */
public interface Ack {

    /**
     * 获取重试次数
     * 1 ++
     *
     * @return 次数
     */
    int getRetryCount();

    /**
     * 是否确认提交
     *
     * @param submit 提交
     */
    void confirmSubmit(boolean submit);
}
