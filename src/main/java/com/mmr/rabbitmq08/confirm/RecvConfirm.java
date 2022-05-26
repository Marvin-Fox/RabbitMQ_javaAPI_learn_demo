package com.mmr.rabbitmq08.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class RecvConfirm {
	public static final String EXCHANGE_NANE = "test_exchange_confirm";
	public static final String QUEUE_NANE="test_queue_confirm";
	public static final String ROUTING_KEY = "confirm.#";

	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();

		//声明交换机和队列，然后进行绑定设置路由Key
		channel.exchangeDeclare(EXCHANGE_NANE, "topic", true);
		channel.queueDeclare(QUEUE_NANE, false, false, false, null);
		channel.queueBind(QUEUE_NANE, EXCHANGE_NANE, ROUTING_KEY);

		Consumer consumer=new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg=new String(body,"utf-8");
				System.out.println("【confirm】 RecvConfirm msg:"+msg);
			}
		};
		channel.basicConsume(QUEUE_NANE, true, consumer);
	}
}
