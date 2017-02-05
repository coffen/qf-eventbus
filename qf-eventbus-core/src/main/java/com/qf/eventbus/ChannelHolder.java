package com.qf.eventbus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
	
	private final Map<String, Sender> senderMap = new HashMap<String, Sender>();
	private final Map<String, Receiver> receiverMap = new HashMap<String, Receiver>();
	
	public Map<String, Sender> getSenderMap() {
		return Collections.unmodifiableMap(senderMap);
	}
	
	public Map<String, Receiver> getReceiverMap() {
		return Collections.unmodifiableMap(receiverMap);
	}
	
	public Sender getSender(String sid) {
		return senderMap.get(sid);
	}	
	
	public void addSender(Sender sender) {
		if (sender != null && StringUtils.isNotBlank(sender.getId())) {
			senderMap.put(sender.getId(), sender);
		}
	}
	
	public Sender removeSender(String sid) {
		return senderMap.remove(sid);
	}
	
	public Receiver getReceiver(String rid) {
		return receiverMap.get(rid);
	}
	
	public void addReceiver(Receiver receiver) {
		if (receiver != null) {
			receiverMap.put(receiver.getId(), receiver);
		}
	}
	
	public Receiver removeReceiver(String rid) {
		return receiverMap.remove(rid);
	}

}
