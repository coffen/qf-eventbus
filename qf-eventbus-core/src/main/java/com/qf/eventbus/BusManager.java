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
	 * 创建BusSignaler
	 * 
	 * @return
	 */
	public BusSignaler buildSignaler();
	
	/**
	 * 创建频道
	 * 
	 * @param request
	 * @return
	 */
	public <T extends AbstractChannel> ChannelHandler<T> buildChannel(CreateRequest request);
	
	/**
	 * 注册发布者到频道
	 * 
	 * @param request
	 * @return
	 */
	public ChannelPublisher registe(RegisteRequest request);
	
	/**
	 * 注册订阅者到频道
	 * 
	 * @param request
	 * @return
	 */
	public ChannelSubscriber subscribe(SubscribeRequest request);
	
	/**
	 * 查询全部频道列表
	 * 
	 * @return
	 */
	public List<String> getChannelList();
	
}
