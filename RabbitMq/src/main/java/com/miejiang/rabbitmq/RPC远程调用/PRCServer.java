package com.miejiang.rabbitmq.RPC远程调用;

import com.miejiang.rabbitmq.constant.RabbitMqConnection;
import com.rabbitmq.client.*;

import java.io.IOException;

public class PRCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) {
        try {
            Connection connection = RabbitMqConnection.getRabbitMqConnectionFactory();
            // 创建一个通道
            Channel channel = connection.createChannel();
            // 声明一个队列
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            // 开启消息监听
            channel.basicQos(1);
            System.out.println(" [x] Awaiting RPC Request");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    AMQP.BasicProperties build = new AMQP.BasicProperties()
                            .builder()
                            .correlationId(properties.getCorrelationId())
                            .build();
                    String response = "";
                    try {
                        String message = new String(body, "UTF-8");
                        int i = Integer.parseInt(message);
                        System.out.println(" [.] fib("+message+")");
                        response += fib(i);
                    } catch (Exception e ) {
                        System.out.println(" [.] "+e.toString());
                    } finally {
                        channel.basicPublish("", properties.getReplyTo(), build, response.getBytes("UTF-8"));
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }

                }
            };
            channel.basicConsume(RPC_QUEUE_NAME, true, consumer);
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    private static int fib(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }
}
