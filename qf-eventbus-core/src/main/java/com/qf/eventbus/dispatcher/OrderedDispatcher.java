package com.qf.eventbus.dispatcher;

import com.qf.eventbus.AbstractChannel;
import com.qf.eventbus.ActionData;
import com.qf.eventbus.Dispatcher;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 顺序分发器
 * <br>
 * File Name: OrderedDispatcher.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月1日 上午11:34:16 
 * @version: v1.0
 *
 */
public class OrderedDispatcher extends Dispatcher {
	
	public OrderedDispatcher(AbstractChannel channel) {
		super(channel);
	}

	public <T> void dispatch(ActionData<T> data) {
		
	}

}
