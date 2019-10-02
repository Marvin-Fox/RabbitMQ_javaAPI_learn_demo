package com.mmr.rabbitmq02.work;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;


/**
 * 轮询分发
 * 				|----clinet1
 * P----Queue---|
 * 				|----clinet2
 */
public class Send {
	private static final String QUEUE_NAME="test_work_queue";

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		// TODO Auto-generated method stub
		//获取连接
		Connection connection=ConnectionUtils.getConnection();
		//获取channel
		Channel channel=connection.createChannel();
		//声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		for(int i=0;i<10;i++){
			String s="hello:"+i;
			System.out.println("[work send] i="+i);
			channel.basicPublish("", QUEUE_NAME, null, s.getBytes());
			Thread.sleep(2*10);
		}
		channel.close();
		connection.close();
	}
}
