package com.miejiang.rabbitmq.constant;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqConnection {

    public static Connection getRabbitMqConnectionFactory() throws IOException, TimeoutException {
        // 创建连接和通道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123");
        return factory.newConnection();
    }
}
