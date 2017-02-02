package com.qf.eventbus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 频道容器
 * <br>
 * File Name: ChannelDataHolder.java
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
public class ChannelDataHolder {
	
	private final Map<String, Sender> senderMap = new HashMap<String, Sender>();
	private final Map<String, Receiver> receiverMap = new HashMap<String, Receiver>();
	
	public Map<String, Sender> getSenderMap() {
		return Collections.unmodifiableMap(senderMap);
	}
	
	public Map<String, Receiver> getReceiverMap() {
		return Collections.unmodifiableMap(receiverMap);
	}
	
	public Sender getSender(String uuid) {
		return senderMap.get(uuid);
	}	
	
	public void addSender(Sender sender) {
		if (sender != null) {
			senderMap.put(sender.getPid(), sender);
		}
	}
	
	public Receiver getReceiver(String uuid) {
		return receiverMap.get(uuid);
	}	
	
	public void addReceiver(Receiver receiver) {
		if (receiver != null) {
			receiverMap.put(receiver.getSid(), receiver);
		}
	}

}
