package com.qf.eventbus;

import java.io.Serializable;

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
public class ActionData<T> implements Serializable {

	private static final long serialVersionUID = -2434769641025313911L;
	
	private String registerId;
	private String channel;
	
	private T data;
	
	public ActionData(T data) {
		this.data = data;
	}
	
	/**
	 * 设置发布器Id
	 */
	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}
	
	/**
	 * 获取发送器Id
	 * 
	 * @return
	 */
	public String getRegisterId() {
		return registerId;
	}
	
	/**
	 * 设置频道
	 * 
	 * @param channel
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	/**
	 * 获取频道
	 * 
	 * @return
	 */
	public String getChannel() {
		return channel;
	}
	
	/**
	 * 获取数据
	 * 
	 * @return
	 */
	public T getData() {
		return data;
	}
	
}
