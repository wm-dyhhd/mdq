package com.dyhhd.mdq.spring.annotation;

import com.dyhhd.mdq.core.*;
import com.dyhhd.mdq.log.Log;
import com.dyhhd.mdq.log.LogFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mdq 的处理器
 *
 * @author lv ning
 */
public class MdqAutoBeanFactoryPostProcessor implements SmartInitializingSingleton, BeanFactoryPostProcessor, BeanPostProcessor {

    public static final String BEAN_NAME = MdqAutoBeanFactoryPostProcessor.class.getName();

    protected final Log logger = LogFactory.getLog(getClass());

    private ConfigurableListableBeanFactory beanFactory;

    private final Map<String, Set<QueueMetadata>> metadataMap = new ConcurrentHashMap<>();

    private boolean autoAck;

    private int retryTotal;

    private DelayQueueManage delayQueueManage;
    private Producer producer;
    private List<Consumer> consumers;
    private AckFailCallback ackFailCallback;

    public MdqAutoBeanFactoryPostProcessor() {
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        cacheMetadata(bean, targetClass);
        return bean;
    }

    @Override
    public void afterSingletonsInstantiated() {
        ConfigurableListableBeanFactory beanFactory = this.beanFactory;
        Assert.state(this.beanFactory != null, "No ConfigurableListableBeanFactory set");

        Map<String, DelayQueueManage> manageMap = beanFactory.getBeansOfType(DelayQueueManage.class, false, false);
        List<DelayQueueManage> manages = new ArrayList<>(manageMap.values());
        AnnotationAwareOrderComparator.sort(manages);
        if (manages.isEmpty()) {
            logger.warn("dmq manage not exists");
            throw new IllegalStateException("manage not instantiate");
        } else {
            if (manages.size() > 1) {
                logger.warn("dmq manage exists " + manages.size() + " multiple ");
            }
            this.delayQueueManage = manages.get(0);
        }

        Map<String, Producer> producerMap = beanFactory.getBeansOfType(Producer.class, false, false);
        List<Producer> producers = new ArrayList<>(producerMap.values());
        AnnotationAwareOrderComparator.sort(producers);
        if (producers.isEmpty()) {
            logger.warn("dmq producer not exists");
            throw new IllegalStateException("producer not instantiate");
        } else {
            if (producers.size() > 1) {
                logger.warn("dmq producer exists " + producers.size() + " multiple ");
            }
            this.producer = producers.get(0);
        }

        Map<String, Consumer> beans = beanFactory.getBeansOfType(Consumer.class, false, false);
        List<Consumer> consumers = new ArrayList<>(beans.values());
        AnnotationAwareOrderComparator.sort(consumers);
        this.consumers = consumers;

        Map<String, AckFailCallback> ackFailCallbackMap = beanFactory.getBeansOfType(AckFailCallback.class, false, false);
        List<AckFailCallback> ackFailCallbacks = new ArrayList<>(ackFailCallbackMap.values());
        AnnotationAwareOrderComparator.sort(ackFailCallbacks);
        if (ackFailCallbacks.isEmpty()) {
            logger.debug("dmq ack fail callback not exists");
        } else {
            if (ackFailCallbacks.size() > 1) {
                logger.warn("dmq ack fail callback exists " + ackFailCallbacks.size() + " multiple ");
            }
            this.ackFailCallback = ackFailCallbacks.get(0);
        }

        // interface
        processorInterface();
        // annotation
        processorAnnotation();
    }

    private void processorInterface() {
        DelayQueueManage manage = this.delayQueueManage;
        Assert.state(this.delayQueueManage != null, "DelayQueueManage not initialized");
        Producer producer = this.producer;
        Assert.state(this.producer != null, "Producer not initialized");
        List<Consumer> consumers = this.consumers;
        Assert.state(this.consumers != null, "Consumer List not initialized");

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

    private void processorAnnotation() {
        DelayQueueManage manage = this.delayQueueManage;
        Assert.state(this.delayQueueManage != null, "DelayQueueManage not initialized");
        Producer producer = this.producer;
        Assert.state(this.producer != null, "Producer not initialized");

        Consumer consumer;
        for (Map.Entry<String, Set<QueueMetadata>> entry : metadataMap.entrySet()) {
            String queue = entry.getKey();
            Set<QueueMetadata> value = entry.getValue();

            for (QueueMetadata metadata : value) {
                if (metadata.process) {
                    // 注解的全部执行 ack
                    if (autoAck) {
                        AbstractAckConsumer ackConsumer = new QueueListenerAckConsumer(producer, queue, metadata);
                        ackConsumer.setAckFailCallback(ackFailCallback);
                        ackConsumer.setRetryTotal(retryTotal);
                        consumer = ackConsumer;
                    } else if (metadata.ackIndex > -1) {
                        AbstractAckConsumer ackConsumer = new QueueListenerAckConsumer(producer, queue, metadata);
                        ackConsumer.setAckFailCallback(ackFailCallback);
                        ackConsumer.setRetryTotal(retryTotal);
                        consumer = ackConsumer;
                    } else {
                        consumer = new QueueListenerConsumer(producer, queue, metadata);
                    }

                    manage.execute(consumer);
                } else {
                    // log
                    logger.warn("class : " + metadata.clz + " method : " + metadata.method + " args not match");
                }
            }
        }
    }

    private void cacheMetadata(Object bean, Class<?> clz) {
        QueueListener clzListener = clz.getDeclaredAnnotation(QueueListener.class);
        if (null != clzListener) {
            String[] queues = clzListener.queues();
            Method[] methods = clz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(QueueHandler.class)
                        && !method.isAnnotationPresent(QueueListener.class)) {

                    Class<?>[] parameterTypes = method.getParameterTypes();

                    for (String queue : queues) {
                        Set<QueueMetadata> metadata = metadataMap.getOrDefault(queue, new HashSet<>());
                        metadata.add(new QueueMetadata(queue, bean, clz, method, parameterTypes));
                        metadataMap.put(queue, metadata);
                    }
                }
            }
        }

        Method[] methods = clz.getMethods();
        for (Method method : methods) {
            QueueListener methodListener = method.getAnnotation(QueueListener.class);
            if (null != methodListener) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                for (String queue : methodListener.queues()) {
                    Set<QueueMetadata> metadata = metadataMap.getOrDefault(queue, new HashSet<>());
                    metadata.add(new QueueMetadata(queue, bean, clz, method, parameterTypes));
                    metadataMap.put(queue, metadata);
                }
            }
        }
    }

    public void setAutoAck(boolean autoAck) {
        this.autoAck = autoAck;
    }

    public void setRetryTotal(int retryTotal) {
        this.retryTotal = retryTotal;
    }
}
