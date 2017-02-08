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
 * Description: 监听器Anno
 * <br>
 * File Name: Listener.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月8日 上午11:30:29 
 * @version: v1.0
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Listener {
	
	/**
	 * 订阅频道, 未设置时如果Subscriber也未设置默认事件, 则该拦截器无效
	 * 
	 * <p>方法参数只有一个且类型必须为ActionData
	 * 
	 * @return
	 */
    String[] channel() default "";
	
}
