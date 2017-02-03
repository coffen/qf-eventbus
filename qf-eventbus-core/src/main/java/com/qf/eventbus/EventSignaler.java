package com.qf.eventbus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 
 * <br>
 * File Name: EventSignaler.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月2日 下午8:59:23 
 * @version: v1.0
 *
 */
public class EventSignaler implements EventPublisher, EventSubscriber {
	
	private AbstractBusManager busManager;
	
	ConcurrentHashMap<String, List<Sender>> channelMapping = new ConcurrentHashMap<String, List<Sender>>();

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractChannel> ChannelHandler<T> buildChannel(String name) {
		ChannelHandler<DefaultChannel> handler = busManager.buildChannel(name, DefaultChannel.class);
		if (handler != null) {
			channelMapping.putIfAbsent(name, new ArrayList<Sender>());
			handler.open();
		}
		return (ChannelHandler<T>)handler;
	}

	@Override
	public void register(Class<? extends Event> eventClass, String channel) {
		busManager.bindEvent(eventClass, channel);
	}

	@Override
	public void unRegister(Class<? extends Event> eventClass, String channel) {
		
	}
	
	@Override
	public void subscribe(String channel, Listener listener) {
		
	}

	@Override
	public void unSubscribe(String channel) {
		
	}

	@Override
	public List<String> getSubscribedChannelList() {
		return null;
	}

}
