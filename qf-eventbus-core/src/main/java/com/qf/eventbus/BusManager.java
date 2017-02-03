package com.qf.eventbus;

import java.util.List;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 总线管理接口
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
	 * @param clazz
	 * @return
	 */
	public <T extends AbstractChannel> ChannelHandler<T> buildChannel(String name, Class<T> clazz);
	
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
	 * @param sid
	 * @param channel
	 */
	public void unbindEvent(String sid, String channel);
	
	/**
	 * 订阅事件
	 * 
	 * @param channel
	 * @return
	 */
	public Receiver subscribe(String channel);
	
	/**
	 * 取消订阅
	 * 
	 * @param rid
	 * @param channel
	 */
	public void unSubscribe(String rid, String channel);
	
	/**
	 * 查询全部频道列表
	 * 
	 * @return
	 */
	public List<String> getChannelList();
	
}
