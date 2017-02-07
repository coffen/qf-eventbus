package com.qf.eventbus.dispatcher;

import java.util.Collection;

import com.qf.eventbus.AbstractChannel;
import com.qf.eventbus.ActionData;
import com.qf.eventbus.Dispatcher;
import com.qf.eventbus.Executor;
import com.qf.eventbus.Receiver;
import com.qf.eventbus.executor.SingleExecutor;
import com.qf.eventbus.thread.ReceiveRunnable;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 多播分发器
 * <br>
 * File Name: MutilDispatcher.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月1日 下午7:13:36 
 * @version: v1.0
 *
 */
public class MutilDispatcher extends Dispatcher {
	
	private Executor receiveExecutor;
	
	public MutilDispatcher(AbstractChannel channel) {
		super(channel);
		
		this.receiveExecutor = new SingleExecutor();
	}

	public <T> void dispatch(ActionData<T> data) {
		Collection<Receiver> col = getHolder().getReceiverMap().values();
		if (col.size() > 0) {
			for (Receiver r : col) {
				if (r.isValid()) {
					receiveExecutor.submit(new ReceiveRunnable<>(r, data));
				}
			}
		}
		else {
			//TODO 没有任何订阅者的情况, 应阻塞线程, 并将data重新放入线程池中, 订阅动作唤醒线程
		}
	}

}
