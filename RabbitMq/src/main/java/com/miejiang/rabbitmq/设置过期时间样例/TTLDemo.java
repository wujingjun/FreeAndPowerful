package com.miejiang.rabbitmq.设置过期时间样例;

import com.miejiang.rabbitmq.constant.RabbitMqConnection;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TTLDemo {

    public static void main(String[] args) {
        try {
            Connection rabbitMqConnectionFactory = RabbitMqConnection.getRabbitMqConnectionFactory();
            Channel channel = rabbitMqConnectionFactory.createChannel();

            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            // 持久化消息
            builder.deliveryMode(2);
            // 设置TTL=600000ms
            builder.expiration("600000");
            AMQP.BasicProperties properties = builder.build();
            channel.basicPublish("exchange_name", "ttl_queue", properties, "hello world".getBytes());

            
        } catch (Exception e) {
            log.error("RabbitMQ连接失败", e);
        }

    }
}
