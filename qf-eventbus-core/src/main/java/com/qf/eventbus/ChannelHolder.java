package com.qf.eventbus;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 频道发布者、订阅者容器
 * <br>
 * File Name: ChannelHolder.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月2日 下午6:08:10 
 * @version: v1.0
 *
 */
public class ChannelHolder {
	
	private final ConcurrentHashMap<String, Sender> senderMap = new ConcurrentHashMap<String, Sender>();
	private final ConcurrentHashMap<String, Receiver> receiverMap = new ConcurrentHashMap<String, Receiver>();
	
	public Map<String, Sender> getSenderMap() {
		return Collections.unmodifiableMap(senderMap);
	}
	
	public Map<String, Receiver> getReceiverMap() {
		return Collections.unmodifiableMap(receiverMap);
	}
	
	public Sender getSender(String signalerId) {
		return senderMap.get(signalerId);
	}	
	
	public boolean addSender(Sender sender) {
		if (sender == null || StringUtils.isBlank(sender.getSignalerId())) {
			return false;
		}
		Sender s = senderMap.putIfAbsent(sender.getSignalerId(), sender);
		return s == sender;
	}
	
	public Sender removeSender(String signalerId) {
		return senderMap.remove(signalerId);
	}
	
	public Receiver getReceiver(String signalerId) {
		return receiverMap.get(signalerId);
	}
	
	public boolean addReceiver(Receiver receiver) {
		if (receiver == null || StringUtils.isBlank(receiver.getSignalerId())) {
			return false;
		}
		Receiver r = receiverMap.putIfAbsent(receiver.getSignalerId(), receiver);
		return r == receiver;
	}
	
	public Receiver removeReceiver(String signalerId) {
		return receiverMap.remove(signalerId);
	}

}
