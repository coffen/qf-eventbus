package com.qf.eventbus.spring.bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.util.StringUtils;

import com.qf.eventbus.BusServer;
import com.qf.eventbus.Event;
import com.qf.eventbus.spring.anno.EventBinding;
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
	
	private final Map<Class<? extends Event>, Set<String>> eventBindingMap = new ConcurrentHashMap<Class<? extends Event>, Set<String>>();
	private final List<Method> inteceptors = new ArrayList<Method>();
	private final List<Method> Listeners = new ArrayList<Method>();
	
	public EventbusAnnotationBeanPostProcessor() {}
	
	public String getAnnoPackage() {
		return annoPackage;
	}
	
	public void setAnnoPackage(String annoPackage) {
		this.annoPackage = annoPackage;
	}

	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		// 注册BusServerl类型Bean, 设置为单例模式
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
        			buildEventBinding(clazz);
        		}
        		if (clazz.isAnnotationPresent(Publisher.class)) {
        			
        		}
        		if (clazz.isAnnotationPresent(Subscriber.class)) {
        			
        		}
        	}
        	catch (ClassNotFoundException e) {
        		log.error("未找到指定类", e);
        	}
        }
	}
	
	private void registerEventBusServer(BeanDefinitionRegistry registry) {
		BeanDefinitionBuilder eventBusServerBuilder = BeanDefinitionBuilder.genericBeanDefinition(BusServer.class);
		eventBusServerBuilder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		eventBusServerBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
		registry.registerBeanDefinition("busServer", eventBusServerBuilder.getBeanDefinition());
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
	private void buildEventBinding(Class<?> clazz) {
    	if (clazz != null && clazz.isAnnotationPresent(EventBinding.class)) {
    		EventBinding binding = clazz.getAnnotation(EventBinding.class);
    		if (Event.class.isAssignableFrom(clazz)) {
    			eventBindingMap.put((Class<? extends Event>)clazz, new HashSet<String>(Arrays.asList(binding.channel())));
    		}
    		else {
    			
    		}
    	}
    }
	
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// do nothing
	}

}
