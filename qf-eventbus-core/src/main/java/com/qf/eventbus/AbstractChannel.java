package com.qf.eventbus;

import com.qf.eventbus.Dispatcher.Type;
import com.qf.eventbus.dispatcher.MutilDispatcher;
import com.qf.eventbus.dispatcher.OrderedDispatcher;
import com.qf.eventbus.dispatcher.RandomDispatcher;

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
	
	protected ChannelHolder holder = new ChannelHolder();
	protected Dispatcher dispatcher;
	
	private String name;
	
	private SenderFactory senderFactory;
	private ReceiverFactory receiverFactory;
	
	private Class<? extends Sender> senderClazz;
	private Class<? extends Receiver> receiverClazz;
	
	public String getName() {
		return name;
	}
	
	@Override
	public Sender buildSender() {
		Sender sender = senderFactory.create(senderClazz, name, dispatcher);
		if (sender != null) {
			holder.addSender(sender);
		}
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
		Receiver receiver = receiverFactory.create(receiverClazz, name);
		if (receiver != null) {
			holder.addReceiver(receiver);
		}
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
	
	@Override
	public void setSenderFactory(SenderFactory factory) {
		this.senderFactory = factory;		
	}
	
	@Override
	public void setReceiverFactory(ReceiverFactory factory) {
		this.receiverFactory = factory;		
	}
	
	protected void buildDispatcher(Type dispatcherType) {
		if (dispatcherType == Dispatcher.Type.ORDERED) {
			this.dispatcher = new OrderedDispatcher(this);
		}
		else if (dispatcherType == Dispatcher.Type.RANDOM){
			this.dispatcher = new RandomDispatcher(this);
		}
		else {
			this.dispatcher = new MutilDispatcher(this);
		}
	}

}
