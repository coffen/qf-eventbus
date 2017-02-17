package com.qf.eventbus.spring.bean;

import java.util.List;

import com.qf.eventbus.spring.anno.InterceptType;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 拦截器注释属性
 * <br>
 * File Name: InterceptorAttribute.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月15日 上午11:48:39 
 * @version: v1.0
 *
 */
public class InterceptorAttribute {

	private Class<?> targetClass;
	private String methodName;
	private Class<?>[] methodParameterTypes;
	private String[] methodparameterNames;
	
	private List<String> eventList;
	private InterceptType interceptType;
	private String expression;
	
	public InterceptorAttribute(Class<?> targetClass, String methodName, Class<?>[] methodParameterTypes) {
		this.targetClass = targetClass;
		this.methodName = methodName;
		this.methodParameterTypes = methodParameterTypes;
	}
	
	public Class<?> getTargetClass() {
		return targetClass;
	}
	
	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public Class<?>[] getMethodParameterTypes() {
		return methodParameterTypes;
	}
	
	public void setMethodParameterTypes(Class<?>[] methodParameterTypes) {
		this.methodParameterTypes = methodParameterTypes;
	}
	
	public String[] getMethodparameterNames() {
		return methodparameterNames;
	}
	
	public void setMethodparameterNames(String[] methodparameterNames) {
		this.methodparameterNames = methodparameterNames;
	}
	
	public List<String> getEventList() {
		return eventList;
	}

	public void setEventList(List<String> eventList) {
		this.eventList = eventList;
	}

	public InterceptType getInterceptType() {
		return interceptType;
	}

	public void setInterceptType(InterceptType interceptType) {
		this.interceptType = interceptType;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

}
