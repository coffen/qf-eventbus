package com.qf.eventbus;

import java.util.UUID;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 频道抽象类
 * <br>
 * File Name: AbstractChannel.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年1月31日 下午8:17:00 
 * @version: v1.0
 *
 */
public abstract class AbstractChannel implements Channel {
	
	private final static String TOKEN_PUBLISHER = "Pub";
	private final static String TOKEN_SUBSCRIBER = "Sub";
	
	private String name;
	
	private Dispatcher dispatcher;
	private ChannelDataHolder holder;
	
	public String getName() {
		return name;
	}
	
	public Sender getSender(String pid, Class<? extends Event> eventClass) {
		Sender sender = null;
		Sender s = holder.getSender(pid);
		if (s.getEvent() == eventClass) {
			sender = s;
		}
		return sender;
	}
	
	@Override
	public Sender buildSender(Class<? extends Event> eventClass) {
		String pid = TOKEN_PUBLISHER + UUID.randomUUID().toString().replace("-", "");
		Sender sender = new Sender(pid, eventClass, getName(), dispatcher);
		holder.addSender(sender);
		return sender;
	}
	
	@Override
	public Receiver buildReceiver(Listener listener) {
		String sid = TOKEN_SUBSCRIBER + UUID.randomUUID().toString().replace("-", "");
		Receiver receiver = new Receiver(sid, getName(), listener);
		holder.addReceiver(receiver);
		return receiver;
	}
	
	public ChannelDataHolder getHolder() {
		return holder;
	}

}
