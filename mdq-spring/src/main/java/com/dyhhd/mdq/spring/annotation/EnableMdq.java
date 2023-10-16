package com.dyhhd.mdq.spring.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启 mdq
 *
 * @author lv ning
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ImportMdqRegistrar.class)
public @interface EnableMdq {

    /**
     * 自动 ack
     *
     * @return 自动 ack
     */
    boolean autoAck() default true;

    /**
     * 重试次数
     *
     * @return 次数
     */
    int retryTotal() default -1;
}
