package com.qf.eventbus;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 
 * <br>
 * File Name: AbstractBusManager.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年1月31日 下午10:27:36 
 * @version: v1.0
 *
 */
public abstract class AbstractBusManager implements BusManager, Listener {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ChannelWorker worker;
	
	@Override
	public <T extends TopicChannel> T buildChannel(String name, Class<T> clazz) {
		return worker.build(name, clazz);
	}
	
	@Override
	public Sender bindEvent(Class<? extends Event> eventClass, String channel) {
		TopicChannel chl = worker.getChannel(channel);
		if (chl == null) {
			log.error("绑定频道错误, 频道{}不存在", channel);
			return null;
		}
		return chl.buildSender(eventClass);
	}
	
	@Override
	public void unbindEvent(String sid, String channel) {
		TopicChannel chl = worker.getChannel(channel);
		if (chl == null) {
			log.error("取消绑定频道错误, 频道{}不存在", channel);
			return;
		}
		Sender sender = chl.getSender(sid);
		if (sender != null) {
			chl.cancelSender(sid);
		}
	}
	
	@Override
	public Receiver subscribe(String channel) {
		TopicChannel chl = worker.getChannel(channel);
		if (chl == null) {
			log.error("订阅频道错误, 频道{}不存在", channel);
			return null;
		}
		return chl.buildReceiver();
	}
	
	@Override
	public void unSubscribe(String rid, String channel) {
		TopicChannel chl = worker.getChannel(channel);
		if (chl == null) {
			log.error("取消订阅频道错误, 频道{}不存在", channel);
			return;
		}
		chl.cancelReceiver(rid);
	}
	
	@Override
	public List<String> getChannelList() {
		return Collections.unmodifiableList(worker.getChannelList());
	}
	
}
