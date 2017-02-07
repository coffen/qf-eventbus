package com.qf.eventbus;

import java.io.Serializable;
import java.util.UUID;

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
	
	private final String sequence = UUID.randomUUID().toString().replace("-", "");
	
	private ThreadLocal<String> registerId;
	private ThreadLocal<String> channel;
	
	private ThreadLocal<T> data;
	
	public ActionData(final T data) {
		this.data = new ThreadLocal<T>() {
			protected T initialValue() {
				return data;
			}
		};
	}
	
	/**
	 * 设置发布器Id
	 */
	public void setRegisterId(final String registerId) {
		if (this.registerId == null) {
			this.registerId = new ThreadLocal<String>() {
				protected String initialValue() {
					return registerId;
				}
			};
		}
		else {
			this.registerId.set(registerId);
		}
	}
	
	/**
	 * 获取发送器Id
	 * 
	 * @return
	 */
	public String getRegisterId() {
		return this.registerId.get();
	}
	
	/**
	 * 设置频道
	 * 
	 * @param channel
	 */
	public void setChannel(final String channel) {
		if (this.channel == null) {
			this.channel = new ThreadLocal<String>() {
				protected String initialValue() {
					return channel;
				}
			};
		}
		else {
			this.channel.set(channel);
		}
	}
	
	/**
	 * 获取频道
	 * 
	 * @return
	 */
	public String getChannel() {
		return this.channel.get();
	}
	
	/**
	 * 获取消息序列号
	 * 
	 * @return
	 */
	public String getSequence() {
		return this.sequence;
	}
	
	/**
	 * 获取数据
	 * 
	 * @return
	 */
	public T getData() {
		return this.data.get();
	}
	
}
