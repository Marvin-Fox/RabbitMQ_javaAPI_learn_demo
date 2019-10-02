package com.mmr.rabbitmq04.fanout;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 发布订阅模式
 * channel.exchangeDeclare（）交换机
 * 一个生产者消息发送到交换机，交换机分两个消息队列分别发送给两个消费者
 * 实现了一个任务后同时发送邮件和短信的功能
 */
public class Send {
	//交换机名
	private static final String EXCHANGE_NANE="test_exchange_fanout";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		//声明交换机
		channel.exchangeDeclare(EXCHANGE_NANE, "fanout");//分发

		//发送消息，发送到交换机
		String msg="hello ps";
		channel.basicPublish(EXCHANGE_NANE, "", null, msg.getBytes());
		System.out.println("send "+msg);

		channel.close();
		connection.close();
	}
}
