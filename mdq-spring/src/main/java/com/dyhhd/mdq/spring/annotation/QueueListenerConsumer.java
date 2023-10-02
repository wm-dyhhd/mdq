package com.dyhhd.mdq.spring.annotation;

import com.dyhhd.mdq.core.AbstractConsumer;
import com.dyhhd.mdq.core.Producer;
import com.dyhhd.mdq.core.Worker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 队列监听器 消费者
 *
 * @author lv ning
 */
public class QueueListenerConsumer extends AbstractConsumer {

    private final Set<QueueMetadata> value;

    public QueueListenerConsumer(Producer producer,
                                 String queue,
                                 Set<QueueMetadata> value) {
        super(producer, queue);
        this.value = value;
    }

    @Override
    protected void run(Worker worker) {

        inject(worker);
    }

    private void inject(Worker worker) {
        for (QueueMetadata metadata : value) {
            Class<?>[] parameterTypes = metadata.parameterTypes;
            Method method = metadata.method;
            if (null == parameterTypes || parameterTypes.length == 0) {
                continue;
            }

            boolean inject = false;
            Object[] args = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                if (parameterType == Worker.class) {
                    args[i] = worker;
                    inject = true;
                } else if (parameterType == worker.getClass()) {
                    args[i] = worker;
                    inject = true;
                } else if (parameterType.isInstance(worker)) {
                    args[i] = worker;
                    inject = true;
                }
            }

            if (!inject) {
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    if (String.class == parameterType) {
                        args[i] = worker.toString();
                        inject = true;
                        break;
                    } else if (Map.class == parameterType) {
                        args[i] = objectToMap(worker);
                        inject = true;
                        break;
                    }
                }
            }

            try {
                if (inject) {
                    // 执行方法
                    method.setAccessible(true);
                    method.invoke(metadata.bean, args);
                }
            } catch (ReflectiveOperationException e) {
//                throw new RuntimeException(e);
            }
        }
    }

    private Map<String, Object> objectToMap(Object object) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clz = object.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                map.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                log.error("object: " + object + " field：" + field.getName() + " set error", e);
//                throw new RuntimeException(e);
            }
        }
        return map;
    }
}
