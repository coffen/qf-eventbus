package com.qf.eventbus;

import com.qf.eventbus.event.ChannelCreateEvent;
import com.qf.eventbus.event.RegisteEvent;
import com.qf.eventbus.event.SubscribeEvent;
import com.qf.eventbus.event.UnregisteEvent;
import com.qf.eventbus.event.UnsubscribeEvent;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 默认总线管理器
 * <br>
 * File Name: DefaultBusManager.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017-02-06 22:10:10 
 * @version: v1.0
 *
 */
public class DefaultBusManager extends AbstractBusManager {
	
	private final String innerChannel = "_bus";
	private final BusSignaler mainSignaler = new BusSignaler(this);
	
	public DefaultBusManager() {
		registerInnerEvent();
	}

	@Override
	public <T> void onEvent(ActionData<T> data) {
		
	}
	
	private void registerInnerEvent() {
		mainSignaler.buildChannel(innerChannel);
		
		mainSignaler.register(innerChannel, ChannelCreateEvent.class);
		mainSignaler.register(innerChannel, RegisteEvent.class);
		mainSignaler.register(innerChannel, UnregisteEvent.class);
		mainSignaler.register(innerChannel, SubscribeEvent.class);
		mainSignaler.register(innerChannel, UnsubscribeEvent.class);
	}

}
