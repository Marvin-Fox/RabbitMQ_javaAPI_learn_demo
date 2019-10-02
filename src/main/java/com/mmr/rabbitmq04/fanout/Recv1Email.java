package com.mmr.rabbitmq04.fanout;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;

public class Recv1Email {

	private static final String EXCHANGE_NANE="test_exchange_fanout";
	private static final String QUEUE_NAME="test_fanout_email";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection=ConnectionUtils.getConnection();
		final Channel channel=connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NANE, "fanout");
		//声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//绑定交换机（转发器）
		channel.queueBind(QUEUE_NAME, EXCHANGE_NANE, "");
		Consumer consumer=new DefaultConsumer(channel){
			public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope envelope, com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {
				String s=new String(body,"utf-8");
				System.out.println("[1] Recv Email msg:"+s);
				//手动回执一个消息
				channel.basicAck(envelope.getDeliveryTag(), false);
			};
		};
		channel.basicConsume(QUEUE_NAME, false, consumer);
	}
}
