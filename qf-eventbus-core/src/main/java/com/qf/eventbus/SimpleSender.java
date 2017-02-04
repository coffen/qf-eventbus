package com.qf.eventbus;

import java.io.Serializable;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 消息发送器
 * <br>
 * File Name: Sender.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年1月26日 下午9:12:38 
 * @version: v1.0
 *
 */
public class SimpleSender implements Sender, Serializable {

	private static final long serialVersionUID = 6414438134186349922L;
	
	private String id;	
	private String channel;
	
	private Class<? extends Event> eventClass;
	
	public SimpleSender(String sid, String channel) {
		this.id = sid;
		this.channel = channel;
	}
	
	public String getId() {
		return id;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public void setEventClass(Class<? extends Event> eventClass) {
		this.eventClass = eventClass;
	}
	
	public Class<? extends Event> getEvent() {
		return eventClass;
	}
	
	public <T> void send(ActionData<T> data) {}
	
}
