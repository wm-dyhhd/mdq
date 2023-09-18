package com.dyhhd.mdq.spring.annotation;

import java.lang.annotation.*;

/**
 * 队列监听器
 *
 * @author lv ning
 */
@Target(value = {ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface QueueListener {

    /**
     * 队列名称
     *
     * @return 队列名称
     */
    String[] queues();
}
