package com.qf.eventbus.spring.bean;

import java.util.List;
import java.util.Map;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.qf.eventbus.BusSignaler;
import com.qf.eventbus.spring.anno.Interceptor;
import com.qf.eventbus.spring.anno.Publisher;

/**
 * 
 * <p>
 * Project Name: 买到手抽筋
 * <br>
 * Description: 发布者Advisor
 * <br>
 * File Name: PublisherAdvisor.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017-02-13 23:01:42 
 * @version: v1.0
 *
 */
public class PublisherAdvisor extends AbstractPointcutAdvisor implements BeanPostProcessor {
	
	private static final long serialVersionUID = 7098195944662142447L;
	
	private BusSignaler signaler;
	
	private Map<Class<?>, List<String>> defaultEventMapping;
	
	private Advice advice;
	private Pointcut pointCut;
	
	public PublisherAdvisor() {}
	
	public void setSignaler(BusSignaler signaler) {
		this.signaler = signaler;
	}
	
	public BusSignaler getSignaler() {
		return signaler;
	}
	
	public void setDefaultEventMapping(Map<Class<?>, List<String>> defaultEventMapping) {
		this.defaultEventMapping = defaultEventMapping;
	}
	
	public Map<Class<?>, List<String>> getDefaultEventMapping() {
		return defaultEventMapping;
	}

	public Pointcut getPointcut() {
		return this.pointCut;
	}

	public Advice getAdvice() {
		return this.advice;
	}
	
	private void buildAdvice() {
		this.advice = new MethodInterceptor() {
			public Object invoke(MethodInvocation invocation) throws Throwable {
				return null;
			}
		};
	}
	
	private void buildPointcut() {
		AnnotationMatchingPointcut clazzPointcut = AnnotationMatchingPointcut.forClassAnnotation(Publisher.class);
		AnnotationMatchingPointcut methodPointcut = AnnotationMatchingPointcut.forMethodAnnotation(Interceptor.class);
		this.pointCut = new ComposablePointcut(clazzPointcut).union(methodPointcut);
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		buildAdvice();
		buildPointcut();
		
		return bean;
	}

}
