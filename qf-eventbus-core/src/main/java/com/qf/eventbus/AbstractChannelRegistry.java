package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 频道注册抽象类
 * <br>
 * File Name: AbstractChannelRegistry.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月5日 下午4:43:03 
 * @version: v1.0
 *
 */
public abstract class AbstractChannelRegistry implements ChannelRegistry {
	
	private String signalerId;
	private String channel;
	
	private boolean valid;
	
	public AbstractChannelRegistry(String signalerId, String channel) {		
		this.signalerId = signalerId;
		this.channel = channel;
		
		this.valid = true;
	}
	
	public String getSignalerId() {
		return signalerId;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public boolean isValid() {
		return valid;
	}

}
