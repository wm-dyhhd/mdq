package com.dyhhd.mdq.spring.boot.config;

import com.dyhhd.mdq.core.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 消费者 bean factory 处理器
 *
 * @author lv ning
 */
public class MdqAutoBeanFactoryPostProcessor implements SmartInitializingSingleton, BeanFactoryPostProcessor {

    private ConfigurableListableBeanFactory beanFactory;

    private final int retryTotal;

    public MdqAutoBeanFactoryPostProcessor() {
        this(0);
    }

    public MdqAutoBeanFactoryPostProcessor(int retryTotal) {
        this.retryTotal = retryTotal;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;

    }

    @Override
    public void afterSingletonsInstantiated() {
        ConfigurableListableBeanFactory beanFactory = this.beanFactory;
        Assert.state(this.beanFactory != null, "No ConfigurableListableBeanFactory set");

        DelayQueueManage manage = beanFactory.getBean("delayQueueManage", DelayQueueManage.class);
//        Assert.state(manage != null, "DelayQueueManage not initialized");

        Producer producer = beanFactory.getBean("producer", Producer.class);
//        Assert.state(producer != null, "Producer not initialized");

        Map<String, Consumer> beans = beanFactory.getBeansOfType(Consumer.class, false, false);
        List<Consumer> consumers = new ArrayList<>(beans.values());
        AnnotationAwareOrderComparator.sort(consumers);
//        Assert.state(consumers != null, "Consumer List not initialized");

        AckFailCallback ackFailCallback = null;
        try {
            ackFailCallback = beanFactory.getBean("ackFailCallback", AckFailCallback.class);
        } catch (BeansException e) {
//            throw new RuntimeException(e);
        }

        for (Consumer c : consumers) {
            if (c instanceof AbstractConsumer) {
                AbstractConsumer consumer = (AbstractConsumer) c;

                if (consumer instanceof AbstractAckConsumer) {
                    AbstractAckConsumer ackConsumer = (AbstractAckConsumer) consumer;
                    ackConsumer.setAckFailCallback(ackFailCallback);
                    ackConsumer.setRetryTotal(retryTotal);
                }

                if (null != consumer.getDelayQueue()) {
                    continue;
                }

                if (null != consumer.getProducer()) {
                    continue;
                }

                // 设置消费者
                consumer.setProducer(producer);

                consumer.setDelayQueue(consumer.getProducer().getQueue(consumer.getQueue()));
            }

            // 执行自己实现的
            manage.execute(c);
        }
    }
}
