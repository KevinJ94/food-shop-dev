package com.shop;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerConfig {

    // 死信消息队列设计实现订单超时自动取消
    // 生产者 -- > 业务消息 --> 业务消息路由(死信交换机) --> 业务消息队列(不消费,等待过期) --> 重定向路由(过期后) --> 重定向消息队列 --> 被消费



    //死信交换机, 或者是业务交换机
    public static final String DEAD_LETTER_EXCHANGE = "TDL_EXCHANGE";
    //死信路由, 业务路由key
    public static final String DEAD_LETTER_TEST_ROUTING_KEY = "TDL_KEY";
    //重定向, 待处理路由key
    public static final String DEAD_LETTER_REDIRECT_ROUTING_KEY = "TKEY_R";
    //死信队列
    public static final String DEAD_LETTER_QUEUE = "TDL_QUEUE";
    //重定向队列
    public static final String REDIRECT_QUEUE = "TREDIRECT_QUEUE";

    /**
     * 死信队列跟交换机类型没有关系 不一定为directExchange  不影响该类型交换机的特性.
     */
    @Bean("deadLetterExchange")
    public Exchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(DEAD_LETTER_EXCHANGE).durable(true).build();
    }

    @Bean("deadLetterQueue")
    public Queue deadLetterQueue() {
        Map<String, Object> args = new HashMap<>();
//       x-dead-letter-exchange    声明  死信队列Exchange
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);

        args.put("x-message-ttl", 30000);
//       x-dead-letter-routing-key    声明 死信队列抛出异常重定向队列的routingKey(TKEY_R)
        args.put("x-dead-letter-routing-key", DEAD_LETTER_REDIRECT_ROUTING_KEY);
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).withArguments(args).build();
    }

    @Bean("redirectQueue")
    public Queue redirectQueue() {
        return QueueBuilder.durable(REDIRECT_QUEUE).build();
    }

    /**
     * 死信队列绑定到死信交换器上.
     *
     * @return the binding
     */
    @Bean
    public Binding deadLetterBinding() {
        return new Binding(DEAD_LETTER_QUEUE, Binding.DestinationType.QUEUE, DEAD_LETTER_EXCHANGE, DEAD_LETTER_TEST_ROUTING_KEY, null);

    }

    /**
     * 将重定向队列通过routingKey(TKEY_R)绑定到死信队列的Exchange上
     *
     * @return the binding
     */
    @Bean
    public Binding redirectBinding() {
        return new Binding(REDIRECT_QUEUE, Binding.DestinationType.QUEUE, DEAD_LETTER_EXCHANGE, DEAD_LETTER_REDIRECT_ROUTING_KEY, null);
    }
}
