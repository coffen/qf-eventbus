package com.qf.eventbus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: Bus信号员
 * <br>
 * File Name: BusSignaler.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月6日 上午9:01:27 
 * @version: v1.0
 *
 */
public class BusSignaler implements AutoCloseable {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final static String ID_PREFIX = "Sig-";	
	private final String id = ID_PREFIX + UUID.randomUUID().toString().replace("-", "");
	
	private final static Dispatcher.Type DEFAULT_DISPATCHER = Dispatcher.Type.MUTIL;
	
	private final Map<String, ChannelPublisher> publisherMap = new HashMap<String, ChannelPublisher>();
	private final Map<String, ChannelSubscriber> subscriberMap = new HashMap<String, ChannelSubscriber>();
	
	private final ConcurrentHashMap<Class<? extends Event>, Set<String>> channelEventMapping = new ConcurrentHashMap<Class<? extends Event>, Set<String>>();
	
	private final Map<String, ChannelHandler<? extends AbstractChannel>> handlerMapping = new HashMap<String, ChannelHandler<? extends AbstractChannel>>();
	
	private BusManager busManager;
	
	private boolean isClose = false;
	
	public BusSignaler(BusManager busManager) {
		this.busManager = busManager;
	}
	
	public String getSignalerId() {
		return id;
	}
	
	public boolean buildChannel(String channel) {
		return buildChannel(channel, DEFAULT_DISPATCHER);
	}
	
	public boolean buildChannel(String channel, Dispatcher.Type type) {
		boolean created = false;
		if (!checkStatus()) {
			return created;
		}
		CreateRequest request = new CreateRequest();
		request.setSignalerId(id);
		request.setChannel(channel);
		ChannelHandler<AbstractChannel> handler = busManager.buildChannel(request);
		created = (handler != null);
		if (created) {
			handler.setDispatcher(type);
			handlerMapping.put(channel, handler);
			log.info("创建频道成功: channel={}", channel);
		}
		return created;
	}
	
	public boolean openChannel(String channel) {
		if (!checkStatus()) {
			return false;
		}
		ChannelHandler<?> handler = handlerMapping.get(channel);
		if (handler == null) {
			log.error("打开频道失败, 不是频道的创建者");
			return false;
		}
		return handler.open();
	}
	
	public void closeChannel(String channel) {
		if (!checkStatus()) {
			return;
		}
		ChannelHandler<?> handler = handlerMapping.get(channel);
		if (handler == null) {
			log.error("关闭频道失败, 不是频道的创建者");
			return;
		}
		handler.close(true);
	}
	
	public boolean register(String channel, Class<? extends Event> eventClass) {
		boolean binded = false;
		if (!checkStatus()) {
			return binded;
		}
		ChannelPublisher publisher = null;
		if (!publisherMap.containsKey(channel)) {
			RegisteRequest request = new RegisteRequest();
			request.setSignalerId(id);
			request.setChannel(channel);
			publisher = busManager.registe(request);
			boolean registered = (publisher != null);
			if (registered) {
				publisherMap.put(channel, publisher);
				log.info("注册频道成功: channel={}", channel);
			}
		}
		publisher = publisherMap.get(channel);
		if (publisher != null) {
			binded = bindEvent(channel, eventClass);
		}
		else {
			log.info("频道未注册: channel={}", channel);
		}				
		return binded;
	}
	
	private boolean bindEvent(String channel, Class<? extends Event> eventClass) {
		Set<String> channelSet = channelEventMapping.get(eventClass);
		if (channelSet == null) {
			channelEventMapping.putIfAbsent(eventClass, new HashSet<String>());
		}
		channelSet = channelEventMapping.get(eventClass);
		channelSet.add(channel);
		return true;
	}
	
	public boolean subscribe(String channel, Listener listener) {
		boolean binded = false;
		if (!checkStatus()) {
			return binded;
		}
		ChannelSubscriber subscriber = null;
		if (!subscriberMap.containsKey(channel)) {
			SubscribeRequest request = new SubscribeRequest();
			request.setSignalerId(id);
			request.setChannel(channel);
			subscriber = busManager.subscribe(request);
			boolean subscribed = (subscriber != null);
			if (subscribed) {
				subscriberMap.put(channel, subscriber);
				log.info("订阅频道成功: channel={}", channel);
			}
		}
		subscriber = subscriberMap.get(channel);
		if (subscriber != null) {
			binded = bindListener(subscriber, listener);
		}
		else {
			log.info("频道未注册: channel={}", channel);
		}
		return binded;
	}
	
	private boolean bindListener(ChannelSubscriber subscriber, Listener listener) {
		subscriber.setListener(listener);
		return true;
	}
	
	public <T> void fileEvent(Class<? extends Event> eventClass, ActionData<T> data) {
		if (!checkStatus()) {
			return;
		}
		Set<String> channelSet = channelEventMapping.get(eventClass);
		if (CollectionUtils.isNotEmpty(channelSet)) {
			Iterator<String> it = channelSet.iterator();
			while (it.hasNext()) {
				String channel = it.next();
				ChannelPublisher publisher = publisherMap.get(channel);
				if (publisher != null) {
					publisher.send(data);
				}
			}
		}
	}
	
	public <T> void fileEvent(Class<? extends Event> eventClass, String channel, ActionData<T> data) {
		if (!checkStatus()) {
			return;
		}
		Set<String> channelSet = channelEventMapping.get(eventClass);
		if (channelSet != null && channelSet.contains(channel)) {
			ChannelPublisher publisher = publisherMap.get(channel);
			if (publisher != null) {
				publisher.send(data);
			}
			else {
				log.error("Publisher失效: eventClass={}, channel={}", eventClass.getName(), channel);
			}
		}
		else {
			log.error("事件未注册到频道: eventClass={}, channel={}", eventClass.getName(), channel);
		}
	}
	
	public boolean checkStatus() {
		if (isClose) {
			log.error("BusSignaler已经关闭");
		}
		return !isClose;
	}
	
	public boolean isClose() {
		return isClose;
	}
	
	public void setClose() {
		this.isClose = true;
	}
	
	public void close() throws Exception {
		
	}
	
}
