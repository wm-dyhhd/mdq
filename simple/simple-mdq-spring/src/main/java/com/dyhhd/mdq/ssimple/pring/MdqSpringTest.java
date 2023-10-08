package com.dyhhd.mdq.ssimple.pring;

import com.dyhhd.mdq.core.Producer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * mdq spring 测试
 *
 * @author lv ning
 */
public class MdqSpringTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MdqConfig.class);
        Producer producer = context.getBean(Producer.class);

        producer.addWorker("queue-1", 1000, new MyWorker(18, "张三"));
        producer.addWorker("queue-1", 1500, new MyWorker(28, "李四"));
        producer.addWorker("queue-1", 2000, new MyWorker(38, "王五"));
        producer.addWorker("queue-1", 2500, new MyWorker(48, "赵六"));

        producer.addWorker("queue-2", 3000, "zhangsan");
        producer.addWorker("queue-2", 3500, "lisi");
        producer.addWorker("queue-2", 4000, "wnagwu");

        producer.addWorker("queue-3", 4500, new MyWorker(222, "张三"));
        producer.addWorker("queue-3", 5000, new MyWorker(333, "李四"));
        producer.addWorker("queue-3", 5500, "wnagwu");

        producer.addWorker("queue-4", 6000, "zhangsan");
        producer.addWorker("queue-4", 6500, new MyWorker(10000, "王五"));
        producer.addWorker("queue-4", 7000, "wnagwu");

        MyAckWorker myAckWorker1 = new MyAckWorker(6000);
        myAckWorker1.setName("a1");
        producer.addWorker("queue-5", myAckWorker1);
        MyAckWorker myAckWorker2 = new MyAckWorker(6500);
        myAckWorker2.setName("a2");
        producer.addWorker("queue-5", myAckWorker2);
        MyAckWorker myAckWorker3 = new MyAckWorker(7000);
        myAckWorker3.setName("a3");
        producer.addWorker("queue-5", myAckWorker3);

        Thread mainThread = Thread.currentThread();
//        LockSupport.park(mainThread);
    }
}
