package com.qf.eventbus.spring.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 
 * <p>
 * Project Name: 买到手抽筋
 * <br>
 * Description: Spel表达式工具
 * <br>
 * File Name: SpelExpressionUtil.java
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州偶尔科技有限公司
 * <br>
 * @author 穷奇
 * @create time：2017-02-14 21:04:07 
 * @version: v1.0
 *
 */
public class SpelExpressionUtil {
	
	private final static Logger log = LoggerFactory.getLogger(SpelExpressionUtil.class);
	
	/**
	 * 解析单个参数组成的表达式
	 * 
	 * @param object
	 * @param expr
	 * @return
	 */
	public static Object parse(Object object, String expr) {
		if (object == null) {
			log.error("对象为空");
			return null;
		}
		if (StringUtils.isBlank(expr)) {
			return object;
		}
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setRootObject(object);
		return parser.parseExpression(expr).getValue(context);
	}
	
	/**
	 * 解析多个参数组成的表达式
	 * 
	 * @param object
	 * @param expr
	 * @return
	 */
	public static Object parse(String[] objectNames, Object[] objects, String expr) {
		if (objectNames == null || objects == null) {
			log.error("对象数组或对象名称数组为空");
			return null;
		}
		if (objects.length != objectNames.length) {
			log.error("对象数组和对象名称数组长度不等");
			return null;
		}
		if (StringUtils.isBlank(expr)) {
			return objects;
		}
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		for(int i = 0; i < objectNames.length; i++) {
			context.setVariable(objectNames[i], objects[i]);
		}
		return parser.parseExpression(expr).getValue(context);
	}

}
