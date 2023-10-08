package com.dyhhd.mdq.ssimple.pring;

import com.dyhhd.mdq.core.Ack;
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
@QueueListener(queues = "queue-5")
public class MdqListener34 {

    public void listener1() {
    }

    @QueueHandler
    public void listener2(String worker, Ack ack) {
        System.out.println("clz" + this.getClass().getName() + " method " + " listener2 " + worker);
        System.out.println("ack " + ack.getRetryCount() + " " + ack);
        ack.confirmSubmit(false);
    }

    @QueueHandler
    public void listener3(WorkerInstance<MyWorker> worker, Ack ack) {
        System.out.println("clz" + this.getClass().getName() + " method " + " listener3 " + worker);
        System.out.println("ack " + ack.getRetryCount() + " " + ack);
        ack.confirmSubmit(false);
    }

    @QueueHandler
    public void listener4(Worker worker) {
        System.out.println("clz" + this.getClass().getName() + " method " + " listener4 " + worker);
    }

    @QueueListener(queues = "queue-5")
    public void listener5(Worker worker, Ack ack) {
        System.out.println("clz" + this.getClass().getName() + " method " + " listener5 " + worker);
        System.out.println("ack " + ack.getRetryCount() + " " + ack);
        ack.confirmSubmit(true);
    }
}
