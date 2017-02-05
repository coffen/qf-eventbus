package com.qf.eventbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private final static int STATUS_RUNNING = 1 << 1;
	private final static int STATUS_CLOSE = 1 << 2;
	
	private int status = STATUS_UNSTART;
	
	public void setDispatcher(Dispatcher.Type type) {
		if (status == STATUS_RUNNING) {
			log.error("运行期不允许设置分发器");
			return;
		}
		if (dispatcher == null) {
			log.error("分发器为空, 设置失败");
			return;
		}
		buildDispatcher(type);
	}
	
	public void open() {
		if (status == STATUS_RUNNING || status == STATUS_CLOSE) {
			log.error("频道已打开或已关闭");
			return;
		}
		if (dispatcher == null) {
			buildDispatcher(null);
		}
		status = STATUS_RUNNING;
	}
	
	public void close(boolean igoreMsg) {
		status = STATUS_CLOSE;
	}
	
	@Override
	public boolean isOpen() {
		return status == STATUS_RUNNING;
	}

	@Override
	public boolean checkRegistryInfo(RegistryInfo info) {
		return true;
	}

}
