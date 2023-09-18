package com.dyhhd.mdq.simple;

import com.dyhhd.mdq.core.AbstractConsumer;
import com.dyhhd.mdq.core.Consumer;
import com.dyhhd.mdq.core.DelayQueueManage;
import com.dyhhd.mdq.core.DelayQueueManageImpl;
import com.dyhhd.mdq.core.Worker;
import com.dyhhd.mdq.core.DelayQueueProducer;
import com.dyhhd.mdq.core.Producer;

import java.time.LocalDateTime;

/**
 * 队列测试
 *
 * @author lv ning
 */
public class MdqTest {

    public static void main(String[] args) {
        Thread thread = Thread.currentThread();
        String name = "queue-name";

        Producer producer = new DelayQueueProducer();


        Consumer consumer = new AbstractConsumer(producer.getQueue(name)) {
            @Override
            protected void run(Worker worker) {
                System.out.println(" time : " + LocalDateTime.now() + " worker : " + worker);
            }
        };

        producer.addWorker(name, 10000, new MyWorker(18, "张三"));
        producer.addWorker(name, 50000, new MyWorker(28, "李四"));
        producer.addWorker(name, 15000, new MyWorker(38, "王五"));
        producer.addWorker(name, 5000, new MyWorker(48, "赵六"));
        producer.addWorker(name, 5000, "hello");

        DelayQueueManage manage = new DelayQueueManageImpl();
        manage.execute(consumer);

        System.out.println("------------");
//        LockSupport.park(thread);
    }
}
