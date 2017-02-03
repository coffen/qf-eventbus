package com.qf.eventbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qf.eventbus.dispatcher.MutilDispatcher;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 默认频道
 * <br>
 * File Name: DefaultChannel.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月2日 下午9:06:53 
 * @version: v1.0
 *
 */
public class DefaultChannel extends AbstractChannel {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final static int STATUS_UNSTART = 1;
	private final static int STATUS_OPEN = 1 << 1;
	private final static int STATUS_CLOSE = 1 << 2;
	
	private int status = STATUS_UNSTART;
	
	public DefaultChannel() {
		holder = new ChannelHolder();
	}
	
	public void setDispatcher(Dispatcher dispatcher) {
		if (status == STATUS_OPEN) {
			log.error("运行期不允许设置分发器");
			return;
		}
		if (dispatcher == null) {
			log.error("分发器为空, 设置失败");
			return;
		}
		this.dispatcher = dispatcher;
	}
	
	public boolean open() {
		if (status == STATUS_OPEN || status == STATUS_CLOSE) {
			log.error("频道已打开或已关闭");
			return false;
		}
		if (dispatcher == null) {
			dispatcher = new MutilDispatcher();
		}
		return true;
	}
	
	public void close(boolean igoreMsg) {
		// TODO
		holder = null;
		dispatcher = null;
	}

}