package com.miejiang.rabbitmq.死信队列;

import com.miejiang.rabbitmq.constant.RabbitMqConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class DLXDemo {

    public static void main(String[] args) {
        try {
            Connection rabbitMqConnectionFactory = RabbitMqConnection.getRabbitMqConnectionFactory();

            Channel channel = rabbitMqConnectionFactory.createChannel();

            channel.exchangeDeclare("dlx_exchange","direct");
            Map<String,Object> params = new HashMap<>();
            params.put("x-dead-letter-exchange","dlx_exchange");
            //为队列myqueue 添加 DLX
            channel.queueDeclare("myqueue",true,false,false,params);

            // 为这个DLX指定路由键
//            params.put("x-dead-letter-routing-key","dlx_routing_key");

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
