package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 频道注册
 * <br>
 * File Name: ChannelRegistry.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月5日 下午3:14:00 
 * @version: v1.0
 *
 */
public interface ChannelRegistry {
	
	/**
	 * 获取Id
	 * 
	 * @return
	 */
	public String getSignalerId();
	
	/**
	 * 获取频道名称
	 * 
	 * @return
	 */
	public String getChannel();
	
	/**
	 * 销毁注册信息
	 */
	public void destroy();

}
