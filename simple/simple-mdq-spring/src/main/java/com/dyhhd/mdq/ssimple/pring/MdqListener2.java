package com.dyhhd.mdq.ssimple.pring;

import com.dyhhd.mdq.core.Worker;
import com.dyhhd.mdq.spring.annotation.QueueHandler;
import com.dyhhd.mdq.spring.annotation.QueueListener;
import org.springframework.stereotype.Component;

/**
 * 监听器
 *
 * @author lv ning
 */
@Component
public class MdqListener2 {

    public void listener1() {
    }

    @QueueHandler
    public void listener2() {

    }

    @QueueListener(queues = "queue-3")
    public void listener3(Worker worker) {
        System.out.println("clz" + this.getClass().getName() + " method " + " listener3 " + worker);
    }

    @QueueListener(queues = "queue-4")
    @QueueHandler
    public void listener4(MyWorker worker) {
        System.out.println("clz" + this.getClass().getName() + " method " + " listener4 " + worker);
    }

    @QueueListener(queues = "queue-4")
    public void listener5(MyWorker worker) {
        System.out.println("clz" + this.getClass().getName() + " method " + " listener5 " + worker);
    }
}
