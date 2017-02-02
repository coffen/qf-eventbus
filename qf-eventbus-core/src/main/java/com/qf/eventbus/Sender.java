package com.qf.eventbus;

import java.io.Serializable;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 消息发布器
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
public class Sender implements Serializable {

	private static final long serialVersionUID = 6414438134186349922L;
	
	private String id;	
	private Class<? extends Event> eventClass;
	private String channel;
	
	private Dispatcher dispatcher;
	
	public Sender(String sid, Class<? extends Event> eventClass, String channel, Dispatcher dispatcher) {
		this.id = sid;
		this.eventClass = eventClass;
		this.channel = channel;
		this.dispatcher = dispatcher;
	}
	
	public String getId() {
		return id;
	}
	
	public Class<? extends Event> getEvent() {
		return eventClass;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public <T> void send(ActionData<T> data) {
		dispatcher.submit(data);
	}
	
}
