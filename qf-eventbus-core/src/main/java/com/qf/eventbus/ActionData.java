package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 消息主体
 * <br>
 * File Name: ActionData.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年1月25日 下午8:34:53 
 * @version: v1.0
 *
 */
public interface ActionData<T> {
	
	/**
	 * 获取频道
	 * 
	 * @return
	 */
	public String getChannel();
	
	/**
	 * 获取发送器Id
	 * 
	 * @return
	 */
	public String getRegisterId();
	
	/**
	 * 获取数据
	 * 
	 * @return
	 */
	public T getData();
	
}
