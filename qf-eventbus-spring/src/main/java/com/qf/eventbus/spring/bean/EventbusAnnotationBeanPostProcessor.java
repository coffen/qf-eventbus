package com.qf.eventbus.spring.bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.qf.eventbus.BusManager;
import com.qf.eventbus.BusSignaler;
import com.qf.eventbus.DefaultBusManager;
import com.qf.eventbus.Event;
import com.qf.eventbus.spring.anno.EventBinding;
import com.qf.eventbus.spring.anno.InterceptType;
import com.qf.eventbus.spring.anno.Interceptor;
import com.qf.eventbus.spring.anno.Listener;

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
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private String annoPackage;	// 扫描事件、发布者、订阅者的类包, 多个包名用","隔开
	
	private final Map<String, Class<? extends Event>> eventClazzMapping = new HashMap<String, Class<? extends Event>>();
	private final Map<String, Set<Class<? extends Event>>> channelEventMapping = new HashMap<String, Set<Class<? extends Event>>>();
	private final Set<InterceptorAttribute> interceptorSet = new HashSet<InterceptorAttribute>();
	private final Set<ListenerAttribute> listenerSet = new HashSet<ListenerAttribute>();
	private final Set<AdviceListener> adviceSet = new HashSet<AdviceListener>();
	
	private final Class<?> PUBLISHER_ADVISOR_BEAN_CLASS = PublisherAdvisor.class;
	private final Class<?> BUS_BEAN_CLASS = DefaultBusManager.class;
	
	private final Class<? extends BusManager> busManagerClazz = DefaultBusManager.class;
	
	private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
	
	public EventbusAnnotationBeanPostProcessor() {}
	
	public String getAnnoPackage() {
		return annoPackage;
	}
	
	public void setAnnoPackage(String annoPackage) {
		this.annoPackage = annoPackage;
	}

	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		AopConfigUtils.registerAutoProxyCreatorIfNecessary(registry);
		
        String[] scanPackages = StringUtils.tokenizeToStringArray(annoPackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        if (scanPackages == null || scanPackages.length < 1) {
        	log.error("包名指定错误, 无法执行BeanDefinitionRegistryPostProcessor: annoPackage={}", annoPackage);
        	throw new FatalBeanException("BeanDefinitionRegistryPostProcessor执行错误, 包名无效: " + annoPackage);  
        }
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.addIncludeFilter(new AnnotationTypeFilter(EventBinding.class));
        ClassLoader classLoader = scanner.getResourceLoader().getClassLoader();
        Set<BeanDefinition> beanDefinitions = findCandidateComponents(scanner, scanPackages);
        
        // 收集事件类型和绑定的频道
        for(BeanDefinition bd : beanDefinitions) {
        	try {
        		Class<?> clazz = classLoader.loadClass(bd.getBeanClassName());
        		if (clazz.isAnnotationPresent(EventBinding.class)) {
        			buildEventBinding(clazz);
        		}
        	}
        	catch (ClassNotFoundException e) {
        		log.error("未找到指定类", e);
        	}
        }
        // 检查发布者和订阅者的注释
        for(BeanDefinition bd : beanDefinitions) {
        	try {
        		Class<?> clazz = classLoader.loadClass(bd.getBeanClassName());
       			checkAnnoClazz(clazz);
        	}
        	catch (ClassNotFoundException e) {
        		log.error("未找到指定类", e);
        	}
        }		
		// 注册BusServerl类型Bean
		registerBeanDifinition(registry, BeanDefinitionBuilder.genericBeanDefinition(BUS_BEAN_CLASS), BUS_BEAN_CLASS.getName());
        
    	// 创建PublisherAdvisor的BeanDifinition
		BeanDefinitionBuilder pubBuilder = BeanDefinitionBuilder.genericBeanDefinition(PUBLISHER_ADVISOR_BEAN_CLASS);
		registerBeanDifinition(registry, pubBuilder, PUBLISHER_ADVISOR_BEAN_CLASS.getName());
	}
	
	private void registerBeanDifinition(BeanDefinitionRegistry registry, BeanDefinitionBuilder builder, String beanName) {
		builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		builder.setScope(BeanDefinition.SCOPE_SINGLETON);
		registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
	}
	
    private Set<BeanDefinition> findCandidateComponents(ClassPathBeanDefinitionScanner scanner, String... scanPackages){
        Set<BeanDefinition> beanDefinitionSet = new LinkedHashSet<BeanDefinition>();
        for(String scanPackage : scanPackages){
            Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(scanPackage);
            beanDefinitionSet.addAll(beanDefinitions);
        }
        return beanDefinitionSet;
    }
    
	@SuppressWarnings("unchecked")
	private void buildEventBinding(Class<?> clazz) throws BeansException {
    	if (clazz != null && clazz.isAnnotationPresent(EventBinding.class)) {
    		EventBinding binding = clazz.getAnnotation(EventBinding.class);
    		// 验证注释的正确性
    		String eventName = binding.name();
    		if (StringUtils.isEmpty(eventName)) {
				log.error("事件检查错误, 事件名称为空: class={}", clazz);
				throw new FatalBeanException("事件检查错误错误, 事件名称为空: " + clazz.getName());
    		}
    		if (eventClazzMapping.containsKey(eventName)) {
				log.error("事件检查错误, 事件名称为空: class={}, name={}", clazz.getName(), eventName);
				throw new FatalBeanException("事件检查错误错误, 同名事件已经存在: " + eventName);
    		}
    		List<String> channelList = checkChannel(clazz, binding.channel(), false);

    		Class<? extends Event> eventProxyClazz = null;
    		if (Event.class.isAssignableFrom(clazz)) {
    			eventProxyClazz = (Class<? extends Event>)clazz;
    		}
    		else {
    			try {
	    			Enhancer enhancer = new Enhancer();
	    			enhancer.setInterfaces(new Class[] { Event.class });
	    			enhancer.setCallbackType(NoOp.class);
	    			eventProxyClazz = enhancer.createClass();
    			}
    			catch (Exception e) {
    				log.error("生成事件代理类失败", e);
    				throw new FatalBeanException("事件代理类生成错误: " + e.getMessage());
    			}
    		}
    		for (String channel : channelList) {
    			Set<Class<? extends Event>> set = channelEventMapping.get(channel);
    			if (set == null) {
    				set = new HashSet<Class<? extends Event>>();
    				channelEventMapping.put(channel, set);
    			}
    			set.add(eventProxyClazz);
    		}
    		eventClazzMapping.put(eventName, eventProxyClazz);
    	}
    }
	
	private void checkAnnoClazz(Class<?> clazz) throws BeansException {
    	if (clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
            	if (method.isAnnotationPresent(Interceptor.class)) {
            		InterceptorAttribute attribute = buildInterceptorAttribute(method);
            		interceptorSet.add(attribute);
            	}
            	if (method.isAnnotationPresent(Listener.class)) {
            		ListenerAttribute attribute = buildListenerAttribute(method);
            		listenerSet.add(attribute);
            	}
            }
    	}
	}
	
	private InterceptorAttribute buildInterceptorAttribute(Method method) throws FatalBeanException {
		if (method == null || !method.isAnnotationPresent(Interceptor.class)) {
			return null;
		}
		Class<?> targetClazz = method.getDeclaringClass();
		Interceptor intr = method.getAnnotation(Interceptor.class);
		String[] eventArr = intr.event();
		InterceptType type = intr.type();
		String expr = intr.expr();
		List<String> events = checkEvent(targetClazz, eventArr, true);		
		// 构建InterceptorAttribute
		InterceptorAttribute attribute = new InterceptorAttribute();
		attribute.setTargetClass(targetClazz);
		attribute.setMethodName(method.getName());
		attribute.setMethodParameterTypes(method.getParameterTypes());
		attribute.setMethodparameterNames(discoverer.getParameterNames(method));
		attribute.setEventList(events);
		attribute.setInterceptType(type);
		attribute.setExpression(expr);
		return attribute;
	}
	
	private ListenerAttribute buildListenerAttribute(Method method) throws FatalBeanException {
		if (method == null || !method.isAnnotationPresent(Listener.class)) {
			return null;
		}
		Class<?> targetClazz = method.getDeclaringClass();
		Listener lisn = method.getAnnotation(Listener.class);
		String[] channelArr = lisn.channel();
		List<String> channels = checkChannel(targetClazz, channelArr, true);		
		// 构建ListenerAttribute
		ListenerAttribute attribute = new ListenerAttribute();
		attribute.setTargetClass(targetClazz);
		attribute.setMethodName(method.getName());
		attribute.setMethodParameterTypes(method.getParameterTypes());
		attribute.setChannelList(channels);
		return attribute;
	}
	
	private List<String> checkEvent(Class<?> clazz, String[] events, boolean needCheckExist) throws FatalBeanException {
		List<String> eventList = events == null ? null : new ArrayList<String>(Arrays.asList(events));
		if (!CollectionUtils.isEmpty(eventList)) {
			Iterator<String> it = eventList.iterator();
			while(it.hasNext()) {
				String event = it.next();
				if (StringUtils.isEmpty(event)) {
					it.remove();
				}
				if (needCheckExist && !eventClazzMapping.containsKey(event)) {
					log.error("事件检查错误, 事件未声明: clazz={}, event={}", clazz, event);
					throw new FatalBeanException("事件检查错误, 事件未声明: clazz=" + clazz + ", event=" + event);
				}
			}
		}
		if (CollectionUtils.isEmpty(eventList)) {
			log.error("事件检查错误, 事件列表为空: clazz={}", clazz);
			throw new FatalBeanException("事件检查错误, 事件列表为空: clazz=" + clazz);
		}
		else {
			return eventList;
		}
	}
	
	private List<String> checkChannel(Class<?> clazz, String[] channels, boolean needCheckExist) throws FatalBeanException {
		List<String> channelList = channels == null ? null : new ArrayList<String>(Arrays.asList(channels));
		if (!CollectionUtils.isEmpty(channelList)) {
			Iterator<String> it = channelList.iterator();
			while(it.hasNext()) {
				String channel = it.next();
				if (StringUtils.isEmpty(channel)) {
					it.remove();
				}
				if (needCheckExist && !channelEventMapping.containsKey(channel)) {
					log.error("频道检查错误, 频道未声明: clazz={}, channel={}", clazz, channel);
					throw new FatalBeanException("频道检查错误, 频道未声明: clazz=" + clazz + ", channel=" + channel);
				}
			}
		}
		if (CollectionUtils.isEmpty(channelList)) {
			log.error("频道检查错误, 频道列表为空: clazz={}", clazz);
			throw new FatalBeanException("频道检查错误, 频道列表为空: clazz=" + clazz);
		}
		else {
			return channelList;
		}
	}
	
	public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
		BusManager eventBusBean = beanFactory.getBean(busManagerClazz);
		if (eventBusBean == null) {
			log.error("事件总线创建失败, BusManager未生成");
			throw new FatalBeanException("事件总线创建失败");
		}
		BusSignaler signaler = eventBusBean.buildSignaler();
		if (signaler == null) {
			log.error("Signaler创建失败, BusSignaler未生成");
			throw new FatalBeanException("Signaler创建失败");
		}
		// 创建频道
		if (channelEventMapping.size() > 0) {
			for (String channel : channelEventMapping.keySet()) {
				if (StringUtils.isEmpty(channel)) {
					continue;
				}
				if (signaler.buildChannel(channel)) {
					signaler.openChannel(channel);
				}
				else {
					log.error("频道创建失败, channel={}", channel);
					throw new FatalBeanException("频道创建失败, channel=" + channel);
				}
			}
		}
		// 注册发布器
		Iterator<Entry<String, Set<Class<? extends Event>>>> eventIterator = channelEventMapping.entrySet().iterator();
		while (eventIterator.hasNext()) {
			Entry<String, Set<Class<? extends Event>>> entry = eventIterator.next();
			String ch = entry.getKey();
			Set<Class<? extends Event>> eventSet = entry.getValue();
			if (!StringUtils.isEmpty(ch) && eventSet.size() > 0) {
				for (Class<? extends Event> clazz : eventSet) {
					if (clazz != null) {
						signaler.register(ch, clazz);
					}
				}
			}
		}
		// 订阅频道
		Iterator<ListenerAttribute> it = listenerSet.iterator();
		while (it.hasNext()) {
			ListenerAttribute attribute = it.next();
			if (attribute != null && !CollectionUtils.isEmpty(attribute.getChannelList())) {
				for (String channel : attribute.getChannelList()) {
					AdviceListener advice = new AdviceListener(attribute);
					signaler.subscribe(channel, advice);
					adviceSet.add(advice);
				}
			}
		}
		
		PublisherAdvisor advisor = (PublisherAdvisor)beanFactory.getBean(PUBLISHER_ADVISOR_BEAN_CLASS);
		advisor.setSignaler(signaler);
		advisor.setEventMapping(eventClazzMapping);
	}

}
