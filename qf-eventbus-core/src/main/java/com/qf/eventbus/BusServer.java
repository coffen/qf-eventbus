package com.qf.eventbus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 总线服务器
 * <br>
 * File Name: BusServer.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月6日 下午22:14:52
 * @version: v1.0
 *
 */
public class BusServer {
	
	private static BusManager manager = new DefaultBusManager();
	
	public static BusSignaler buildSignaler() {
		BusManager proxy = buildBusManagerProxy();
		BusSignaler signaler = new BusSignaler(proxy);
		return signaler;
	}
	
	private static BusManager buildBusManagerProxy() {
		return (BusManager)Proxy.newProxyInstance(BusServer.class.getClassLoader(), new Class<?>[] { BusManager.class }, new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return method.invoke(manager, args);
			}
		});
	}

}
