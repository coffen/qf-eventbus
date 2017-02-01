package com.qf.eventbus;

import java.util.List;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 总线管理器
 * <br>
 * File Name: BusManager.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年1月31日 下午7:39:47 
 * @version: v1.0
 *
 */
public interface BusManager {
	
	/**
	 * 创建渠道
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public Channel buildChannel(String name, Class<? extends Channel> clazz);
	
	/**
	 * 绑定事件到指定频道
	 * 
	 * @param eventClass
	 * @param channel
	 * @return
	 */
	public Sender bindEvent(Class<? extends Event> eventClass, String channel);

	/**
	 * 解除指定频道的绑定
	 * 
	 * @param eventClass
	 * @param channel
	 * @param ignoreMsg
	 */
	public void unbindEvent(Class<? extends Event> eventClass, String channel, boolean ignoreMsg);
	
	/**
	 * 解除所有频道的绑定
	 * 
	 * @param eventClass
	 * @param channel
	 * @param ignoreMsg
	 */
	public void unbindEvent(Class<? extends Event> eventClass, boolean ignoreMsg);
	
	/**
	 * 订阅事件
	 * 
	 * @param eventClazz
	 * @return
	 */
	public Receiver subscribe(String channel);
	
	/**
	 * 取消订阅
	 * 
	 * @param eventClazz
	 */
	public void unSubscribe(String channel);
	
	/**
	 * 查询全部频道列表
	 * 
	 * @return
	 */
	public List<String> getChannelList();
	
}
