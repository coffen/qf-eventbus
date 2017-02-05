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
 * Description: 发布者工厂
 * <br>
 * File Name: SenderFactory.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月5日 下午3:27:27 
 * @version: v1.0
 *
 */
public class SenderFactory {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final static String ID_PREFIX = "Pub-";
	
	/**
	 * 创建Sender
	 * 
	 * @param channel
	 * @return
	 */
	public Sender buildSender(String channel) {
		if (StringUtils.isBlank(channel)) {
			log.error("创建Sender失败, 频道参数为空");
			return null;
		}
		String sid = ID_PREFIX + UUID.randomUUID().toString().replace("-", "");
		return new Sender(sid, channel);
	}
	
	/**
	 * 创建Sender代理
	 * 
	 * @param sender
	 * @return
	 */
	public ChannelPublisher buildSenderProxy(final Sender sender) {
		if (sender == null) {
			log.error("创建Sender代理失败, 代理对象为空");
			return null;
		}
		Object proxy = Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { ChannelPublisher.class }, new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return method.invoke(sender, args);
			}
		});
		return (ChannelPublisher)proxy;
	}
	
}
