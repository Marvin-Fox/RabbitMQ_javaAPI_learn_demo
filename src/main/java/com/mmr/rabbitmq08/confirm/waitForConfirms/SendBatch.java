package com.mmr.rabbitmq08.confirm.waitForConfirms;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.mmr.rabbitmq.util.ConnectionUtils;
import com.mmr.rabbitmq08.confirm.RecvConfirm;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 批量发送，waitForConfirms确认
 */
public class SendBatch {
	private static final String ROUTING_KEY = "confirm.batch_waitForConfirms";
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		//生产者调用confirmSelect 将channel设置为confirm模式
		channel.confirmSelect();
		//批量发送
		for(int i=0;i<10;i++){
			String msg="hello confrom message【batch waitForConfirms】=="+i;
			channel.basicPublish(RecvConfirm.EXCHANGE_NANE, ROUTING_KEY, null, msg.getBytes());
		}
		//确认消息(可以同时确认多条消息)是否发送成功
		if(channel.waitForConfirms()){
			System.out.println("message send success【batch waitForConfirms】");
		}else{
			System.out.println("message send failed【batch waitForConfirms】");
		}
		channel.close();
		connection.close();
	}
}
