package com.dyhhd.mdq.spring.boot.config;

import com.dyhhd.mdq.core.*;
import com.dyhhd.mdq.spring.annotation.MdqAutoBeanFactoryPostProcessor;
import com.dyhhd.mdq.thread.ThreadFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mdq 配置
 *
 * @author lv ning
 */
@Configuration
@EnableConfigurationProperties({MdqProperties.class})
public class MdqAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DelayQueueManage delayQueueManage(ThreadFactory threadFactory) {
        return new DelayQueueManageImpl(threadFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public Producer producer() {
        return new DelayQueueProducer();
    }

    @Bean
    @ConditionalOnMissingBean
    public AckFailCallback ackFailCallback() {
        return new LogAckFailCallback();
    }

    @Bean
    @ConditionalOnMissingBean
    public ThreadFactory threadFactory() {
        return ThreadFactory.defaultThreadFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public MdqAutoBeanFactoryPostProcessor mdqAutoBeanFactoryPostProcessor(MdqProperties properties) {
        MdqAutoBeanFactoryPostProcessor processor = new MdqAutoBeanFactoryPostProcessor();
        processor.setAutoAck(properties.getAck().isAutoAck());
        processor.setRetryTotal(properties.getAck().getRetryTotal());
        return processor;
    }
}
