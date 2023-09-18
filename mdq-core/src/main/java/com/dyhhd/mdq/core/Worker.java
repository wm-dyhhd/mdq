package com.dyhhd.mdq.core;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延迟队列任务
 *
 * @author lv ning
 */
public interface Worker extends Delayed {

    /**
     * 获取名字
     *
     * @return 名字
     */
    String getName();

    /**
     * 获取唯一标识
     *
     * @return 唯一的值
     */
    String getUk();

    /**
     * 获取生成时间（毫秒）
     *
     * @return 生成时间
     */
    long getGenerateTime();

    /**
     * 获取执行时间（毫秒）
     *
     * @return 执行时间
     */
    long getExecuteTime();

    @Override
    default long getDelay(TimeUnit unit) {
        return unit.convert(getGenerateTime() + getExecuteTime() - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS);
    }

    @Override
    default int compareTo(Delayed o) {
        return (int) (getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
//        Worker worker = (Worker) o;
//        return (int) (
//                ((getGenerateTime() + getExecuteTime()) - System.currentTimeMillis()) -
//                        ((worker.getGenerateTime() + worker.getExecuteTime()) - System.currentTimeMillis())
//        );
    }
}
