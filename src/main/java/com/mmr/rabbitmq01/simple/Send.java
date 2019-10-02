package com.mmr.rabbitmq01.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
	//队列名
	private static final String QUEUE_NAME="test_simple_queue";

	public static void main(String[] args) throws IOException, TimeoutException {
		//获取连接
		Connection connection=ConnectionUtils.getConnection();
		//从连接中创建通道
		Channel channel=connection.createChannel();
		//声明（创建）队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//发送的消息
		String msg="hello simple";
		channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());

		System.out.println("--send queue:"+msg);
		//关闭通道和连接
		channel.close();
		connection.close();

	}
}
