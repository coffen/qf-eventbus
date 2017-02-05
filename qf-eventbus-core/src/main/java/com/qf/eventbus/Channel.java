package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 频道
 * <br>
 * File Name: Channel.java
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
public interface Channel {
	
	/**
	 * 获取频道名称
	 * 
	 * @return String
	 */
	public String getName();
	
	/**
	 * 注册发布者
	 * 
	 * @param info
	 * @return
	 */
	public ChannelPublisher register(RegistryInfo info);
	
	/**
	 * 订阅频道
	 * 
	 * @return
	 */
	public ChannelSubscriber subscribe();

}
