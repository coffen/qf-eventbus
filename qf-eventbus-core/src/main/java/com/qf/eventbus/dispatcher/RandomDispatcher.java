package com.qf.eventbus.dispatcher;

import com.qf.eventbus.AbstractChannel;
import com.qf.eventbus.ActionData;
import com.qf.eventbus.Dispatcher;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 随机转发器
 * <br>
 * File Name: RandomDispatcher.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月1日 下午7:14:28 
 * @version: v1.0
 *
 */
public class RandomDispatcher extends Dispatcher {
	
	public RandomDispatcher(AbstractChannel channel) {
		super(channel);
	}

	public <T> void dispatch(ActionData<T> data) {
		
	}

}
