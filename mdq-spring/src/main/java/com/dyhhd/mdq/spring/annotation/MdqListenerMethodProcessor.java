package com.dyhhd.mdq.spring.annotation;

import com.dyhhd.mdq.core.Consumer;
import com.dyhhd.mdq.core.DelayQueueManage;
import com.dyhhd.mdq.core.Producer;
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
public class MdqListenerMethodProcessor implements SmartInitializingSingleton, BeanFactoryPostProcessor, BeanPostProcessor {

    public static final String BEAN_NAME = MdqListenerMethodProcessor.class.getName();

    protected final Log logger = LogFactory.getLog(getClass());

    private ConfigurableListableBeanFactory beanFactory;

    private DelayQueueManage delayQueueManage;

    private Producer produce;

    private final Map<String, Set<QueueMetadata>> metadataMap = new ConcurrentHashMap<>();

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;

        Map<String, DelayQueueManage> manageMap = beanFactory.getBeansOfType(DelayQueueManage.class, false, false);
        List<DelayQueueManage> manages = new ArrayList<>(manageMap.values());
        AnnotationAwareOrderComparator.sort(manages);
        if (manages.isEmpty()) {
            logger.warn("dmq manage not exists");
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
        } else {
            if (producers.size() > 1) {
                logger.warn("dmq producer exists " + producers.size() + " multiple ");
            }
            this.produce = producers.get(0);
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        cacheMetadata(bean, targetClass);
        return bean;
    }

    @Override
    public void afterSingletonsInstantiated() {
        DelayQueueManage manage = this.delayQueueManage;
        Assert.state(this.delayQueueManage != null, "manage not instantiate");
        Producer produce = this.produce;
        Assert.state(this.produce != null, "producer not instantiate");

        List<Consumer> consumers = new ArrayList<>();
        Consumer consumer;
        for (Map.Entry<String, Set<QueueMetadata>> entry : metadataMap.entrySet()) {
            String queue = entry.getKey();
            Set<QueueMetadata> value = entry.getValue();

            consumer = new QueueListenerConsumer(produce, queue, value);
            consumers.add(consumer);
        }
        manage.executes(consumers);
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
}
