package com.qf.eventbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractIdleService;

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
public class BusServer extends AbstractIdleService {
	
	private final static Logger log = LoggerFactory.getLogger(BusServer.class);
	
	private final BusManager manager = new DefaultBusManager();
	
	protected void startUp() throws Exception {
		log.error("总线服务开启中...");
		
		log.error("总线服务已成功开启");
	}

	protected void shutDown() throws Exception {
		log.error("总线服务已关闭");
	}
	
	public BusSignaler buildSignaler() {
		return manager.buildSignaler();
	}

}
