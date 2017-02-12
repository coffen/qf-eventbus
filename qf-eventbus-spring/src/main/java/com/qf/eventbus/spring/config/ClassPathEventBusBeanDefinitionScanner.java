package com.qf.eventbus.spring.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.qf.eventbus.spring.anno.EventBinding;
import com.qf.eventbus.spring.anno.Publisher;
import com.qf.eventbus.spring.anno.Subscriber;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: BeanDefinition扫描器
 * <br>
 * File Name: ClassPathEventBusBeanDefinitionScanner.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月12日 下午7:50:51 
 * @version: v1.0
 *
 */
public class ClassPathEventBusBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

	public ClassPathEventBusBeanDefinitionScanner(BeanDefinitionRegistry registry) {
		super(registry);
	}
	
	public void registerDefaultFilters() {
		addIncludeFilter(new AnnotationTypeFilter(EventBinding.class));
		addIncludeFilter(new AnnotationTypeFilter(Publisher.class));
		addIncludeFilter(new AnnotationTypeFilter(Subscriber.class));
	}
	
	public Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
		for (BeanDefinitionHolder holder : beanDefinitions) {
			GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
			definition.getPropertyValues().add("innerClassName", definition.getBeanClassName());
		}
		return beanDefinitions;
	}

	public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return super.isCandidateComponent(beanDefinition) 
				&& (beanDefinition.getMetadata().hasAnnotation(EventBinding.class.getName())
				|| beanDefinition.getMetadata().hasAnnotation(Publisher.class.getName())
				|| beanDefinition.getMetadata().hasAnnotation(Subscriber.class.getName()));
	}
	
}
