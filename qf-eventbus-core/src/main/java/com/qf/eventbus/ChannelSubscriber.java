package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 频道订阅者
 * <br>
 * File Name: ChannelSubscriber.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月5日 下午3:13:43 
 * @version: v1.0
 *
 */
public interface ChannelSubscriber extends ChannelRegistry {
	
	/**
	 * 设置监听器
	 * 
	 * @param listener
	 */
	public void setListener(Listener listener);
	
	/**
	 * 取消订阅
	 * 
	 * @return
	 */
	public boolean unSubscribe();
	
	/**
	 * 接受消息
	 * 
	 * @param data
	 */
	public <T> void receive(ActionData<T> data);
	
}
