package com.dyhhd.mdq.ssimple.pring;

import com.dyhhd.mdq.core.Worker;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * ack worker
 *
 * @author lv ning
 */
public class MyAckWorker implements Worker {

    private String name;

    private String uk;

    private long generateTime;

    private long executeTime;

    MyAckWorker(long executeTime) {
        this(executeTime, TimeUnit.MILLISECONDS);
    }

    MyAckWorker(long executeTime, TimeUnit executeUnit) {
        this.uk = UUID.randomUUID().toString();
        this.generateTime = System.currentTimeMillis();
        this.executeTime = executeUnit.toMillis(executeTime);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUk() {
        return uk;
    }

    public void setUk(String uk) {
        this.uk = uk;
    }

    @Override
    public long getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(long generateTime) {
        this.generateTime = generateTime;
    }

    @Override
    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    @Override
    public String toString() {
        return "MyAckWorker{" +
                "name='" + name + '\'' +
                ", uk='" + uk + '\'' +
                ", generateTime=" + generateTime +
                ", executeTime=" + executeTime +
                '}';
    }
}
