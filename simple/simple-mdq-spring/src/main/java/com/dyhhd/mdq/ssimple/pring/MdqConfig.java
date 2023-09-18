package com.dyhhd.mdq.ssimple.pring;

import com.dyhhd.mdq.core.DelayQueueManage;
import com.dyhhd.mdq.core.DelayQueueManageImpl;
import com.dyhhd.mdq.core.DelayQueueProducer;
import com.dyhhd.mdq.core.Producer;
import com.dyhhd.mdq.spring.annotation.EnableMdq;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @author lv ning
 */
@EnableMdq
@Configuration
@ComponentScan(basePackages = "com.dyhhd.mdq.ssimple.pring")
public class MdqConfig {

    @Bean
    public DelayQueueManage delayQueueManage() {
        return new DelayQueueManageImpl();
    }

    @Bean
    public Producer producer() {
        return new DelayQueueProducer();
    }
}
