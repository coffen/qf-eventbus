package com.qf.eventbus;

import com.qf.eventbus.event.ChannelCloseEvent;
import com.qf.eventbus.event.ChannelCreateEvent;
import com.qf.eventbus.event.ChannelOpenEvent;

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
	
	public DefaultBusManager() {
		initMainSignaler();
	}
	
	private void initMainSignaler() {
		boolean created = mainSignaler.buildChannel(innerChannel, Dispatcher.Type.MUTIL);
		if (created) {
			mainSignaler.openChannel(innerChannel);
			
			mainSignaler.register(innerChannel, ChannelCreateEvent.class);
			mainSignaler.register(innerChannel, ChannelOpenEvent.class);
			mainSignaler.register(innerChannel, ChannelCloseEvent.class);
		}
	}

	@Override
	public <T> void onEvent(ActionData<T> data) {
		
	}

}
