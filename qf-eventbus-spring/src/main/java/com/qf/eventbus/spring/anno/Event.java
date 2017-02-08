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
 * Description: 事件Anno
 * <br>
 * File Name: Event.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月8日 上午10:31:20 
 * @version: v1.0
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Event {
	
	/**
	 * 事件名称, 若未设置则以当前类名作为事件名称, 有重复事件名Spring解析报错
	 * 
	 * @return
	 */
    String name() default "";
    
	/**
	 * 绑定频道列表, 必须设置
	 * 
	 * @return
	 */
    String[] channel();

}
