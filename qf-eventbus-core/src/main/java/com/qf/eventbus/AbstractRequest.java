package com.qf.eventbus;

import java.io.Serializable;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 请求抽象类
 * <br>
 * File Name: AbstractRequest.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月5日 下午1:03:21 
 * @version: v1.0
 *
 */
public abstract class AbstractRequest implements Request, Serializable{
	
	private static final long serialVersionUID = -4281812624499102609L;
	
	public String signalerId;	
	public String channel; 
	
	public String getSignalerId() {
		return signalerId;
	}
	
	public void setSignalerId(String signalerId) {
		this.signalerId = signalerId;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
}
