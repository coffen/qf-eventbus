package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 频道处理工具
 * <br>
 * File Name: ChannelHandler.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月2日 下午10:38:55 
 * @version: v1.0
 *
 */
public interface ChannelHandler<T extends AbstractTopicChannel> {
	
	/**
	 * 开启频道
	 * 
	 * @return
	 */
	public boolean open();
	
	/**
	 * 关闭频道
	 * 
	 * @param igoreMsg 是否忽略已提交的消息
	 */
	public void close(boolean igoreMsg);
	
}
