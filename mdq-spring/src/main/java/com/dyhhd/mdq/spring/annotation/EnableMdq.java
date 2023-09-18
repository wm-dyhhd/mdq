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
}
