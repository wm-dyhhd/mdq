package com.dyhhd.mdq.spring.boot.config;

import com.dyhhd.mdq.core.DelayQueueManage;
import com.dyhhd.mdq.core.DelayQueueManageImpl;
import com.dyhhd.mdq.core.DelayQueueProducer;
import com.dyhhd.mdq.core.Producer;
import com.dyhhd.mdq.spring.annotation.EnableMdq;
import com.dyhhd.mdq.thread.ThreadFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * mdq 配置
 *
 * @author lv ning
 */
@EnableMdq
@Configuration
public class MdqAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DelayQueueManage delayQueueManage(ThreadPoolExecutor threadPoolExecutor) {
        return new DelayQueueManageImpl(threadPoolExecutor);
    }

    @Bean
    @ConditionalOnMissingBean
    public Producer producer() {
        return new DelayQueueProducer();
    }

    @Bean
    @ConditionalOnMissingBean
    public ThreadPoolExecutor daemonThreadPoolExecutor() {
        int cpu = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(cpu + 1,
                50,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                ThreadFactory.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @Bean
    MdqAutoBeanFactoryPostProcessor mdqAutoBeanFactoryPostProcessor() {
        return new MdqAutoBeanFactoryPostProcessor();
    }
}
