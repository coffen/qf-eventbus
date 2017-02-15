package com.qf.eventbus.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

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
@ContextConfiguration(classes = { SpringConfig.class })
public class EventBusSpringTest extends AbstractJUnit4SpringContextTests {
	
    @Autowired
    private ProductService productService;
	
	@Test
	public void test() {
		productService.save(10029L);
	}

}
