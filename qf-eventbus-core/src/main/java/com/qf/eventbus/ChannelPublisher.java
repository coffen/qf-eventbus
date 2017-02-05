package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 频道发布者
 * <br>
 * File Name: ChannelPublisher.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月5日 下午3:14:14 
 * @version: v1.0
 *
 */
public interface ChannelPublisher extends ChannelRegistry {
	
	/**
	 * 注销发布者
	 * 
	 * @return
	 */
	public boolean unRegister();
	
	/**
	 * 发送消息
	 * 
	 * @param data
	 */
	public <T> void send(ActionData<T> data);

}
