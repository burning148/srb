package com.atguigu.srb.rabbitutil.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author wangjun
 * @Date 2021/12/15 17:18
 * @Description
 */
@Configuration
public class MQConfig {

    @Bean
    public MessageConverter messageConverter(){
        //json字符串转换器
        return new Jackson2JsonMessageConverter();

    }

    Queue createQueue() {
        return new Queue("queue_name", true, false, false);
    }
}
