package com.dyhhd.mdq.core;

/**
 * ack
 *
 * @author lv ning
 */
public class AckImpl implements Ack {

    private int count = 1;

    private int total;

    private final AckCallback ackCallback;

    private final Worker worker;

    public AckImpl(Worker worker, AckCallback ackCallback, int total) {
        this.worker = worker;
        this.ackCallback = ackCallback;
        this.total = total;
    }

    @Override
    public int getRetryCount() {
        return count;
    }

    public void setRetryTotal(int total) {
        this.total = total;
    }

    @Override
    public void confirmSubmit(boolean submit) {
        if (submit) {
            // 提交
            ackCallback.commit(worker);
        } else {
            if (count++ >= total) {
                // 异常
                ackCallback.abnormal(worker, count);
            } else {
                // 继续提交
                ackCallback.retry(worker, this);
            }
        }
    }
}
