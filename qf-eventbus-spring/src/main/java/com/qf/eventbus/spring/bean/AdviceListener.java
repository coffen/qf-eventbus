package com.qf.eventbus.spring.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import com.qf.eventbus.ActionData;
import com.qf.eventbus.Listener;

public class AdviceListener implements Listener {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public AdviceListener(Class<?> targetClazz, String methodName, Class<?>[] methodParameterTypes) {
		this.targetClazz = targetClazz;
		this.methodName = methodName;
		this.methodParameterTypes = methodParameterTypes;
	}
	
	private Class<?> targetClazz;
	private String methodName;
	private Class<?>[] methodParameterTypes;
	
	private Object targetProxy;
	private Method method;
	
	public Class<?> getTargetClazz() {
		return targetClazz;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public Class<?>[] getMethodParameterTypes() {
		return methodParameterTypes;
	}
	
	public void setTargetProxy(Object targetProxy) {
		this.targetProxy = targetProxy;
	}
	
	public Object getTargetProxy() {
		return targetProxy;
	}
	
	public void setMethod(Method method) {
		this.method = method;
	}
	
	public Method getMethod() {
		return method;
	}
	
	public <T> void onEvent(ActionData<T> data) {
		try {
			method.invoke(targetProxy, data.getData());
		}
		catch (BeansException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error("监听器处理错误: method=" + targetClazz.getName() + "." + method.getName(), e);
		}
	}

}
