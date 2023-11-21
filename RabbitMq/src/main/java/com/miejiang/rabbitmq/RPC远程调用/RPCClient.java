package com.miejiang.rabbitmq.RPC远程调用;

import com.miejiang.rabbitmq.constant.RabbitMqConnection;
import com.rabbitmq.client.*;

import java.util.UUID;

public class RPCClient {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";
    private String replyQueueName;
    private DefaultConsumer consumer;

    public RPCClient() throws Exception {
        Connection rabbitMqConnectionFactory = RabbitMqConnection.getRabbitMqConnectionFactory();
        replyQueueName = channel.queueDeclare().getQueue();
        channel.basicConsume(replyQueueName, true, consumer);
    }

    public String call(String message) throws Exception {
        String response = null;
        String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();
        channel.basicPublish("", requestQueueName, properties, message.getBytes());

        while (true) {
            GetResponse getResponse = channel.basicGet(replyQueueName, true);
            if (getResponse!= null) {
                response = new String(getResponse.getBody());
                if (corrId.equals(getResponse.getProps().getCorrelationId())) {
                    channel.basicAck(getResponse.getEnvelope().getDeliveryTag(), false);
                    break;
                }
            }
        }
        return response;
    }

    public void close() throws Exception {
        channel.close();
        connection.close();
    }

    public static void main(String[] args) throws Exception {
        RPCClient rpcClient = new RPCClient();
        System.out.println(" [x] Requesting fib(30)");
        String response = rpcClient.call("30");
        System.out.println(" [.] Got '"+response+"'");
        rpcClient.close();
    }
}
