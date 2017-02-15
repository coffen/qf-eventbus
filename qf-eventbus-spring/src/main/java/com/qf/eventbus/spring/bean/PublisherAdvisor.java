package com.qf.eventbus.spring.bean;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

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
	
	private BusSignaler signaler;
	
	private Map<String, Class<? extends Event>> eventMapping = new HashMap<String, Class<? extends Event>>();
	
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
				Interceptor interceptor = m.getAnnotation(Interceptor.class);
				InterceptType type = interceptor.type();
				String[] events = interceptor.event();
				String expr = interceptor.expr();
				LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
				String[] parameterNames = u.getParameterNames(m);
				Object retured = null;
				Object data = null;
				if (type == InterceptType.PARAMETER_BEFORE) {
					data = parseParameter(parameterNames, invocation.getArguments(), expr);
					fireEvents(events, invocation.getThis(), m, data);
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
					fireEvents(events, invocation.getThis(), m, data);
				}
				else {
					try {
						retured = invocation.proceed();
					}
					catch (Exception e) {
						fireEvents(events, invocation.getThis(), m, e);
					}
				}
				return retured;
			}
		};
	}
	
	private Object parseParameter(String[] parameterNames, Object[] parameterValues, String expression) {
		if (StringUtils.isEmpty(expression)) {
			return parameterValues;
		}
		return SpelExpressionUtil.parse(parameterNames, parameterValues, expression);
	}
	
	private Object parseReturned(Object returned, String expression) {
		if (StringUtils.isEmpty(expression)) {
			return returned;
		}
		return SpelExpressionUtil.parse(returned, expression);
	}
	
	private void fireEvents(String[] events, Object target, Method method, Object data) {
		if (events != null && events.length > 0) {
			for (String event : events) {
				Class<? extends Event> eventClazz = eventMapping.get(event);
				if (eventClazz != null) {
					signaler.fileEvent(eventClazz, new ActionData<EventData>(new EventData(target, method, data)));
				}
			}
		}
	}
	
	private void buildPointcut() {
		AnnotationMatchingPointcut methodPointcut = AnnotationMatchingPointcut.forMethodAnnotation(Interceptor.class);
		this.pointCut = methodPointcut;
	}
	
	public class EventData {
		
		private Object target;
		private Method method;
		private Object data;
		
		public EventData(Object target, Method method, Object data) {
			this.target = target;
			this.method = method;
			this.data = data;
		}
		
		public Object getTarget() {
			return target;
		}
		
		public Method getMethod() {
			return method;
		}
		
		public Object getData() {
			return data;
		}
	}

}
