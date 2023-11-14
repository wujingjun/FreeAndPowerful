package com.miejiang.rabbitmq.constant;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqConnection {

    public static Connection getRabbitMqConnectionFactory() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory() {
            @Override
            public String getHost() {
                return "127.0.0.1";
            }

            @Override
            public int getPort() {
                return 5672;
            }

            @Override
            public String getVirtualHost() {
                return "/";
            }

            @Override
            public String getUsername() {
                return "guest";
            }

            @Override
            public String getPassword() {
                return "guest";
            }
        };
        return connectionFactory.newConnection();
    }
}
