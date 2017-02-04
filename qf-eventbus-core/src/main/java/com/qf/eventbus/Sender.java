package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 消息发送器接口
 * <br>
 * File Name: Sender.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月4日 上午10:26:29 
 * @version: v1.0
 *
 */
public interface Sender {
	
	/**
	 * 获取发送器Id
	 * 
	 * @return
	 */
	public String getId();
	
	/**
	 * 获取频道
	 * 
	 * @return
	 */
	public String getChannel();
	
	/**
	 * 发送消息
	 * 
	 * @param data
	 */
	public <T> void send(ActionData<T> data);
}
