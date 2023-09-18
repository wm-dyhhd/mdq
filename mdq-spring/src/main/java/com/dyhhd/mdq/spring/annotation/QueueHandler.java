package com.dyhhd.mdq.spring.annotation;

import java.lang.annotation.*;

/**
 * 队列操作
 * 该注解需要配置 {@link QueueListener} 使用
 *
 * @author lv ning
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface QueueHandler {
}
