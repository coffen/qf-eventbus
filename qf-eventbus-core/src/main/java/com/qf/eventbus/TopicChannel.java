package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 主题频道
 * <br>
 * File Name: TopicChannel.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年1月25日 下午8:14:36 
 * @version: v1.0
 *
 */
public interface TopicChannel {
	
	/**
	 * 获取频道名称
	 * 
	 * @return String
	 */
	public String getName();
	
	/**
	 * 创建消息发送器
	 * 
	 * @param eventClass
	 * @return
	 */
	public Sender buildSender(Class<? extends Event> eventClass);
	
	/**
	 * 根据SenderId获取消息发送器
	 * 
	 * @param sid
	 * @return
	 */
	public Sender getSender(String sid);
	
	/**
	 * 注销消息发送器
	 * 
	 * @param sid
	 * @return
	 */
	public void cancelSender(String sid);
	
	/**
	 * 创建消息接收器
	 * 
	 * @return
	 */
	public Receiver buildReceiver();
	
	/**
	 * 根据ReceiverId获取消息接收器
	 * 
	 * @param rid
	 * @return
	 */
	public Receiver getReceiver(String rid);
	
	/**
	 * 注销消息接收器
	 * 
	 * @param sid
	 * @return
	 */
	public void cancelReceiver(String rid);
	
	/**
	 * 打开频道
	 */
	public void open();
	
	/**
	 * 关闭频道
	 */
	public void close();

}
