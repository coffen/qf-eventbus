package com.qf.eventbus.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: 总线管理名称空间处理器
 * <br>
 * File Name: EventBusNamespaceHandler.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月11日 下午3:58:21 
 * @version: v1.0
 *
 */
public class EventBusNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
        registerBeanDefinitionParser("annotation", new EventBusAnnotationParser());		
	}

}
