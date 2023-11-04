package com.miejiang.rabbitmq.demo_1;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RabbitConsumer {

    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_kEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 5672;

    public static void main(String[] args) {
        try {
            Address address = new Address(IP_ADDRESS, PORT);
            Address[] addresses = {address};
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setUsername("admin");
            connectionFactory.setPassword("123");
            Connection connection = connectionFactory.newConnection(addresses);
            Channel channel = connection.createChannel();
            channel.basicQos(64);
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("接收到的消息：" + new String(body));
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };
            channel.basicConsume(QUEUE_NAME,consumer);
            TimeUnit.SECONDS.sleep(5);
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
