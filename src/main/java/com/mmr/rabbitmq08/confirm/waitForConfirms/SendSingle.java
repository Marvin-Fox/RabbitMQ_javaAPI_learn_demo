package com.mmr.rabbitmq08.confirm.waitForConfirms;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.mmr.rabbitmq.util.ConnectionUtils;
import com.mmr.rabbitmq08.confirm.RecvConfirm;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 单条发送，waitForConfirms确认
 */
public class SendSingle {
	private static final String ROUTING_KEY = "confirm.single_waitForConfirms";
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		//生产者调用confirmSelect 将channel设置为confirm模式
		channel.confirmSelect();
		//发送消息
		String msg="hello confrom message【single waitForConfirms】";
		channel.basicPublish(RecvConfirm.EXCHANGE_NANE, ROUTING_KEY, null, msg.getBytes());
		//确认消息(可以同时确认多条消息)是否发送成功
		if(channel.waitForConfirms()){
			System.out.println("message send success【single waitForConfirms】");
		}else{
			System.out.println("message send failed【single waitForConfirms】");
		}
		channel.close();
		connection.close();
	}
}
