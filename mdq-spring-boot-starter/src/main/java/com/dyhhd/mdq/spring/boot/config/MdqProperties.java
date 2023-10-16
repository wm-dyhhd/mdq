package com.dyhhd.mdq.spring.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * mdq 配置信息
 *
 * @author lv ning
 */
@ConfigurationProperties(prefix = "mdq")
public class MdqProperties {

    private final Ack ack = new Ack();

    /**
     * ack 信息
     */
    public static class Ack {

        /**
         * 重试次数
         */
        private int retryTotal = 3;

        /**
         * 启动 自动 ack
         */
        private boolean autoAck;

        public int getRetryTotal() {
            return retryTotal;
        }

        public void setRetryTotal(int retryTotal) {
            this.retryTotal = retryTotal;
        }

        public boolean isAutoAck() {
            return autoAck;
        }

        public void setAutoAck(boolean autoAck) {
            this.autoAck = autoAck;
        }
    }

    public Ack getAck() {
        return ack;
    }
}
