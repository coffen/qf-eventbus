package com.qf.eventbus;

import java.util.List;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 总线管理接口
 * <br>
 * File Name: BusManager.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年1月31日 下午7:39:47 
 * @version: v1.0
 *
 */
public interface BusManager {
	
	/**
	 * 创建频道
	 * 
	 * @param name
	 * @return
	 */
	public <T extends AbstractChannel> ChannelHandler<T> buildChannel(String name);
	
	/**
	 * 查询全部频道列表
	 * 
	 * @return
	 */
	public List<String> getChannelList();
	
}
