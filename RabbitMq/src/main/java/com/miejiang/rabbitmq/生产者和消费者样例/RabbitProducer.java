package com.miejiang.rabbitmq.生产者和消费者样例;

import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

public class RabbitProducer {

    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_kEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 5672;

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(IP_ADDRESS);
        connectionFactory.setPort(PORT);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        Connection connection = connectionFactory.newConnection();// 建立连接
        Channel channel = connection.createChannel();// 创建信道
        // 创建一个 type="direct"、持久化的、非自动删除的交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false,null);
        // 创建一个 持久化的、非排他的、非自动删除的队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 将交换器与队列通过路由键绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_kEY);
        //使用mandatory参数，如果没有一个队列能处理该消息，则消息会被丢弃，不会被路由到其他队列
        channel.basicPublish(EXCHANGE_NAME, "routingkey_demo2", MessageProperties.PERSISTENT_TEXT_PLAIN, "hello,world!".getBytes());
        // 关闭频道和连接
        channel.close();
        connection.close();

        Map<String,Object> params = new HashMap<>();
        params.put("alternate-exchange","myAe");
        channel.exchangeDeclare("normalExchange","direct",true,false,params);
        channel.exchangeDeclare("myAe","fanout",true,false,null);
        channel.queueDeclare("normalQueue",true,false,false,null);
        channel.queueBind("normalQueue","normalExchange","normalKey");
        channel.queueDeclare("unrouteQueue",true,false,false,null);
        channel.queueBind("unrouteQueue","myAe","");

        // 将备份交换器与主交换器关联
        channel.exchangeBind("normalExchange","myAe","");

    }

}
