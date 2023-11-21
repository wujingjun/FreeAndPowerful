package com.miejiang.rabbitmq.死信队列;

import com.miejiang.rabbitmq.constant.RabbitMqConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.util.HashMap;
import java.util.Map;

public class DLXandTTLDemo {

    public static void main(String[] args) {
        try {
            Connection rabbitMqConnectionFactory = RabbitMqConnection.getRabbitMqConnectionFactory();
            Channel channel = rabbitMqConnectionFactory.createChannel();

            channel.exchangeDeclare("exchange.dlx","direct",true);
            channel.exchangeDeclare("exchange.normal","fanout",true);
            Map<String,Object> params = new HashMap<>();
            params.put("x-max-priority",10);
            params.put("x-message-ttl",10000);
            params.put("x-dead-letter-exchange","exchange.dlx");
            params.put("x-dead-letter-routing-key","routingkey");
            channel.queueDeclare("queue.normal",true,false,false,params);
            channel.queueBind("queue.normal","exchange.normal","");

            channel.queueDeclare("queue.dlx",true,false,false,null);
            channel.queueBind("queue.dlx","exchange.dlx","routingkey");

            channel.basicPublish("exchange.normal","routingkey", MessageProperties.PERSISTENT_TEXT_PLAIN,"hello world".getBytes());

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
