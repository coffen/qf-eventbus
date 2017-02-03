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
public abstract class AbstractChannel implements Channel, ChannelHandler<AbstractChannel> {
	
	public final static String TOKEN_PUBLISHER = "Pub";
	public final static String TOKEN_SUBSCRIBER = "Sub";
	
	private String name;
	
	protected Dispatcher dispatcher;
	protected ChannelHolder holder;
	
	public String getName() {
		return name;
	}
	
	@Override
	public Sender buildSender(Class<? extends Event> eventClass) {
		String sid = TOKEN_PUBLISHER + UUID.randomUUID().toString().replace("-", "");
		Sender sender = new Sender(sid, eventClass, getName(), dispatcher);
		holder.addSender(sender);
		return sender;
	}
	
	@Override
	public Sender getSender(String sid) {
		return holder.getSender(sid);
	}
	
	@Override
	public void cancelSender(String sid) {
		holder.removeSender(sid);
	}
	
	@Override
	public Receiver buildReceiver() {
		String rid = TOKEN_SUBSCRIBER + UUID.randomUUID().toString().replace("-", "");
		Receiver receiver = new Receiver(rid, getName());
		holder.addReceiver(receiver);
		return receiver;
	}
	
	@Override
	public Receiver getReceiver(String rid) {
		return holder.getReceiver(rid);
	}
	
	@Override
	public void cancelReceiver(String rid) {
		holder.removeReceiver(rid);
	}
	
	public ChannelHolder getHolder() {
		return holder;
	}

}
