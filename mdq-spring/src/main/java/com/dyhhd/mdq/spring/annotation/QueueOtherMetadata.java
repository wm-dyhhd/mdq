package com.dyhhd.mdq.spring.annotation;

import com.dyhhd.mdq.core.Worker;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 队列其它类型数据
 *
 * @author lv ning
 */
public enum QueueOtherMetadata {

    STR(String.class) {
        @Override
        public Object get(Worker worker) {
            return worker.toString();
        }
    },

    MAP(Map.class) {
        @Override
        public Object get(Worker worker) {
            Map<String, Object> map = new HashMap<>();
            Class<?> clz = worker.getClass();
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(worker));
                } catch (IllegalAccessException e) {
//                    throw new RuntimeException(e);
                }
            }
            return map;
        }
    },
    ;
    private final Class<?> clz;

    public abstract Object get(Worker worker);

    QueueOtherMetadata(Class<?> clz) {
        this.clz = clz;
    }

    public static QueueOtherType match(Class<?>[] parameterTypes) {
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            for (QueueOtherMetadata value : values()) {
                if (value.clz == parameterType) {
                    QueueOtherType otherType = new QueueOtherType();
                    otherType.setIndex(i);
                    otherType.setMetadata(value);
                    return otherType;
                }
            }
        }
        return null;
    }
}
