package com.qf.eventbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public final static String TOKEN_PUBLISHER = "Pub";
	public final static String TOKEN_SUBSCRIBER = "Sub";
	
	private SenderFactory senderFactory = new SenderFactory();
	private ReceiverFactory receiverFactory = new ReceiverFactory();
	
	protected ChannelHolder holder = new ChannelHolder();
	protected Dispatcher dispatcher;
	
	private String name;
	
	public AbstractChannel() {}
	
	public String getName() {
		return name;
	}
	
	public ChannelHolder getHolder() {
		return holder;
	}
	
	public ChannelPublisher register(RegistryInfo info) {
		ChannelPublisher publisher = null;
		if (!isOpen()) {
			log.error("频道未开启或已关闭: channel={}", name);
			return publisher;
		}
		if (!checkRegistryInfo(info)) {
			log.error("注册请求无效: channel={}, info={}", name, info);
			return publisher;
		}
		Sender sender = senderFactory.buildSender(name);
		if (sender != null) {
			try {
				sender.setChannel(this);
				publisher = senderFactory.buildSenderProxy(sender);
				holder.addSender(sender);
			}
			catch (Exception e) {
				log.error("创建发布者代理失败", e);
			}
		}
		else {
			log.error("创建Sender失败: info={}", info);
		}
		return publisher;
	}
	
	public ChannelSubscriber subscribe() {
		ChannelSubscriber subscriber = null;
		if (!isOpen()) {
			log.error("频道未开启或已关闭: channel={}", name);
			return subscriber;
		}
		Receiver receiver = receiverFactory.buildReceiver(name);
		if (receiver != null) {
			try {
				receiver.setChannel(this);
				subscriber = receiverFactory.buildReceiverProxy(receiver);
				holder.addReceiver(receiver);
			}
			catch (Exception e) {
				log.error("创建订阅者代理失败", e);
			}
		}
		else {
			log.error("创建Receiver失败");
		}
		return subscriber;
	}
	
	public boolean unRegister(String sid) {
		if (!isOpen()) {
			log.error("频道未开启或已关闭: channel={}", name);
			return false;
		}
		Sender sender = holder.removeSender(sid);
		if (sender != null) {
			sender.destroy();
			sender = null;
		}
		return true;
	}
	
	public boolean unSubscribe(String rid) {
		if (!isOpen()) {
			log.error("频道未开启或已关闭: channel={}", name);
			return false;
		}
		Receiver receiver = holder.removeReceiver(rid);
		if (receiver != null) {
			receiver.destroy();
			receiver = null;
		}
		return true;
	}
	
	public Sender getSender(String sid) {
		return holder.getSender(sid);
	}
	
	public Receiver getReceiver(String rid) {
		return holder.getReceiver(rid);
	}
	
	public <T> void send(ActionData<T> data) {
		if (isOpen()) {
			dispatcher.submit(data);
		}
	}
	
	protected void buildDispatcher(Dispatcher.Type dispatcherType) {
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
	
	// 验证注册者合法性
	public abstract boolean checkRegistryInfo(RegistryInfo info);

}
