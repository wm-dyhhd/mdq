package com.dyhhd.mdq.log;

/**
 * 日志异常
 *
 * @author lv ning
 */
public class LogException extends RuntimeException {

    public LogException() {
        super();
    }

    public LogException(String message) {
        super(message);
    }

    public LogException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogException(Throwable cause) {
        super(cause);
    }
}
