package com.qf.eventbus.thread;

import com.qf.eventbus.ActionData;
import com.qf.eventbus.Dispatcher;

/**
 * 
 * <p>
 * Project Name: 买到手抽筋
 * <br>
 * Description: 消息分发线程
 * <br>
 * File Name: DispatchRunnable.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017-02-07 22:07:14 
 * @version: v1.0
 *
 */
public class DispatchRunnable<T> implements Runnable {
	
	private Dispatcher disPatcher;	
	private ActionData<T> data;
	
	public DispatchRunnable(Dispatcher disPatcher, ActionData<T> data) {
		this.disPatcher = disPatcher;
		this.data = data;
	}

	public void run() {
		if (disPatcher != null && data != null) {
			disPatcher.dispatch(data);
		}
	}

}
