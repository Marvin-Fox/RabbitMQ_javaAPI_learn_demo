package com.mmr.rabbitmq06.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;

public class Recv2 {
	private static final String EXCHANGE_NANE="test_exchange_topic";
	private static final String QUEUE_NANE="test_topic_queue_2";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection=ConnectionUtils.getConnection();
		final Channel channel=connection.createChannel();
		channel.basicQos(1);
		//声明路由（修改类型为topic）
		channel.exchangeDeclare(EXCHANGE_NANE, "topic");
		//声明队列
		channel.queueDeclare(QUEUE_NANE, false, false, false, null);
		//绑定方式（变为匹配符绑定）
		channel.queueBind(QUEUE_NANE, EXCHANGE_NANE, "goods.#");
		Consumer consumer=new DefaultConsumer(channel){
			public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope envelope, com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {
				String s=new String(body,"utf-8");
				System.out.println("[1 goods.#] Recv msg:"+s);
				channel.basicAck(envelope.getDeliveryTag(), false);
			};
		};
		channel.basicConsume(QUEUE_NANE, false, consumer);
	}
}
