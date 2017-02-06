package com.qf.eventbus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
public class BusSignaler {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final static String ID_PREFIX = "Sig-";
	
	private final String id = ID_PREFIX + UUID.randomUUID().toString().replace("-", "");
	
	// private final ConcurrentHashMap<String, SenderBuilder> senderMap = new ConcurrentHashMap<String, SenderBuilder>();
	// private final ConcurrentHashMap<String, ReceiverBuilder> receiverMap = new ConcurrentHashMap<String, ReceiverBuilder>();
	
	private final Map<Class<? extends Event>, Set<String>> channelEventMapping = new HashMap<Class<? extends Event>, Set<String>>();
	
	private final Map<String, ChannelHandler<? extends AbstractChannel>> handlerMapping = new HashMap<String, ChannelHandler<? extends AbstractChannel>>();
	
	private AbstractBusManager busManager;
	
	public BusSignaler(AbstractBusManager busManager) {
		this.busManager = busManager;
	}
	
	public String getSignalerId() {
		return id;
	}
	
	public boolean buildChannel(String channel) {
		CreateRequest request = new CreateRequest();
		request.setSignalerId(id);
		request.setChannel(channel);
		ChannelHandler<AbstractChannel> handler = busManager.buildChannel(request);
		boolean created = (handler != null);
		if (created) {
			handlerMapping.put(channel, handler);
		}
		return created;
	}
	
	public boolean register(String channel) {
		
	}
	
	public boolean subscribe(String channel) {
		
	}
	
}
