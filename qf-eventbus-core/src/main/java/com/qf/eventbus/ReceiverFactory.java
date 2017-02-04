package com.qf.eventbus;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: Receiver工厂
 * <br>
 * File Name: ReceiverFactory.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月4日 下午12:51:48 
 * @version: v1.0
 *
 */
public class ReceiverFactory {
	
	public final static String TOKEN_RECEIVER = "Rid-";
	
	private final Logger log = LoggerFactory.getLogger(ReceiverFactory.class);
	
	@SuppressWarnings("unchecked")
	public <T extends Receiver> T create(Class<T> clazz, String channel) {
		if (clazz == null || StringUtils.isBlank(channel)) {
			log.error("创建ReceiverFactory失败, 参数为空: clazz={}, channel={}", clazz, channel);
			return null;
		}
		String rid = TOKEN_RECEIVER + UUID.randomUUID().toString().replace("-", "");
		Receiver r = new SimpleReceiver(rid, channel);
		return (T)r;
	}
	
}
