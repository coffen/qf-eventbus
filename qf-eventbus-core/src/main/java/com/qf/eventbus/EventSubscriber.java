package com.qf.eventbus;

import java.util.List;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 订阅者
 * <br>
 * File Name: EventSubscriber.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年1月25日 下午8:43:41 
 * @version: v1.0
 *
 */
public interface EventSubscriber {
	
	/**
	 * 订阅事件, 设置监听器
	 * 
	 * @param channel
	 * @param listener
	 */
	public void subscribe(String channel, Listener listener);
	
	/**
	 * 取消订阅
	 * 
	 * @param channel
	 */
	public void unSubscribe(String channel);
	
	/**
	 * 查询所有订阅的频道
	 * 
	 * @return
	 */
	public List<String> getSubscribedChannelList();

}
