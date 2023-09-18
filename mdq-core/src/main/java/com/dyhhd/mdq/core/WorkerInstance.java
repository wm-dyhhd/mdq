package com.dyhhd.mdq.core;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 延迟队列任务
 *
 * @author lv ning
 */
class WorkerInstance<T> implements Worker {

    private String name;

    private String uk;

    private long generateTime;

    private long executeTime;

    private T body;

    WorkerInstance(long executeTime) {
        this(executeTime, TimeUnit.MILLISECONDS);
    }

    WorkerInstance(long executeTime, TimeUnit executeUnit) {
        this.uk = UUID.randomUUID().toString();
        this.generateTime = System.currentTimeMillis();
        this.executeTime = executeUnit.toMillis(executeTime);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUk() {
        return uk;
    }

    @Override
    public long getGenerateTime() {
        return generateTime;
    }

    @Override
    public long getExecuteTime() {
        return executeTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUk(String uk) {
        this.uk = uk;
    }

    public void setGenerateTime(long generateTime) {
        this.generateTime = generateTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "WorkerInstance{" +
                "name='" + name + '\'' +
                ", uk='" + uk + '\'' +
                ", generateTime=" + generateTime +
                ", executeTime=" + executeTime +
                ", body=" + body +
                '}';
    }
}
