package com.qf.eventbus;

import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 消息收听器
 * <br>
 * File Name: Receiver.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月5日 下午3:47:53 
 * @version: v1.0
 *
 */
public class Receiver extends AbstractChannelRegistry implements ChannelSubscriber {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private AbstractChannel channel;
	private Listener listener;
	
	private ReentrantLock lock = new ReentrantLock();
	
	public Receiver(String signalerId, String channel) {
		super(signalerId, channel);
	}
	
	public void setChannel(AbstractChannel channel) {
		this.channel = channel;
	}
	
	@Override
	public void setListener(Listener listener) {
		this.listener = listener;		
	}

	public boolean unSubscribe() {
		if (isValid()) {
			lock.lock();
			if (isValid()) {
				boolean result = channel.unRegiste(getSignalerId());
				if (result) {
					setValid(false);
					log.info("取消订阅成功: rid={}", getSignalerId());
				}
			}
			lock.unlock();
		}
		return !isValid();
	}

	public <T> void receive(ActionData<T> data) {
		listener.onEvent(data);
	}
	
	@Override
	public void destroy() {}

}
