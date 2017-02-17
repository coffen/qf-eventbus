package com.qf.eventbus.spring.bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import com.qf.eventbus.ActionData;
import com.qf.eventbus.BusSignaler;
import com.qf.eventbus.Event;
import com.qf.eventbus.spring.anno.InterceptType;
import com.qf.eventbus.spring.anno.Interceptor;
import com.qf.eventbus.spring.util.SpelExpressionUtil;

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
public class PublisherAdvisor extends AbstractPointcutAdvisor {
	
	private static final long serialVersionUID = 7098195944662142447L;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private BusSignaler signaler;
	
	private Map<String, Class<? extends Event>> eventMapping = new HashMap<String, Class<? extends Event>>();
	private List<InterceptorAttribute> attributeList = new ArrayList<InterceptorAttribute>();
	
	private Advice advice;
	private Pointcut pointCut;
	
	public PublisherAdvisor() {
		buildAdvice();
		buildPointcut();
	}
	
	public void setSignaler(BusSignaler signaler) {
		this.signaler = signaler;
	}
	
	public BusSignaler getSignaler() {
		return signaler;
	}
	
	public void setEventMapping(Map<String, Class<? extends Event>> eventMapping) {
		this.eventMapping = eventMapping;
	}
	
	public void setAttributeList(List<InterceptorAttribute> attributeList) {
		if (CollectionUtils.isEmpty(attributeList)) {
			log.error("拦截器注释列表为空");
			return;
		}
		this.attributeList = attributeList;
	}
	
	public InterceptorAttribute getAttribute(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
		if (!CollectionUtils.isEmpty(attributeList)) {
			for (InterceptorAttribute attribute : attributeList) {
				if (attribute != null && clazz != null && !StringUtils.isEmpty(methodName) && clazz == attribute.getTargetClass() && methodName.equals(attribute.getMethodName())) {
					Class<?>[] clazzArr1 = parameterTypes == null ? new Class<?>[0] : parameterTypes;
					Class<?>[] clazzArr2 = attribute.getMethodParameterTypes() == null ? new Class<?>[0] : attribute.getMethodParameterTypes();
					if (Arrays.deepEquals(clazzArr1, clazzArr2)) {
						return attribute;
					}
				}
			}
		}
		return null;
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
				Class<?> targetClass = AopUtils.getTargetClass(invocation.getThis());
				if (targetClass == null) {
					return invocation.proceed();
				}
				Method m = invocation.getMethod();
				InterceptorAttribute attribute = getAttribute(targetClass, m.getName(), m.getParameterTypes());
				if (attribute == null) {
					log.error("未找到注释属性: Method={},{},{}", targetClass.getName(), m.getName(), m.getParameterTypes());
					return invocation.proceed();
				}
				InterceptType type = attribute.getInterceptType();
				List<String> events = attribute.getEventList();
				String expr = attribute.getExpression();
				String[] parameterNames = attribute.getMethodparameterNames();
				Object retured = null;
				Object[] data = null;
				if (type == InterceptType.PARAMETER_BEFORE) {
					data = parseParameter(parameterNames, invocation.getArguments(), expr);
					fireEvents(events, data);
					retured = invocation.proceed();
				}
				else if (type == InterceptType.PARAMETER_AFTER || type == InterceptType.RETURNING) {
					retured = invocation.proceed();
					if (type == InterceptType.PARAMETER_AFTER) {
						data = parseParameter(parameterNames, invocation.getArguments(), expr);
					}
					else {
						data = parseReturned(retured, expr);
					}
					fireEvents(events, data);
				}
				else {
					try {
						retured = invocation.proceed();
					}
					catch (Exception e) {
						fireEvents(events, new Object[] { e });
					}
				}
				return retured;
			}
		};
	}
	
	private Object[] parseParameter(String[] parameterNames, Object[] parameterValues, String expression) {
		if (StringUtils.isEmpty(expression)) {
			return parameterValues;
		}
		return new Object[] { SpelExpressionUtil.parse(parameterNames, parameterValues, expression) };
	}
	
	private Object[] parseReturned(Object returned, String expression) {
		if (StringUtils.isEmpty(expression)) {
			return new Object[] { returned };
		}
		return new Object[] { SpelExpressionUtil.parse(returned, expression) };
	}
	
	private void fireEvents(List<String> events, Object data) {
		if (!CollectionUtils.isEmpty(events)) {
			for (String event : events) {
				Class<? extends Event> eventClazz = eventMapping.get(event);
				if (eventClazz != null) {
					signaler.fileEvent(eventClazz, new ActionData<Object>(data));
				}
			}
		}
	}
	
	private void buildPointcut() {
		AnnotationMatchingPointcut methodPointcut = AnnotationMatchingPointcut.forMethodAnnotation(Interceptor.class);
		this.pointCut = methodPointcut;
	}

}
