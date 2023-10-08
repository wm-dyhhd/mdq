package com.dyhhd.mdq.core;

import com.dyhhd.mdq.log.Log;
import com.dyhhd.mdq.log.LogFactory;

import java.util.concurrent.DelayQueue;

/**
 * 消费者
 *
 * @author lv ning
 */
public abstract class AbstractAckConsumer extends AbstractConsumer implements AckCallback {

    protected static Log log = LogFactory.getLog(AbstractAckConsumer.class);

    private AckFailCallback ackFailCallback;

    private int retryTotal;

    public AbstractAckConsumer(String queue) {
        super(queue);
    }

    public AbstractAckConsumer(Producer producer, String queue) {
        super(producer, queue);
    }

    public AbstractAckConsumer(DelayQueue<Worker> delayQueue) {
        super(delayQueue);
    }

    @Override
    public void run() {
        if (log.isDebugEnabled()) {
            log.debug("begin run consumer...");
        }

        Ack ack;
        while (isRun()) {
            try {
                Worker worker = delayQueue.take();
                if (log.isDebugEnabled()) {
                    log.debug("worker info :" + worker);
                }

                // ack
                ack = new AckImpl(worker, this, retryTotal);

                ackRun(worker, ack);
            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
                log.error("worker error ", e);
            }
        }
    }

    protected void ackRun(Worker worker, Ack ack) {
        try {
            run(worker);
            ack.confirmSubmit(true);
        } catch (Throwable e) {
            ack.confirmSubmit(false);
        }
    }

    @Override
    protected void run(Worker worker) {
    }

    /**
     * 提交
     */
    @Override
    public void commit(Worker worker) {
        // empty
    }

    /**
     * 重试
     */
    @Override
    public void retry(Worker worker, Ack ack) {
        ackRun(worker, ack);
    }

    /**
     * 异常
     */
    @Override
    public void abnormal(Worker worker, int retry) {
        if (null != ackFailCallback) {
            ackFailCallback.callback(getQueue(), worker, retry);
        }
    }

    public void setAckFailCallback(AckFailCallback ackFailCallback) {
        this.ackFailCallback = ackFailCallback;
    }

    public void setRetryTotal(int retryTotal) {
        this.retryTotal = retryTotal;
    }
}
