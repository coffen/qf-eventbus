package com.qf.eventbus;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qf.eventbus.executor.SingleExecutor;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 消息分发器
 * <br>
 * File Name: Dispatcher.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月1日 上午11:26:05 
 * @version: v1.0
 *
 */
public abstract class Dispatcher {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private AbstractChannel channel;
	private Executor executor;
	
	private ChannelHolder holder;
	
	public Dispatcher(AbstractChannel channel) {
		this.channel = channel;
		this.holder = channel.getHolder();
		
		this.executor = new SingleExecutor(this);
	}
	
	public ChannelHolder getHolder() {
		return this.holder;
	}
	
	/**
	 * 提交消息
	 * 
	 * @param data
	 */
	public <T> void submit(ActionData<T> data) {
		if (!checkData(data)) {
			log.error("消息验证失败, data={}", data);
			return;
		}
		executor.submit(data);
	}
	
	// 验证消息
	private <T> boolean checkData(ActionData<T> data) {
		if (data == null || StringUtils.isBlank(data.getRegisterId())) {
			return false;
		}
		Sender sender = channel.getSender(data.getRegisterId());
		if (sender == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * 分发消息
	 * 
	 * @param data
	 */
	public abstract <T> void dispatch(ActionData<T> data);
	
	/**
	 * 销毁分发器
	 * 
	 * @param data
	 */
	public void destroy() {
		holder.clean();
		executor.shutDown();
	}
	
	// 分发器类型
	public static enum Type {		
		MUTIL, ORDERED, RANDOM;
	}

}
