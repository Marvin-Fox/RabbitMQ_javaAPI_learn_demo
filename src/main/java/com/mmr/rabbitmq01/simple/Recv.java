package com.mmr.rabbitmq01.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;

/**
 * 消费者获取消息
 * @author Marvin
 *
 */
public class Recv {
	private static final String QUEUE_NAME="test_simple_queue";

	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		// TODO Auto-generated method stub
//		oldAPI();
		newAPI();
	}
	/**
	 * 新的方式（DefaultConsumer），推荐使用
	 */
	private static void newAPI() throws IOException, TimeoutException {
		//获取连接
		Connection connection=ConnectionUtils.getConnection();
		//创建频道
		Channel channel=connection.createChannel();
		//队列声明
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//定义消费者
		DefaultConsumer consumer=new DefaultConsumer(channel){
			//获取到达消息
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg=new String(body,"utf-8");
				System.out.println("new api recv:"+msg);
			}
		};
		//监听队列
		channel.basicConsume(QUEUE_NAME, true, consumer);
	}
	/**
	 * 旧的方式（QueueingConsumer），不推荐使用
	 */
	private static void oldAPI() throws IOException, TimeoutException, InterruptedException {
		//获取连接
		Connection connection=ConnectionUtils.getConnection();
		//创建频道
		Channel channel=connection.createChannel();
		//定义队列的消费者
		QueueingConsumer consumer=new QueueingConsumer(channel);
		//监听队列
		channel.basicConsume(QUEUE_NAME, true, consumer);
		while(true){
			Delivery delivery=consumer.nextDelivery();
			String msgString=new String(delivery.getBody());
			System.out.println("[recv] msg:"+msgString);
		}
	}

}
