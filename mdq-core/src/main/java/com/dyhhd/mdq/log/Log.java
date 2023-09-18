package com.dyhhd.mdq.log;

/**
 * 日志
 *
 * @author lv ning
 */
public interface Log {

    boolean isDebugEnabled();

    boolean isTraceEnabled();

    void error(String s, Throwable e);

    void error(String s);

    void debug(String s);

    void trace(String s);

    void warn(String s);
}
