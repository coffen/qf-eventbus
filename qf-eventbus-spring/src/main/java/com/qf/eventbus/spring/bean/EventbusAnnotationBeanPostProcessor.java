package com.qf.eventbus.spring.bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.util.StringUtils;

import com.qf.eventbus.BusManager;
import com.qf.eventbus.BusServer;
import com.qf.eventbus.DefaultBusManager;
import com.qf.eventbus.Event;
import com.qf.eventbus.spring.anno.EventBinding;
import com.qf.eventbus.spring.anno.Interceptor;
import com.qf.eventbus.spring.anno.Listener;
import com.qf.eventbus.spring.anno.Publisher;
import com.qf.eventbus.spring.anno.Subscriber;
import com.qf.eventbus.spring.config.ClassPathEventBusBeanDefinitionScanner;

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
	
	private final Map<Class<? extends Event>, List<String>> eventBindingMap = new HashMap<Class<? extends Event>, List<String>>();
	private final Map<Method, List<String>> inteceptorMap = new HashMap<Method, List<String>>();
	private final Map<Method, List<String>> listenerMap = new HashMap<Method, List<String>>();
	
	public EventbusAnnotationBeanPostProcessor() {}
	
	public String getAnnoPackage() {
		return annoPackage;
	}
	
	public void setAnnoPackage(String annoPackage) {
		this.annoPackage = annoPackage;
	}

	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		// 注册BusServerl类型Bean
		registerEventBusServer(registry);
		
        String[] scanPackages = StringUtils.tokenizeToStringArray(annoPackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        if (scanPackages == null || scanPackages.length < 1) {
        	log.error("包名指定错误, 无法执行BeanDefinitionRegistryPostProcessor: annoPackage={}", annoPackage);
        	throw new FatalBeanException("BeanDefinitionRegistryPostProcessor执行错误, 包名无效: " + annoPackage);  
        }
        ClassPathEventBusBeanDefinitionScanner scanner = new ClassPathEventBusBeanDefinitionScanner(registry);
        ClassLoader classLoader = scanner.getResourceLoader().getClassLoader();
        Set<BeanDefinition> beanDefinitions = findCandidateComponents(scanner, scanPackages);
        for(BeanDefinition bd : beanDefinitions) {
        	try {
        		Class<?> clazz = classLoader.loadClass(bd.getBeanClassName());
        		if (clazz.isAnnotationPresent(EventBinding.class)) {
        			collectEventBinding(clazz);
        		}
        		if (clazz.isAnnotationPresent(Publisher.class)) {
        			collectInteceptors(clazz);
        		}
        		if (clazz.isAnnotationPresent(Subscriber.class)) {
        			collectListeners(clazz);
        		}
        	}
        	catch (ClassNotFoundException e) {
        		log.error("未找到指定类", e);
        	}
        }
	}
	
	private void registerEventBusServer(BeanDefinitionRegistry registry) {
		BeanDefinitionBuilder eventBusServerBuilder = BeanDefinitionBuilder.genericBeanDefinition(DefaultBusManager.class);
		eventBusServerBuilder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		eventBusServerBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
		registry.registerBeanDefinition("busManager", eventBusServerBuilder.getBeanDefinition());
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
	private void collectEventBinding(Class<?> clazz) throws BeansException {
    	if (clazz != null && clazz.isAnnotationPresent(EventBinding.class)) {
    		EventBinding binding = clazz.getAnnotation(EventBinding.class);
    		// 验证注释的正确性
    		String name = binding.name();
    		if (StringUtils.isEmpty(name) || eventBindingMap.containsKey(name)) {
				log.error("事件名称不能为空");
				throw new FatalBeanException("事件代理类生成错误, 名称为空");
    		}
    		if (eventBindingMap.containsKey(name)) {
				log.error("同名事件已经存在");
				throw new FatalBeanException("事件代理类生成错误, 同名事件已经存在");
    		}
    		List<String> channels = new ArrayList<String>(Arrays.asList(binding.channel()));
    		Iterator<String> it = channels.iterator();
    		while(it.hasNext()) {
    			String channel = it.next();
    			if (StringUtils.isEmpty(channel)) {
    				it.remove();
    			}
    		}
    		if (channels.size() == 0) {
				log.error("事件绑定渠道为空");
				throw new FatalBeanException("事件代理类生成错误, 绑定渠道为空");
    		}
    		if (Event.class.isAssignableFrom(clazz)) {
    			eventBindingMap.put((Class<? extends Event>)clazz, channels);
    		}
    		else {
    			try {
	    			Enhancer enhancer = new Enhancer();
	    			enhancer.setInterfaces(new Class[] { Event.class });
	    			enhancer.setCallbackType(NoOp.class);
	    			Class<? extends Event> eventClazz = enhancer.createClass();
	    			eventBindingMap.put(eventClazz, channels);
    			}
    			catch (Exception e) {
    				log.error("生成事件代理类失败", e);
    				throw new FatalBeanException("事件代理类生成错误: " + e.getMessage());
    			}
    		}
    	}
    }
	
	private void collectInteceptors(Class<?> clazz) throws BeansException {
    	if (clazz != null && clazz.isAnnotationPresent(Publisher.class)) {
    		Publisher publisher = clazz.getAnnotation(Publisher.class);
    		List<String> defaultEvents = new ArrayList<String>(Arrays.asList(publisher.event()));
    		Iterator<String> it = defaultEvents.iterator();
    		while(it.hasNext()) {
    			String event = it.next();
    			if (StringUtils.isEmpty(event)) {
    				it.remove();
    			}
    		}
    		boolean hasDefaultEvent = defaultEvents.size() > 0;
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods){
            	if (method.isAnnotationPresent(Interceptor.class)) {
            		Interceptor interceptor = method.getAnnotation(Interceptor.class);
            		String[] eventArr = interceptor.event();
            		List<String> methodEvents = new ArrayList<String>(Arrays.asList(eventArr));
            		Iterator<String> eit = methodEvents.iterator();
            		while(eit.hasNext()) {
            			String event = eit.next();
            			if (StringUtils.isEmpty(event)) {
            				eit.remove();
            			}
            		}
            		if (!hasDefaultEvent && methodEvents.size() == 0) {
        				log.error("拦截器至少绑定一个事件");
        				throw new FatalBeanException("拦截器注册失败: 绑定事件为空");
            		}
            		List<String> events = new ArrayList<String>(defaultEvents);
            		events.addAll(methodEvents);
            		inteceptorMap.put(method, events);
            	}
            }
    	}
	}
	
	private void collectListeners(Class<?> clazz) throws BeansException {
    	if (clazz != null && clazz.isAnnotationPresent(Subscriber.class)) {
    		Subscriber subscriber = clazz.getAnnotation(Subscriber.class);
    		List<String> defaultChannels = new ArrayList<String>(Arrays.asList(subscriber.channel()));
    		Iterator<String> it = defaultChannels.iterator();
    		while(it.hasNext()) {
    			String channel = it.next();
    			if (StringUtils.isEmpty(channel)) {
    				it.remove();
    			}
    		}
    		boolean hasDefaultChannel = defaultChannels.size() > 0;
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods){
            	if (method.isAnnotationPresent(Listener.class)) {
            		Listener listener = method.getAnnotation(Listener.class);
            		String[] channelArr = listener.channel();
            		List<String> methodChannels = new ArrayList<String>(Arrays.asList(channelArr));
            		Iterator<String> cit = methodChannels.iterator();
            		while(cit.hasNext()) {
            			String channel = cit.next();
            			if (StringUtils.isEmpty(channel)) {
            				cit.remove();
            			}
            		}
            		if (!hasDefaultChannel && methodChannels.size() == 0) {
        				log.error("监听器至少订阅一个频道");
        				throw new FatalBeanException("监听器注册失败: 订阅频道为空");
            		}
            		List<String> channels = new ArrayList<String>(defaultChannels);
            		channels.addAll(methodChannels);
            		listenerMap.put(method, channels);
            	}
            }
    	}
	}
	
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		BusManager eventBusBean = beanFactory.getBean(DefaultBusManager.class);
		if (eventBusBean == null) {
			log.error("监听器至少订阅一个频道");
			throw new FatalBeanException("监听器注册失败: 订阅频道为空");
		}
	}

}
