package com.mmr.rabbitmq08.confirm.addConfirmListener;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.mmr.rabbitmq08.confirm.RecvConfirm;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 批量发送，addConfirmListener异步监听确认
 */
public class SendBatch {
	private static final String ROUTING_KEY = "confirm.addConfirmListener";
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		//生产者调用confirmSelect 将channel设置为confirm模式
		channel.confirmSelect();
		//批量发送
		for(int i=0;i<50;i++){
			String msg="hello confrom message【batch addConfirmListener】=="+i;
			channel.basicPublish(RecvConfirm.EXCHANGE_NANE, ROUTING_KEY, null, msg.getBytes());
		}

		//异步监听确认是否发送成功
		channel.addConfirmListener(new ConfirmListener() {
			/**
			 * 消息成功处理
			 * @param deliveryTag:唯一消息标签
			 * @param multiple:是否批量
			 * @throws IOException
			 */
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("message send success【batch addConfirmListener】:deliveryTag【"+ deliveryTag +"】multiple【"+ multiple +"】");
			}

			/**
			 * 消息失败处理
			 * @param deliveryTag:唯一消息标签
			 * @param multiple:是否批量
			 * @throws IOException
			 */
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("message send failed【batch addConfirmListener】:deliveryTag【"+ deliveryTag +"】multiple【"+ multiple +"】");
			}
		});


		//给监听一点时间再关闭
		Thread.sleep(10000);
		channel.close();
		connection.close();
	}
}
