package com.mmr.rabbitmq04.fanout;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;

public class Recv1SMS {
	//交换机名
	private static final String EXCHANGE_NANE="test_exchange_fanout";
	//队列名
	private static final String QUEUE_NAME="test_fanout_sms";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection=ConnectionUtils.getConnection();
		final Channel channel=connection.createChannel();
		//先声明交换机
		channel.exchangeDeclare(EXCHANGE_NANE, "fanout");//分发
		//声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//绑定交换机（转发器）将队列绑定到交换机
		channel.queueBind(QUEUE_NAME, EXCHANGE_NANE, "");
		Consumer consumer=new DefaultConsumer(channel){
			public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope envelope, com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {
				String s=new String(body,"utf-8");
				System.out.println("[2] Recv SMS msg:"+s);
				//手动回执一个消息
				channel.basicAck(envelope.getDeliveryTag(), false);
			};
		};
		channel.basicConsume(QUEUE_NAME, false, consumer);
	}
}
