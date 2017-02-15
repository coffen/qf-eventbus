package com.qf.eventbus.spring.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import com.qf.eventbus.ActionData;
import com.qf.eventbus.Listener;
import com.qf.eventbus.spring.bean.PublisherAdvisor.EventData;

public class AdviceListener implements Listener {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ListenerAttribute attribute;
	
	public AdviceListener(ListenerAttribute attribute) {
		this.attribute = attribute;
	}
	
	public ListenerAttribute getAttribute() {
		return attribute;
	}
	
	@SuppressWarnings("rawtypes")
	public void onEvent(ActionData data) {
		try {
			if (data.getData() instanceof PublisherAdvisor.EventData) {
				EventData ed = (EventData)data.getData();
				Object targetProxy = ed.getData();
				Method method = ed.getMethod();
				method.invoke(targetProxy, data.getData());
			}
		}
		catch (BeansException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error("监听器处理错误: method=" + attribute.getTargetClass().getName() + "." + attribute.getMethodName(), e);
		}
	}

}
