package com.dyhhd.mdq.spring.annotation;

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

    public QueueMetadata(String queue, Object bean, Class<?> clz, Method method, Class<?>[] parameterTypes) {
        this.queue = queue;
        this.bean = bean;
        this.clz = clz;
        this.method = method;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public String toString() {
        return "QueueMetadata{" +
                "queue='" + queue + '\'' +
                ", bean=" + bean +
                ", clz=" + clz +
                ", method=" + method +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                '}';
    }
}
