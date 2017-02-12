package com.qf.eventbus.spring.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.qf.eventbus.spring.bean.EventbusAnnotationBeanPostProcessor;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 事件总线Annotation元素解析器
 * <br>
 * File Name: EventBusAnnotationParser.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月11日 下午4:00:36 
 * @version: v1.0
 *
 */
public class EventBusAnnotationParser implements BeanDefinitionParser {

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String pkg = element.getAttribute("package");
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(EventbusAnnotationBeanPostProcessor.class);
		builder.addPropertyValue("annoPackage", pkg);
		
        registerPostProcessor(parserContext, builder, EventbusAnnotationBeanPostProcessor.class.getSimpleName());
        return builder.getBeanDefinition();
	}
	
	private void registerPostProcessor(ParserContext parserContext, BeanDefinitionBuilder builder, String beanName) {
		builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		parserContext.getRegistry().registerBeanDefinition(beanName, builder.getBeanDefinition());
        BeanDefinitionHolder holder = new BeanDefinitionHolder(builder.getBeanDefinition(), beanName);
        parserContext.registerComponent(new BeanComponentDefinition(holder));
	}

}
