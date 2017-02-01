package com.qf.eventbus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 抽象频道
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
public abstract class AbstractChannel implements Channel {
	
	private final static String TOKEN_PUBLISHER = "Pub";
	private final static String TOKEN_SUBSCRIBER = "Sub";
	
	private String name;
	
	private Dispatcher dispatcher;
	
	private Map<String, Sender> senderMap = new HashMap<String, Sender>();
	private Map<String, Receiver> receiverMap = new HashMap<String, Receiver>();
	
	public AbstractChannel() {
		init();
	}
	
	public abstract void init();
	
	public String getName() {
		return name;
	}
	
	public Sender getSender(String pid, Class<? extends Event> eventClass) {
		Sender sender = null;
		Sender s = senderMap.get(pid);
		if (s.getEvent() == eventClass) {
			sender = s;
		}
		return sender;
	}
	
	@Override
	public Sender buildSender(Class<? extends Event> eventClass) {
		String pid = TOKEN_PUBLISHER + UUID.randomUUID().toString().replace("-", "");
		Sender sender = new Sender(pid, eventClass, getName(), dispatcher);
		senderMap.put(pid, sender);
		return sender;
	}
	
	@Override
	public Receiver buildReceiver(Listener listener) {
		String sid = TOKEN_SUBSCRIBER + UUID.randomUUID().toString().replace("-", "");
		Receiver Receiver = new Receiver(sid, getName(), listener);
		receiverMap.put(sid, Receiver);
		return Receiver;
	}

}
