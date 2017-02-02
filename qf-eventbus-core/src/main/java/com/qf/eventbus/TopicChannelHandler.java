package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 主题频道工具
 * <br>
 * File Name: TopicChannelHandler.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月2日 下午10:55:24 
 * @version: v1.0
 *
 */
public interface TopicChannelHandler<T extends AbstractTopicChannel> extends ChannelHandler<T> {
	
	/**
	 * 设置分发器
	 * 
	 * @param dispatcher
	 */
	public void setDispatcher(Dispatcher dispatcher);
	
}
