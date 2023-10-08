package com.dyhhd.mdq.simple.spring.boot;

import com.dyhhd.mdq.core.AbstractAckConsumer;
import com.dyhhd.mdq.core.Worker;
import org.springframework.stereotype.Component;

/**
 * 运行测试
 *
 * @author lv ning
 */
@Component
public class ConsumerAckTest extends AbstractAckConsumer {

    public ConsumerAckTest() {
        super("queue");
    }

    @Override
    protected void run(Worker worker) {
        System.out.println("ConsumerAckTest " + worker);
    }
}
