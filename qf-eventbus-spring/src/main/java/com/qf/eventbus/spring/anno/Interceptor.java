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
 * Description: 拦截器Anno
 * <br>
 * File Name: Interceptor.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月8日 上午10:31:58 
 * @version: v1.0
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Interceptor {
	
	/**
	 * 发起事件列表, 未设置时如果Publisher也未设置默认事件, 则该拦截器无效
	 * 
	 * @return
	 */
	String[] event() default "";
	
	/**
	 * 拦截类型（即拦截的Pointcut）
	 * 
	 * <p>type=InterceptType.PARAMETER_BEFORE, 在方法执行前异步执行
	 * <p>type=InterceptType.PARAMETER_AFTER,  在方法执行后异步执行
	 * <p>type=InterceptType.RETURNING, 在方法执行后异步执行
	 * <p>type=InterceptType.ON_EXCEPTION, 在方法抛出异常时异步执行
	 * 
	 * @return
	 */
	InterceptType type() default InterceptType.RETURNING;
	
	/**
	 * 拦截Spel表达式, 表达式解析结果作为ActionData发布
	 * 
	 * <p>type=InterceptType.PARAMETER_BEFORE,InterceptType.PARAMETER_AFTER, 如未指定表达式, 参数数组即发布数据
	 * <p>type=InterceptType.RETURNING,    如未指定表达式, 返回对象即发布数据（返回对象为空不处理）
	 * <p>type=InterceptType.ON_EXCEPTION, 表达式无效, 抛出异常即发布数据
	 * 
	 * @return
	 */
	String expr() default "";
	
}
