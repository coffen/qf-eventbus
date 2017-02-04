package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 消息接收器接口
 * <br>
 * File Name: Receiver.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月4日 上午10:47:54 
 * @version: v1.0
 *
 */
public interface Receiver {
	
	/**
	 * 获取接收器ID
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
	 * 接受消息
	 * 
	 * @param data
	 */
	public <T> void receive(ActionData<T> data);

}
