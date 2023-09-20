package com.dyhhd.mdq.simple.spring.boot;

import com.dyhhd.mdq.core.AbstractConsumer;
import com.dyhhd.mdq.core.Worker;
import org.springframework.stereotype.Component;

/**
 * 运行测试
 *
 * @author lv ning
 */
@Component
public class ConsumerTest extends AbstractConsumer {

    public ConsumerTest() {
        super("queue");
    }

    @Override
    protected void run(Worker worker) {
        System.out.println("ConsumerTest " + worker);
    }
}
