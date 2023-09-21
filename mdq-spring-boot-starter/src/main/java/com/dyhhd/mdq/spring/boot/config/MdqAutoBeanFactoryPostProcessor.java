package com.dyhhd.mdq.spring.boot.config;

import com.dyhhd.mdq.core.AbstractConsumer;
import com.dyhhd.mdq.core.Consumer;
import com.dyhhd.mdq.core.DelayQueueManage;
import com.dyhhd.mdq.core.Producer;
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

    private DelayQueueManage manage;

    private Producer producer;

    private List<Consumer> consumers;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;

        this.manage = beanFactory.getBean("delayQueueManage", DelayQueueManage.class);

        this.producer = beanFactory.getBean("producer", Producer.class);

        Map<String, Consumer> beans = beanFactory.getBeansOfType(Consumer.class, false, false);
        List<Consumer> consumers = new ArrayList<>(beans.values());
        AnnotationAwareOrderComparator.sort(consumers);
        this.consumers = consumers;
    }


    @Override
    public void afterSingletonsInstantiated() {
//        ConfigurableListableBeanFactory beanFactory = this.beanFactory;
//        Assert.state(this.beanFactory != null, "No ConfigurableListableBeanFactory set");

        DelayQueueManage manage = this.manage;
        Assert.state(this.manage != null, "DelayQueueManage not initialized");

        Producer producer = this.producer;
        Assert.state(this.producer != null, "Producer not initialized");

        List<Consumer> consumers = this.consumers;
        Assert.state(this.consumers != null, "Consumer List not initialized");

        for (Consumer c : consumers) {
            if (c instanceof AbstractConsumer) {
                AbstractConsumer consumer = (AbstractConsumer) c;
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
