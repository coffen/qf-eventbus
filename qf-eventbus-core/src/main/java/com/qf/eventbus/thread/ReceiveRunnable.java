package com.qf.eventbus.thread;

import com.qf.eventbus.ActionData;
import com.qf.eventbus.Receiver;

/**
 * 
 * <p>
 * Project Name: 买到手抽筋
 * <br>
 * Description: 消息处理线程
 * <br>
 * File Name: ReceiveRunnable.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017-02-07 22:23:00 
 * @version: v1.0
 *
 */
public class ReceiveRunnable<T> implements Runnable {
	
	private Receiver receiver;	
	private ActionData<T> data;
	
	public ReceiveRunnable(Receiver receiver, ActionData<T> data) {
		this.receiver = receiver;
		this.data = data;
	}

	public void run() {
		if (receiver != null && data != null) {
			receiver.receive(data);
		}
	}
}
