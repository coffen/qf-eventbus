package com.qf.eventbus;

import java.io.Serializable;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 消息接收器
 * <br>
 * File Name: Receiver.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年1月26日 下午9:12:21 
 * @version: v1.0
 *
 */
public class Receiver implements Serializable {
	
	private static final long serialVersionUID = 2746998704184626286L;
	
	private String sid;	
	private String channel;
	
	private Listener listener;
	
	public Receiver(String sid, String channel, Listener listener) {
		this.sid = sid;
		this.channel = channel;
		this.listener = listener;
	}
	
	public String getSid() {
		return sid;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public <T> void receive(ActionData<T> data) {
		listener.onEvent(data);
	}
	
}
