package com.mmr.rabbitmq08.confirm.addConfirmListener;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.mmr.rabbitmq08.confirm.RecvConfirm;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 单条发送，addConfirmListener异步监听确认
 */
public class SendSingle {
	private static final String ROUTING_KEY = "confirm.addConfirmListener";
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		//生产者调用confirmSelect 将channel设置为confirm模式
		channel.confirmSelect();
		String msg="hello confrom message【single addConfirmListener】";
		channel.basicPublish(RecvConfirm.EXCHANGE_NANE, ROUTING_KEY, null, msg.getBytes());
		//异步监听确认是否发送成功
		channel.addConfirmListener(new ConfirmListener() {
			/**
			 * 消息成功处理
			 * @param deliveryTag:唯一消息标签
			 * @param multiple:是否批量
			 * @throws IOException
			 */
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("message send success【single addConfirmListener】:deliveryTag【"+ deliveryTag +"】multiple【"+ multiple +"】");
			}

			/**
			 * 消息失败处理
			 * @param deliveryTag:唯一消息标签
			 * @param multiple:是否批量
			 * @throws IOException
			 */
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("message send failed【single addConfirmListener】:deliveryTag【"+ deliveryTag +"】multiple【"+ multiple +"】");
			}
		});

//		channel.addReturnListener(new ReturnListener() {
//			public void handleReturn(int i, String s, String s1, String s2, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
//				//如果交换机分发消息到队列失败，则会执行此方法（用来处理交换机分发消息到队列失败的情况）
//				System.out.println("*****"+i);  //标识
//				System.out.println("*****"+s);  //
//				System.out.println("*****"+s1); //交换机名
//				System.out.println("*****"+s2); //交换机对应的队列的key
//				System.out.println("*****"+new String(bytes));  //发送的消息
//			}
//		});
		//给监听一点时间再关闭
		Thread.sleep(5000);
		channel.close();
		connection.close();
	}
}
