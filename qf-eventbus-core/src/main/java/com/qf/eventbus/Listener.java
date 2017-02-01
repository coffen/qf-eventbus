package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 消息监听器
 * <br>
 * File Name: Listener.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年1月25日 下午8:32:51 
 * @version: v1.0
 *
 */
public interface Listener {
	
	/**
	 * 消息处理
	 * 
	 * @param data
	 */
	public <T> void onEvent(ActionData<T> data);

}
