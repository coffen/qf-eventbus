package com.qf.eventbus;

import org.apache.commons.lang3.StringUtils;
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
			log.error("设置分发器失败, 运行期不允许设置分发器");
			return;
		}
		if (type == null) {
			log.error("设置分发器失败, 分发器为空");
			return;
		}
		buildDispatcher(type);
		log.info("频道设置分发器成功: channel={}, dispatcherType={}", getName(), type);
	}
	
	public void open() {
		if (status == STATUS_RUNNING || status == STATUS_CLOSE) {
			log.error("频道打开失败, 频道已打开或已关闭");
			return;
		}
		if (dispatcher == null) {
			buildDispatcher(null);
		}
		status = STATUS_RUNNING;
		log.info("频道成功打开: channel={}", getName());
	}
	
	public void close(boolean igoreMsg) {
		status = STATUS_CLOSE;
		log.info("频道已关闭: channel={}", getName());
	}
	
	@Override
	public boolean isOpen() {
		return status == STATUS_RUNNING;
	}

	@Override
	public <T extends Request> boolean checkRequest(T request) {
		return request != null && StringUtils.isNotBlank(request.getChannel());
	}

}
