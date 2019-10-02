package com.mmr.rabbitmq07.transaction;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class TxSend {
	private static final String QUEUE_NANE="test_queue_Transaction";
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NANE, false, false, false, null);
		String msg="hello transaction message";
		try {
			//设置为transaction模式
			channel.txSelect();
			channel.basicPublish("", QUEUE_NANE, null, msg.getBytes());
			//提交事务
			channel.txCommit();
			System.out.println("commit");
		} catch (Exception e) {
			//回滚事务
			channel.txRollback();
			System.out.println("rollback");
		}
		channel.close();
		connection.close();
	}
}
