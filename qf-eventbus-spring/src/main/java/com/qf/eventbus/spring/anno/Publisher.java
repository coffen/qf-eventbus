package com.qf.eventbus.spring.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 发布者Anno
 * <br>
 * File Name: Publisher.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月8日 上午10:32:36 
 * @version: v1.0
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Publisher {
	
	/**
	 * 默认发起事件列表, 如设置了事件则下属的所有Interceptor均注册该事件列表
	 * 
	 * @return
	 */
	String[] event() default "";

}
