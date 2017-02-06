package com.qf.eventbus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 订阅者工厂
 * <br>
 * File Name: ReceiverFactory.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月5日 下午7:07:00 
 * @version: v1.0
 *
 */
public class ReceiverFactory {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final static String ID_PREFIX = "Sub-";
	
	/**
	 * 创建Receiver
	 * 
	 * @param channel
	 * @return
	 */
	public Receiver buildReceiver(String channel) {
		if (StringUtils.isBlank(channel)) {
			log.error("创建订阅者失败, 频道参数为空");
			return null;
		}
		String sid = ID_PREFIX + UUID.randomUUID().toString().replace("-", "");
		return new Receiver(sid, channel);
	}
	
	/**
	 * 创建Receiver代理
	 * 
	 * @param Receiver
	 * @return
	 */
	public ChannelSubscriber buildReceiverProxy(final Receiver Receiver) {
		if (Receiver == null) {
			log.error("创建订阅者代理失败, 代理对象为空");
			return null;
		}
		Object proxy = Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { ChannelSubscriber.class }, new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return method.invoke(Receiver, args);
			}
		});
		return (ChannelSubscriber)proxy;
	}

}
