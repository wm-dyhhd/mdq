package com.dyhhd.mdq.simple;

import com.dyhhd.mdq.core.*;
import com.dyhhd.mdq.thread.ThreadFactory;

import java.time.LocalDateTime;

/**
 * 队列 ack 测试
 *
 * @author lv ning
 */
public class MdqAckTest {

    public static void main(String[] args) throws Exception {
        Thread thread = Thread.currentThread();
        String name = "queue-name";

        Producer producer = new DelayQueueProducer();

        AbstractAckConsumer consumer = new AbstractAckConsumer(producer.getQueue(name)) {
            @Override
            protected void run(Worker worker) {
                if (worker instanceof MyAckWorker) {
                    MyAckWorker ackWorker = (MyAckWorker) worker;
                    System.out.println("ackWorker " + ackWorker);
                    int i = 1 / 0;
                }
                System.out.println(" time : " + LocalDateTime.now() + " worker : " + worker);
            }
        };
        consumer.setRetryTotal(3);

        producer.addWorker(name, 10000, new MyWorker(18, "张三"));
        producer.addWorker(name, 50000, new MyWorker(28, "李四"));
        producer.addWorker(name, 15000, new MyWorker(38, "王五"));
        producer.addWorker(name, 5000, new MyWorker(48, "赵六"));
        producer.addWorker(name, 5000, "hello");
        producer.addWorker(name, new MyAckWorker(500));
        producer.addWorker(name, new MyAckWorker(1000));

        DelayQueueManage manage = new DelayQueueManageImpl(ThreadFactory.defaultThreadFactory());
        manage.execute(consumer);

        Thread.sleep(1000);
//        manage.shutdown();
        manage.showdownNow();

        Thread.sleep(1000);
        manage.activate();

//        Thread.sleep(100000);

        Thread.sleep(1000);
        manage.shutdown();
//        manage.showdownNow();

        Thread.sleep(1000);
        manage.activate();

        System.out.println("------------");
//        LockSupport.park(thread);
    }
}
