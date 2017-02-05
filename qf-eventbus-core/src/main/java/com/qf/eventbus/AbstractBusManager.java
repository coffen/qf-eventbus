package com.qf.eventbus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractChannel> ChannelHandler<T> buildChannel(String name) {
		ChannelHandler<T> handler = null;
		try {
			AbstractChannel t = worker.build(name, channelClass);
			if (t != null) {
				handler = buildChannelHandlerProxy((T)t);
			}
		}
		catch (Exception e) {
			log.error("创建频道失败", e);
		}
		return handler;
	}
	
	public List<String> getChannelList() {
		return Collections.unmodifiableList(worker.getChannelList());
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
