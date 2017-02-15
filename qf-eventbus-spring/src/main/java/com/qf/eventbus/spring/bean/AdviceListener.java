package com.qf.eventbus.spring.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanFactory;

import com.qf.eventbus.ActionData;
import com.qf.eventbus.Listener;

public class AdviceListener implements Listener {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ListenerAttribute attribute;
	private Object targetProxy;
	private Method method;
	
	public AdviceListener(ListenerAttribute attribute) {
		this.attribute = attribute;
	}
	
	public ListenerAttribute getAttribute() {
		return attribute;
	}
	
	void setMethod(Method method) {
		this.method = method;
	}
	
	public <T> void onEvent(ActionData<T> data) {
		try {
			method.invoke(targetProxy, data.getData());
		}
		catch (BeansException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error("监听器处理错误: method=" + attribute.getTargetClass().getName() + "." + attribute.getMethodName(), e);
		}
	}

	public void fillProxy(BeanFactory beanFactory) throws BeansException, NoSuchMethodException, SecurityException {
		Object obj = beanFactory.getBean(attribute.getTargetClass());
		if (obj == null) {
			throw new FatalBeanException("未找到指定类型的代理类: " + attribute.getTargetClass());
		}
		targetProxy = obj;
		method = obj.getClass().getMethod(attribute.getMethodName(), attribute.getMethodParameterTypes());
	}

}
