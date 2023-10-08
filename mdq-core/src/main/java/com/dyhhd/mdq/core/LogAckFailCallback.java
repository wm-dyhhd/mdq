package com.dyhhd.mdq.core;

import com.dyhhd.mdq.log.Log;
import com.dyhhd.mdq.log.LogFactory;

/**
 * 日志界别的 ack 异常回调
 *
 * @author lv ning
 */
public class LogAckFailCallback implements AckFailCallback {

    protected static Log log = LogFactory.getLog(LogAckFailCallback.class);

    @Override
    public void callback(String queue, Worker worker, int retry) {
        log.debug("queue: " + queue
                + " worker: " + worker
                + " retry: " + retry);
    }
}
