package com.mmr.rabbitmq06.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * topic
 * 路由匹配
 */
public class Send {
	private static final String EXCHANGE_NANE="test_exchange_topic";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		//声明交换机 topic 模式
		channel.exchangeDeclare(EXCHANGE_NANE, "topic");

		String msg="商品....添加";
		String routingKey="goods.add";
		channel.basicPublish(EXCHANGE_NANE, routingKey, null, msg.getBytes("utf-8"));

		String msgd="商品....删除";
		String routingKeyd="goods.delete";
		channel.basicPublish(EXCHANGE_NANE, routingKeyd, null, msgd.getBytes("utf-8"));
		System.out.println("---send:"+msg);

		channel.close();
		connection.close();
	}
}
