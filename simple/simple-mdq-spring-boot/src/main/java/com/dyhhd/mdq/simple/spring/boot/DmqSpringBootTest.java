package com.dyhhd.mdq.simple.spring.boot;

import com.dyhhd.mdq.core.Producer;
import com.dyhhd.mdq.core.Worker;
import com.dyhhd.mdq.spring.annotation.QueueListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * spring boot test
 *
 * @author lv ning
 */
@Slf4j
@RestController
@SpringBootApplication
public class DmqSpringBootTest {

    @Autowired
    private Producer producer;

    public static void main(String[] args) {
        SpringApplication.run(DmqSpringBootTest.class, args);
        System.out.println("启动成功");
    }

    @GetMapping("/push1")
    public String push1(Long time, String name) {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        int i = current.nextInt(3);
        int queue = current.nextInt(3) + 1;
        if (i % 3 == 0) {
            producer.addWorker("queue" + queue, time, name);
        } else if (i % 3 == 1) {
            producer.addWorker("queue" + queue, time, new MyInfo1(name));
        } else {
            WorkerInstance<MyInfo1> instance = new WorkerInstance<>(time);
            instance.setBody(new MyInfo1(name));
            System.out.println("worker instance");
            producer.addWorker("queue" + queue, instance);
        }
        return "success";
    }

    @GetMapping("/push2")
    public String push2(Long time, Integer age, String name, String email) {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        int i = current.nextInt(2);
        int queue = current.nextInt(3) + 1;
        MyInfo2 info2 = new MyInfo2(age, name, email);
        if (i % 2 == 0) {
            producer.addWorker("queue" + queue, time, info2.toString());
        } else {
            producer.addWorker("queue" + queue, time, info2);
        }

        return "success";
    }

    @GetMapping("/push3")
    public String push3(Long time, String name) {
        producer.addWorker("queue", time, name);
        return "success";
    }

    @QueueListener(queues = {"queue1", "queue2", "queue3"})
    public void listener1(Worker worker) {
        System.out.println("listener1 worker " + worker);
        log.info("worker : {}", worker);
    }

    @QueueListener(queues = {"queue1", "queue2", "queue3"})
    public void listener2(String worker) {
        System.out.println("listener2 worker " + worker);
        log.info("worker : {}", worker);
    }

    @QueueListener(queues = {"queue1", "queue2", "queue3"})
    public void listener3(MyInfo1 worker) {
        System.out.println("listener3 worker " + worker);
        log.info("worker : {}", worker);
    }

    @QueueListener(queues = {"queue1", "queue2", "queue3"})
    public void listener4(MyInfo2 worker) {
        System.out.println("listener4 worker " + worker);
        log.info("worker : {}", worker);
    }

    @QueueListener(queues = {"queue1", "queue2", "queue3"})
    public void listener5(WorkerInstance<MyInfo1> worker) {
        System.out.println("listener5 worker " + worker);
        log.info("worker : {}", worker);
    }

    @QueueListener(queues = {"queue1", "queue2", "queue3"})
    public void listener6(WorkerInstance<MyInfo2> worker) {
        System.out.println("listener6 worker " + worker);
        log.info("worker : {}", worker);
    }

    @QueueListener(queues = {"queue1", "queue2", "queue3"})
    public void listener7(WorkerInstance<Map<String, Object>> worker) {
        System.out.println("listener7 worker " + worker);
        log.info("worker : {}", worker);
    }

    @QueueListener(queues = {"queue1", "queue2", "queue3"})
    public void listener8(Map<String, Object> worker) {
        System.out.println("listener8 worker " + worker);
        log.info("worker : {}", worker);
    }
}
