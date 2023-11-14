package com.miejiang.rabbitmq.备份交换器样例;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class BackupExchangeDemo {

    private static final String BACKUP_EXCHANGE_NAME = "BACKUP_EXCHANGE_NEW_1";
    private static final String EXCHANGE_NAME = "MY_EXCHANGE_NEW_1";

    private static final String QUEUE_NAME = "my_queue_new_1";

    private static final String BACKUP_QUEUE_NAME = "backup_queue_new_1";

    public static void main(String[] args) {
        try {
            // 创建连接和通道
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            factory.setPort(5672);
            factory.setUsername("admin");
            factory.setPassword("123");

            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();

            //声明主交换器
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            //声明备份交换器
            channel.exchangeDeclare(BACKUP_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            // 将备份交换器与主交换器关联
            channel.exchangeBind(BACKUP_EXCHANGE_NAME, EXCHANGE_NAME, "");

            // 声明队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // 将队列绑定到主交换器
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "normal");

            // 声明备份队列
            channel.queueDeclare(BACKUP_QUEUE_NAME, false, false, false, null);

            // 将备份队列绑定到备份交换器
            channel.queueBind(BACKUP_QUEUE_NAME, BACKUP_EXCHANGE_NAME, "");

            // 发送一条消息到主交换器
            channel.basicPublish(EXCHANGE_NAME, "normal", null, "hello world".getBytes("UTF-8"));

            // 模拟无法路由的情况，发送一条无法匹配的消息到主交换器
            channel.basicPublish(EXCHANGE_NAME, "", null, "fuck world".getBytes("UTF-8"));

            // 消费主队列的消息
            channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("消费者收到消息：" + new String(body, Charset.forName("UTF-8")));
                }
            });

            // 消费备份队列的消息
            channel.basicConsume(BACKUP_QUEUE_NAME, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("消费者收到备份消息：" + new String(body, Charset.forName("UTF-8")));
                }
            });

            // 等待一些时间，以便观察消息的处理情况
            Thread.sleep(2000);
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
