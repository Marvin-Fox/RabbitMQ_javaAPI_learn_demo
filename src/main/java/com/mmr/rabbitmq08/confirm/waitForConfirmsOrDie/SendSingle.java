package com.mmr.rabbitmq08.confirm.waitForConfirmsOrDie;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.mmr.rabbitmq08.confirm.RecvConfirm;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 单条发送，waitForConfirmsOrDie确认
 */
public class SendSingle {
	private static final String ROUTING_KEY = "confirm.single_waitForConfirmsOrDie";
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		//生产者调用confirmSelect 将channel设置为confirm模式
		channel.confirmSelect();
		//发送消息
		String msg="hello confrom message【single waitForConfirmsOrDie】";
		channel.basicPublish(RecvConfirm.EXCHANGE_NANE, ROUTING_KEY, null, msg.getBytes());
		//确认消息(可以同时确认多条消息)是否发送成功【通过异常来判断，源码是通过调用waitForConfirms】
		try {
			channel.waitForConfirmsOrDie();
			System.out.println("message send success【single waitForConfirmsOrDie】");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("message send failed【single waitForConfirmsOrDie】");
		}

		channel.close();
		connection.close();
	}
}
