package com.qf.eventbus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qf.eventbus.event.ChannelCreateEvent;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 总线管理抽象类
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
	
	private ChannelWorker worker = new ChannelWorker();
	private Class<? extends AbstractChannel> channelClass = DefaultChannel.class;
	
	protected BusSignaler mainSignaler = new BusSignaler(this);
	
	@Override
	public BusSignaler buildSignaler() {
		BusManager proxy = buildBusManagerProxy();
		BusSignaler signaler = new BusSignaler(proxy);
		return signaler;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractChannel> ChannelHandler<T> buildChannel(CreateRequest request) {
		ChannelHandler<T> handler = null;
		if (request == null || StringUtils.isBlank(request.getChannel())) {
			log.error("创建频道错误, 参数为空: request={}", request);
			return handler;
		}
		try {
			AbstractChannel t = worker.build(request.getChannel(), channelClass);
			if (t != null) {
				handler = buildChannelHandlerProxy((T)t);
			}
			if (handler != null && mainSignaler != null) {
				mainSignaler.fileEvent(ChannelCreateEvent.class, new ActionData<CreateRequest>(request));
			}
		}
		catch (Exception e) {
			log.error("创建频道失败", e);
		}
		return handler;
	}
	
	public ChannelPublisher registe(RegisteRequest request) {
		ChannelPublisher publisher = null;
		if (request == null || StringUtils.isBlank(request.getChannel())) {
			log.error("绑定频道错误, 参数为空: request={}", request);
			return publisher;
		}
		Channel chl = worker.getChannel(request.getChannel());
		if (chl == null) {
			log.error("绑定频道错误, 频道{}不存在", request.getChannel());
			return publisher;
		}
		return chl.registe(request);
	}
	
	public ChannelSubscriber subscribe(SubscribeRequest request) {
		ChannelSubscriber subscriber = null;
		if (request == null || StringUtils.isBlank(request.getChannel())) {
			log.error("订阅频道错误, 参数为空: request={}", request);
			return subscriber;
		}
		Channel chl = worker.getChannel(request.getChannel());
		if (chl == null) {
			log.error("订阅频道错误, 频道{}不存在", request.getChannel());
			return subscriber;
		}
		return chl.subscribe(request);
	}
	
	public List<String> getChannelList() {
		return Collections.unmodifiableList(worker.getChannelList());
	}
	
	/**
	 * 创建BusSignaler代理
	 * 
	 * @return
	 */
	private BusManager buildBusManagerProxy() {
		final BusManager manager = this;
		return (BusManager)Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { BusManager.class }, new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return method.invoke(manager, args);
			}
		});
	}
	
	/**
	 * 创建频道工具代理
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T extends AbstractChannel> ChannelHandler<T> buildChannelHandlerProxy(final T channel) {
		if (channel != null) {
			return (ChannelHandler<T>)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { ChannelHandler.class }, new InvocationHandler() {
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					return method.invoke(channel, args);
				}
			});
		}
		return null;
	}
	
}
