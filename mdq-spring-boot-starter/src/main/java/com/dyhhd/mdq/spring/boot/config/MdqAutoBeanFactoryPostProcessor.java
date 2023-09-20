package com.dyhhd.mdq.spring.boot.config;

import com.dyhhd.mdq.core.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Map;
import java.util.concurrent.DelayQueue;

/**
 * 消费者 bean factory 处理器
 *
 * @author lv ning
 */
public class MdqAutoBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DelayQueueManage manage = beanFactory.getBean("delayQueueManage", DelayQueueManage.class);
        Map<String, Consumer> beans = beanFactory.getBeansOfType(Consumer.class, false, false);
        for (Consumer c : beans.values()) {
            if (c instanceof AbstractConsumer) {
                AbstractConsumer consumer = (AbstractConsumer) c;
                DelayQueue<Worker> delayQueue = consumer.getDelayQueue();
                if (null != delayQueue) {
                    continue;
                }

                Producer producer = consumer.getProducer();
                if (null != producer) {
                    continue;
                }

                producer = beanFactory.getBean("producer", Producer.class);
                consumer.setProducer(producer);

                consumer.setDelayQueue(consumer.getProducer().getQueue(consumer.getQueue()));
            }

            // 执行自己实现的
            manage.execute(c);
        }
    }
}
