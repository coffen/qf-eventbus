package com.qf.eventbus.spring.bean;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.qf.eventbus.ActionData;
import com.qf.eventbus.Listener;

public class AdviceListener implements Listener {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ListenerAttribute attribute;
	private BeanFactory factory;
	
	public AdviceListener(ListenerAttribute attribute) {
		this.attribute = attribute;
	}
	
	public ListenerAttribute getAttribute() {
		return attribute;
	}
	
	public void setFactory(BeanFactory factory) {
		this.factory = factory;
	}
	
	@SuppressWarnings("rawtypes")
	public void onEvent(ActionData data) {
		try {
			Object targetProxy = factory.getBean(attribute.getTargetClass());
			Method method = targetProxy.getClass().getMethod(attribute.getMethodName(), attribute.getMethodParameterTypes());
			method.invoke(targetProxy, (Object[])data.getData());
		}
		catch (Exception e) {
			log.error("监听器处理错误: method=" + attribute.getTargetClass().getName() + "." + attribute.getMethodName(), e);
		}
	}

}
