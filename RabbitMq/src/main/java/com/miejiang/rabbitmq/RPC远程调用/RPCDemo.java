package com.miejiang.rabbitmq.RPC远程调用;

import com.miejiang.rabbitmq.constant.RabbitMqConnection;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class RPCDemo {

    public static void main(String[] args) {
        try {
            Connection rabbitMqConnectionFactory = RabbitMqConnection.getRabbitMqConnectionFactory();
            Channel channel = rabbitMqConnectionFactory.createChannel();
            String queue = channel.queueDeclare().getQueue();
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().replyTo(queue).build();
            channel.basicPublish("","rpc_queue",properties,"hello world".getBytes());
        } catch (Exception e ){
            e.printStackTrace();
        }
    }
}
