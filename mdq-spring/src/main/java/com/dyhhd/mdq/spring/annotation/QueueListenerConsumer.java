package com.dyhhd.mdq.spring.annotation;

import com.dyhhd.mdq.core.AbstractConsumer;
import com.dyhhd.mdq.core.Producer;
import com.dyhhd.mdq.core.Worker;

/**
 * 队列监听器 消费者
 *
 * @author lv ning
 */
public class QueueListenerConsumer extends AbstractConsumer {

    private final QueueMetadata metadata;

    public QueueListenerConsumer(Producer producer,
                                 String queue,
                                 QueueMetadata metadata) {
        super(producer, queue);
        this.metadata = metadata;
    }

    @Override
    protected void run(Worker worker) {
        // 可以进入的都是已经过滤了的
        metadata.inject(worker);
    }
}
