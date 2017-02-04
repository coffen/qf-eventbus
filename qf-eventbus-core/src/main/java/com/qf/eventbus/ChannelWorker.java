package com.qf.eventbus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 频道工厂
 * <br>
 * File Name: ChannelWorker.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年1月31日 下午7:58:04 
 * @version: v1.0
 *
 */
public class ChannelWorker {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();
	
	/**
	 * 根据频道名称和类别创建频道, 配置默认工厂
	 * 
	 * @param channelName
	 * @param channelClazz
	 * @return
	 */
	public <T extends AbstractChannel> T build(String channelName, Class<T> channelClazz) {
		return build(channelName, channelClazz, null, null);
	}
	
	/**
	 * 根据频道名称和类别创建频道, 设置Sender工厂和Receiver工厂
	 * 
	 * <p>如果名称已经存在且类别相同则返回对应的频道, 如果名称存在但类型不同, 则返回null
	 * 
	 * @param channelName
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractChannel> T build(String channelName, Class<T> channelClazz, SenderFactory sf, ReceiverFactory rf) {
		T channel = null;
		if (StringUtils.isBlank(channelName)) {
			log.error("创建频道失败, 频道名称为空");
			return channel;
		}
		if (channelClazz == null) {
			log.error("创建频道失败, 未指定频道类型");
			return channel;
		}
		try {
			Channel chl = channelMap.putIfAbsent(channelName, channelClazz.newInstance());
			if (chl != null && chl.getClass() == channelClazz) {
				chl.setSenderFactory(sf == null ? new SenderFactory() : sf);
				chl.setReceiverFactory(rf == null ? new ReceiverFactory() : rf);
				channel = (T)chl;
			}
		}
		catch (Exception e) {
			log.error("创建频道失败, 初始化频道类失败", e);
		}
		return channel;
	}
	
	/**
	 * 根据频道名称获取频道
	 * 
	 * @param channelName
	 * @return
	 */
	public Channel getChannel(String channelName) {
		return channelMap.get(channelName);
	}
	
	/**
	 * 获取全部频道
	 * 
	 * @return
	 */
	public List<String> getChannelList() {
		List<String> channelList = new ArrayList<>();
		for (Channel chl : channelMap.values()) {
			channelList.add(chl.getName());
		}
		return channelList;
	}

}
