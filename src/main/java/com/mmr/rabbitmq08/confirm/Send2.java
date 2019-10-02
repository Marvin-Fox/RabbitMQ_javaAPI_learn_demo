package com.mmr.rabbitmq08.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 批量发送，确认
 */
public class Send2 {
	private static final String QUEUE_NANE="test_queue_confirm";
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NANE, false, false, false, null);
		//生产者调用confirmSelect 将channel设置为confirm模式
		channel.confirmSelect();
		//批量发送
		for(int i=0;i<10;i++){
			String msg="hello confrom message=="+i;
			channel.basicPublish("", QUEUE_NANE, null, msg.getBytes());
		}
		//确认
		if(channel.waitForConfirms()){
			System.out.println("message seng success");
		}else{
			System.out.println("message seng failed");
		}
		channel.close();
		connection.close();
	}
}
