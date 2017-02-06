package com.qf.eventbus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 
 * <p>
 * Project Name: 买到手抽筋
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
 * @create time：2017-02-06 22:14:52 
 * @version: v1.0
 *
 */
public class BusServer {
	
	private BusManager manager = new DefaultBusManager();
	
	public BusSignaler buildSignaler() {
		BusManager proxy = buildBusManagerProxy();
		BusSignaler signaler = new BusSignaler(proxy);
		return signaler;
	}
	
	private BusManager buildBusManagerProxy() {
		return (BusManager)Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { BusManager.class }, new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return method.invoke(manager, args);
			}
		});
	}

}
