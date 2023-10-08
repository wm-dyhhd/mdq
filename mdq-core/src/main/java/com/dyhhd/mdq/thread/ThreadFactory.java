package com.dyhhd.mdq.thread;

import com.dyhhd.mdq.log.Log;
import com.dyhhd.mdq.log.LogFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义 thread factory
 *
 * @author lv ning
 */
public class ThreadFactory {

    public static final String DEFAULT_THREAD_PREFIX = "thread-pool";

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public static ThreadFactory defaultThreadFactory() {
        return new ThreadFactory();
    }

    public ThreadFactory() {
        // 默认
        this(DEFAULT_THREAD_PREFIX);
    }

    public ThreadFactory(String prefix) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        namePrefix = prefix + "-" +
                poolNumber.getAndIncrement() +
                "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        t.setUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());

        if (t.isDaemon())
            t.setDaemon(true);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }

    public static class ThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        private static final Log log = LogFactory.getLog(ThreadUncaughtExceptionHandler.class);

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            log.error(t.getName() + " 线程执行异常", e);
        }
    }
}
