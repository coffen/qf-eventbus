package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 请求接口
 * <br>
 * File Name: Request.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月6日 上午11:51:15 
 * @version: v1.0
 *
 */
public interface Request {
	
	/**
	 * 获取请求的信号员ID
	 * 
	 * @return
	 */
	public String getSignalerId();
	
	/**
	 * 获取操作的频道
	 * 
	 * @return
	 */
	public String getChannel();

}
