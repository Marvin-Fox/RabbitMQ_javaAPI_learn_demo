package com.mmr.rabbitmq03.work.fair;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 公平分发
 * 使用公平分发必须关闭自动应答
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
		/**
		 * 每个消费者发送确认消息之前，消息队列不发送下一个消息给消费者，一次只处理一个消息
		 *
		 * 限制发送给同一个消费者不得超过一条消息
		 * basicQos（）限制发送消息个数
		 */
		int prefetchCount=1;
		channel.basicQos(prefetchCount);

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
