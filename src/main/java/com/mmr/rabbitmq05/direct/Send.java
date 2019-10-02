package com.mmr.rabbitmq05.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * direct
 * 路由模式，通过routingKey定义，消费者通过key接收特定的消息
 */
public class Send {
	private static final String EXCHANGE_NANE="test_exchange_direct";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		//声明路由
		channel.exchangeDeclare(EXCHANGE_NANE, "direct");
		//error路由（通过routingKey定义，消费者通过key接收特定的消息）
		String msgerr="hello direct err";
		String routingKeyErr="error";
		channel.basicPublish(EXCHANGE_NANE, routingKeyErr, null, msgerr.getBytes());
		//info路由
		String msginfo="hello direct info";
		String routingKeyInfo="info";
		channel.basicPublish(EXCHANGE_NANE, routingKeyInfo, null, msginfo.getBytes());
		//warning路由
		String msgwarning="hello direct warning";
		String routingKeyWarning="warning";
		channel.basicPublish(EXCHANGE_NANE, routingKeyWarning, null, msgwarning.getBytes());

		channel.close();
		connection.close();
	}

}
