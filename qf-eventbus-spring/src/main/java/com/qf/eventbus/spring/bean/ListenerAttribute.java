package com.qf.eventbus.spring.bean;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 监听器注释属性
 * <br>
 * File Name: ListenerAttribute.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月15日 下午2:33:28 
 * @version: v1.0
 *
 */
public class ListenerAttribute {
	
	private final static String BEAN_CLAZZ_NAME_PREFIX = "Listener@";
	
	private Class<?> targetClass;
	private String methodName;
	private Class<?>[] methodParameterTypes;
	
	private List<String> channelList;

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

	public List<String> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<String> channelList) {
		this.channelList = channelList;
	}	
	
	public boolean equals(Object obj) {
		if (targetClass == null || StringUtils.isEmpty(methodName) || !(obj instanceof InterceptorAttribute)) {
			return false;
		}
		InterceptorAttribute attribute = (InterceptorAttribute)obj;
		if ((targetClass != attribute.getTargetClass()) || !(methodName.equals(attribute.getMethodName()))) {
			return false;
		}
		Class<?>[] clazzArr1 = methodParameterTypes == null ? new Class<?>[0] : methodParameterTypes;
		Class<?>[] clazzArr2 = attribute.getMethodParameterTypes() == null ? new Class<?>[0] : attribute.getMethodParameterTypes();
		return Arrays.deepEquals(clazzArr1, clazzArr2);
	}
	
	public String getBeanClazzName() {
		if (targetClass == null) {
			return null;
		}
		return BEAN_CLAZZ_NAME_PREFIX + targetClass.getName();
	}
	
	public static String getBeanClazzName(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		return BEAN_CLAZZ_NAME_PREFIX + clazz.getName();
	}

}
