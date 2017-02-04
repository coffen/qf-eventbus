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
 * Description: Sender工厂
 * <br>
 * File Name: SenderFactory.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月4日 上午11:20:22 
 * @version: v1.0
 *
 */
public class SenderFactory {
	
	public final static String TOKEN_SENDER = "Sid-";
	
	private final Logger log = LoggerFactory.getLogger(SenderFactory.class);
	
	@SuppressWarnings("unchecked")
	public <T extends Sender> T create(Class<T> clazz, String channel, final Dispatcher dispatcher) {
		if (clazz == null || StringUtils.isBlank(channel) || dispatcher == null) {
			log.error("创建Sender失败, 参数为空: clazz={}, channel={}, dispatcher={}", clazz, channel, dispatcher);
			return null;
		}
		String sid = TOKEN_SENDER + UUID.randomUUID().toString().replace("-", "");
		final Sender s = new SimpleSender(sid, channel);
		Object proxy = Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { Sender.class }, new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if (method.getName().equalsIgnoreCase("send") && args != null && args.length > 0 && args[0] != null) {
					ActionData<?> data = (ActionData<?>)args[0];
					dispatcher.dispatch(data);
				}
				return method.invoke(s, args);
			}
		});
		return (T)proxy;
	}

}
