package com.qf.eventbus.spring.bean;

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
import com.qf.eventbus.spring.anno.Listener;
import com.qf.eventbus.spring.anno.Subscriber;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 订阅者Advisor
 * <br>
 * File Name: SubscriberAdvisor.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月14日 上午9:43:24 
 * @version: v1.0
 *
 */
public class SubscriberAdvisor extends AbstractPointcutAdvisor implements BeanPostProcessor {
	
	private static final long serialVersionUID = -646761070317608113L;

	private BusSignaler signaler;
	
	private Advice advice;
	private Pointcut pointCut;
	
	public SubscriberAdvisor() {}
	
	public void setSignaler(BusSignaler signaler) {
		this.signaler = signaler;
	}
	
	public BusSignaler getSignaler() {
		return signaler;
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
		AnnotationMatchingPointcut clazzPointcut = AnnotationMatchingPointcut.forClassAnnotation(Subscriber.class);
		AnnotationMatchingPointcut methodPointcut = AnnotationMatchingPointcut.forMethodAnnotation(Listener.class);
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
