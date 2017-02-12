package com.qf.eventbus.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 事件总线注释后置处理器
 * <br>
 * File Name: EventbusAnnotationBeanPostProcessor.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月12日 上午10:08:18 
 * @version: v1.0
 *
 */
public class EventbusAnnotationBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {
	
	private String annoPackage;
	
	public EventbusAnnotationBeanPostProcessor() {
		System.out.println("BeanDefinitionRegistryPostProcessor created");
	}
	
	public String getAnnoPackage() {
		return annoPackage;
	}
	
	public void setAnnoPackage(String annoPackage) {
		this.annoPackage = annoPackage;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("postProcessBeanFactory called");
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		System.out.println("postProcessBeanDefinitionRegistry called");
	}

}
