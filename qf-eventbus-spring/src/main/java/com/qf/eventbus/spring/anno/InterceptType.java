package com.qf.eventbus.spring.anno;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 事件发布器方法拦截类型
 * <br>
 * File Name: InterceptType.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月8日 上午10:48:14 
 * @version: v1.0
 *
 */
public enum InterceptType {
	
	PARAMETER_BEFORE,	// 方法处理前的参数
	PARAMETER_AFTER,	// 方法处理后的参数
	RETURNING,			// 方法返回对象
	ON_EXCEPTION		// 异常抛出时
	
}
