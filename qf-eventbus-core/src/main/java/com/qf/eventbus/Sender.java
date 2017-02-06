package com.qf.eventbus;

import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 消息发布器
 * <br>
 * File Name: Sender.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月5日 下午3:46:06 
 * @version: v1.0
 *
 */
public class Sender extends AbstractChannelRegistry implements ChannelPublisher {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private AbstractChannel channel;
	
	private ReentrantLock lock = new ReentrantLock();

	public Sender(String signalerId, String channel) {
		super(signalerId, channel);
	}
	
	public void setChannel(AbstractChannel channel) {
		this.channel = channel;
	}

	public boolean unRegister() {
		if (isValid()) {
			lock.lock();
			if (isValid()) {
				boolean result = channel.unRegiste(getSignalerId());
				if (result) {
					setValid(false);
					log.info("注销发布器成功: sid={}", getSignalerId());
				}
			}
			lock.unlock();
		}
		return !isValid();
	}

	public <T> void send(ActionData<T> data) {
		if (!isValid()) {
			log.error("发送消息失败, 当前频道注册已失效");
			return;
		}
		channel.send(data);
	}
	
	public void destroy() {}

}
