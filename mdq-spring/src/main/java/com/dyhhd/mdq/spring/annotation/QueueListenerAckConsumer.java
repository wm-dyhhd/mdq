package com.dyhhd.mdq.spring.annotation;

import com.dyhhd.mdq.core.AbstractAckConsumer;
import com.dyhhd.mdq.core.Ack;
import com.dyhhd.mdq.core.Producer;
import com.dyhhd.mdq.core.Worker;

/**
 * 队列监听器 ack 消费者
 *
 * @author lv ning
 */
public class QueueListenerAckConsumer extends AbstractAckConsumer {

    private final QueueMetadata metadata;

    public QueueListenerAckConsumer(Producer producer,
                                    String queue,
                                    QueueMetadata metadata) {
        super(producer, queue);
        this.metadata = metadata;
    }

    @Override
    protected void ackRun(Worker worker, Ack ack) {
        if (metadata.ackIndex > -1) {
            metadata.inject(worker, ack);
        } else {
            super.ackRun(worker, ack);
        }
    }

    @Override
    protected void run(Worker worker) {

        metadata.inject(worker);
    }
}
