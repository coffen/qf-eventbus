package com.qf.eventbus.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * <p>
 * Project Name: C2C商城
 * <br>
 * Description: Spring测试
 * <br>
 * File Name: EventBusSpringTest.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017年2月12日 上午10:11:55 
 * @version: v1.0
 *
 */
public class EventBusSpringTest {
	
    private ApplicationContext context;
	
    @Before
	public void init() {
    	context = new ClassPathXmlApplicationContext("classpath:spring-test.xml");
	}
	
	@Test
	public void test() {
		Object obj = context.getBean("eventBusBean");
		System.out.println(obj);
	}

}
