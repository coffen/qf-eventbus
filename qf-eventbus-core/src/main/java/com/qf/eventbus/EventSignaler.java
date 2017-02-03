package com.qf.eventbus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final ConcurrentHashMap<EventWrap, SenderBuilder> senderMap = new ConcurrentHashMap<EventWrap, SenderBuilder>();
	private final ConcurrentHashMap<String, ReceiverBuilder> receiverMap = new ConcurrentHashMap<String, ReceiverBuilder>();
	
	private final ConcurrentHashMap<String, ChannelHandler<? extends AbstractChannel>> handlerMapping = new ConcurrentHashMap<String, ChannelHandler<? extends AbstractChannel>>();
	
	private Class<? extends AbstractChannel> channelClass = DefaultChannel.class;
	
	private AbstractBusManager busManager = new DefaultBusManager();

	@SuppressWarnings("unchecked")
	public <T extends AbstractChannel> ChannelHandler<T> buildChannel(String name) {
		ChannelHandler<? extends AbstractChannel> handler = busManager.buildChannel(name, channelClass);
		if (handler != null) {
			handler = handlerMapping.putIfAbsent(name, handler);
			handler.open();
		}
		ChannelHandler<? extends AbstractChannel> chl = handlerMapping.get(name);
		return chl != null ? (ChannelHandler<T>)chl : null;
	}

	public void register(final Class<? extends Event> eventClass, final String channel) {
		EventWrap wrap = new EventWrap(eventClass, channel);
		if (!senderMap.containsKey(wrap)) {
			SenderBuilder builder = new SenderBuilder(wrap);
			SenderBuilder existed = senderMap.putIfAbsent(wrap, builder);
			if (existed != null && builder.getBuilderId().equals(existed.getBuilderId())) {
				existed.build();
			}
		}
	}

	public void unRegister(Class<? extends Event> eventClass, String channel) {
		EventWrap wrap = new EventWrap(eventClass, channel);
		SenderBuilder builder = senderMap.get(wrap);
		if (builder != null) {
			builder.distory();
			senderMap.remove(builder.getEventWrap());
			builder = null;
		}
	}
	
	public void subscribe(String channel, Listener listener) {
		if (!receiverMap.containsKey(channel)) {
			ReceiverBuilder builder = new ReceiverBuilder(channel);
			ReceiverBuilder existed = receiverMap.putIfAbsent(channel, builder);
			if (existed != null && builder.getBuilderId().equals(existed.getBuilderId())) {
				existed.build();
			}
		}
	}

	public void unSubscribe(String channel) {
		ReceiverBuilder builder = receiverMap.get(channel);
		if (builder != null) {
			builder.distory();
			receiverMap.remove(builder.getChannel());
			builder = null;
		}
	}

	public List<String> getSubscribedChannelList() {
		return Collections.unmodifiableList(new ArrayList<String>(receiverMap.keySet()));
	}
	
	public <T> void fireEvent(final Class<? extends Event> eventClass, String channel, ActionData<T> data) {
		if (eventClass == null || StringUtils.isBlank(channel) || data == null) {
			log.error("事件发起错误, 事件类型,频道或消息数据为空: eventClass={}, channel={}, data={}", eventClass, channel, data);
			return;
		}
		SenderBuilder builder = senderMap.get(new EventWrap(eventClass, channel));
		if (builder != null && builder.getSender() != null) {
			builder.getSender().send(data);
		}
	}
	
	public <T> void fireEvent(final Class<? extends Event> eventClass, ActionData<T> data) {
		if (eventClass == null || data == null) {
			log.error("事件发起错误, 事件类型或消息数据为空: eventClass={}, data={}", eventClass, data);
			return;
		}
		Iterator<EventWrap> it = senderMap.keySet().iterator();
		EventWrap wrap = null;
		SenderBuilder builder = null;
		while (it.hasNext()) {
			wrap = it.next();
			if (wrap.getEventClass() == eventClass) {
				builder = senderMap.get(wrap);
				if (builder != null && builder.getSender() != null) {
					builder.getSender().send(data);
				}
			}
		}		
	}
	
	private class EventWrap {
		
		private Class<? extends Event> eventClass;
		private String channel;
		
		EventWrap(Class<? extends Event> eventClass, String channel) {
			this.eventClass = eventClass;
			this.channel = channel;
		}
		
		String getChannel() {
			return channel;
		}
		
		Class<? extends Event> getEventClass() {
			return eventClass;
		}
		
		@Override
		public boolean equals(Object obj) {
			boolean result = false;
			if (eventClass != null && StringUtils.isNotBlank(channel) && (obj instanceof EventWrap)) {
				EventWrap wrap = (EventWrap)obj;
				result = (eventClass == wrap.getEventClass() && channel.equals(wrap.getChannel()));
			}
			return result;
		}
	}
	
	private class SenderBuilder {
		
		private final String builderId = UUID.randomUUID().toString().replace("-", "");
		
		private Sender sender;
		private EventWrap eventWrap;
		
		SenderBuilder(EventWrap wrap) {
			this.eventWrap = wrap;
		}
		
		public String getBuilderId() {
			return builderId;
		}

		Sender getSender() {
			return sender;
		}
		
		EventWrap getEventWrap() {
			return eventWrap;
		}
		
		void build() {
			if (getSender() == null && getEventWrap() != null) {
				sender = busManager.bindEvent(getEventWrap().getEventClass(), getEventWrap().getChannel());
			}
		}
		
		void distory() {
			if (getSender() != null) {
				busManager.unbindEvent(getSender().getId(), getEventWrap().getChannel());
			}
		}
	}
	
	private class ReceiverBuilder {
		
		private final String builderId = UUID.randomUUID().toString().replace("-", "");
		
		private Receiver receiver;
		private String channel;
		
		ReceiverBuilder(String channel) {
			this.channel = channel;
		}
		
		public String getBuilderId() {
			return builderId;
		}

		public Receiver getReceiver() {
			return receiver;
		}
		
		public String getChannel() {
			return channel;
		}
		
		void build() {
			if (getReceiver() == null && StringUtils.isNotBlank(getChannel())) {
				receiver = busManager.subscribe(getChannel());
			}
		}
		
		void distory() {
			if (getReceiver() != null) {
				busManager.unSubscribe(getReceiver().getId(), getChannel());
			}
		}
	}

}
