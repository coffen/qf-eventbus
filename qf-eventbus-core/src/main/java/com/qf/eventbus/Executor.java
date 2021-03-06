package com.qf.eventbus;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 线程池
 * <br>
 * File Name: Executor.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月2日 下午4:52:28 
 * @version: v1.0
 *
 */
public interface Executor {
	
	/**
	 * 提交线程
	 * 
	 * @param runnable
	 */
	public <T> void submit(Runnable runnable);
	
	/**
	 * 获取剩余的任务
	 * 
	 * @return int
	 */
	public int getAvailableTask();
	
	/**
	 * 关闭线程池
	 */
	public void shutDown();
	
}
