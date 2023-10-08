package com.dyhhd.mdq.spring.annotation;

import com.dyhhd.mdq.core.Ack;
import com.dyhhd.mdq.core.Worker;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 队列元数据信息
 *
 * @author lv ning
 */
public class QueueMetadata {

    final String queue;

    final Object bean;

    final Class<?> clz;

    final Method method;

    final Class<?>[] parameterTypes;

    // ack index 不存在为 -1
    final int ackIndex;

    // worker index 不存在为 -1
    int workerIndex = -1;

    // 其它数据
    QueueOtherType otherType = null;

    // 是否执行
    boolean process;

    public QueueMetadata(String queue, Object bean, Class<?> clz, Method method, Class<?>[] parameterTypes) {
        this.queue = queue;
        this.bean = bean;
        this.clz = clz;
        this.method = method;
        this.parameterTypes = parameterTypes;
        this.ackIndex = Arrays.asList(parameterTypes).indexOf(Ack.class);
        builderProcess();
    }

    private void builderProcess() {
        Class<?>[] parameterTypes = this.parameterTypes;
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType == Worker.class) {
                this.workerIndex = i;
                break;
            } else if (Worker.class.isAssignableFrom(parameterType)) {
                this.workerIndex = i;
                break;
            }
        }

        this.process = this.workerIndex > -1;

        // workerIndex == -1
        if (!this.process) {
            this.otherType = QueueOtherMetadata.match(parameterTypes);
            this.process = this.otherType != null;
        }
    }

    public Object[] injectArgs(Worker worker) {
        this.process();

        Object[] args = new Object[this.parameterTypes.length];
        if (this.workerIndex > -1) {
            args[this.workerIndex] = worker;
        }

        if (this.otherType != null) {
            int index = this.otherType.getIndex();
            QueueOtherMetadata metadata = this.otherType.getMetadata();
            args[index] = metadata.get(worker);
        }

        return args;
    }

    public Object[] injectArgs(Worker worker, Ack ack) {
        this.process();

        Object[] args = this.injectArgs(worker);

        if (this.ackIndex > -1) {
            args[this.ackIndex] = ack;
        }

        return args;
    }

    public void inject(Worker worker) {
        this.process();

        Object[] args = this.injectArgs(worker);

        this.inject(args);
    }

    public void inject(Worker worker, Ack ack) {
        this.process();

        Object[] args = this.injectArgs(worker, ack);

        this.inject(args);
    }

    private void inject(Object[] args) {
        Method method = this.method;

        try {
            // 执行方法
            method.setAccessible(true);
            method.invoke(this.bean, args);
        } catch (ReflectiveOperationException e) {
//                throw new RuntimeException(e);
        }
    }

    private void process() {
        if (!process) {
            throw new IllegalArgumentException("非法参数异常，不具备注入条件");
        }
    }

    @Override
    public String toString() {
        return "QueueMetadata{" +
                "queue='" + queue + '\'' +
                ", bean=" + bean +
                ", clz=" + clz +
                ", method=" + method +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", ackIndex=" + ackIndex +
                ", workerIndex=" + workerIndex +
                ", otherType=" + otherType +
                ", process=" + process +
                '}';
    }
}
