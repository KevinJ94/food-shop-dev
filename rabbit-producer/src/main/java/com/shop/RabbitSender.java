package com.shop;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitSender {

    //自动注入RabbitTemplate模板类
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //确认机制
    final ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         * correlationData: 回调的相关数据，包含了消息ID
         * ack: ack结果，true代表ack，false代表nack
         * cause: 如果为nack，返回原因，否则为null
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("correlationData: " + correlationData);
            System.err.println("ack: " + ack);
            if(!ack){
                //做一些补偿机制等
                System.err.println("异常处理....");
            }
        }
    };
    //返回机制
    final ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText,
                                    String exchange, String routingKey) {
            System.err.println("return exchange: " + exchange + ", routingKey: "
                    + routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
        }
    };

    //发送消息方法调用: 构建Message消息
//    public void send(Object message, Map<String, Object> properties) throws Exception {
//        MessageHeaders messageHeaders = new MessageHeaders(properties);
//        //注意导包
//        Message msg = MessageBuilder.createMessage(message, messageHeaders);
//        rabbitTemplate.setConfirmCallback(confirmCallback);
//        rabbitTemplate.setReturnCallback(returnCallback);
//        //id + 时间戳 ，保证全局唯一 ，这个是实际消息的ID
//        //在做补偿性机制的时候通过ID来获取到这条消息进行重发
//        String id = "1234567890";
//        CorrelationData correlationData = new CorrelationData(id);
//        //exchange, routingKey, object, correlationData
//
//        rabbitTemplate.convertAndSend("exchange-test", "test.test", msg, correlationData);
//    }

    public void send(Object obj) {

        // 这里的Exchange可以是业务的Exchange，为了方便测试这里直接往死信Exchange里投递消息
        rabbitTemplate.convertAndSend(
                ProducerConfig.DEAD_LETTER_EXCHANGE,
                ProducerConfig.DEAD_LETTER_TEST_ROUTING_KEY,
                obj);
    }
}