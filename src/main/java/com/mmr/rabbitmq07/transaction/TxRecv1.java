package com.mmr.rabbitmq07.transaction;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;

public class TxRecv1 {
	private static final String QUEUE_NANE="test_queue_Transaction";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NANE, false, false, false, null);
		Consumer consumer=new DefaultConsumer(channel){
			public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope envelope, com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {
				String s=new String(body,"utf-8");
				System.out.println("[TX] Recv msg:"+s);
			};
		};
		channel.basicConsume(QUEUE_NANE, true, consumer);
	}
}
