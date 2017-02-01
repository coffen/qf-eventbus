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
	 * 获取发送器序列号
	 * 
	 * @return
	 */
	public String getSenderId();
	
	/**
	 * 获取事件类型
	 * 
	 * @return
	 */
	public Class<? extends Event> getEvent();
	
	/**
	 * 获取数据
	 * 
	 * @return
	 */
	public T getData();
	
}
