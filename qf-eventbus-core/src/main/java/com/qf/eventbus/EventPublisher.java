package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 发布者
 * <br>
 * File Name: EventPublisher.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年1月26日 下午9:26:03 
 * @version: v1.0
 *
 */
public interface EventPublisher {
	
	/**
	 * 创建频道
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public <T extends AbstractTopicChannel> TopicChannelHandler<T> buildChannel(String name);
	
	/**
	 * 注册订阅事件并绑定到指定频道
	 * 
	 * @param eventClass
	 * @param channel
	 */
	public void register(Class<? extends Event> eventClass, String channel);
	
	/**
	 * 从指定频道注销事件
	 * 
	 * @param eventClass
	 * @param channel
	 */
	public void unRegister(Class<? extends Event> eventClass, String channel);
	
}
