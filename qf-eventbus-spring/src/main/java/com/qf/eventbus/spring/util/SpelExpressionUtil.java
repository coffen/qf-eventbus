package com.qf.eventbus.spring.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
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
	
	private final static Map<String, Expression> expressionMap = new HashMap<String, Expression>();
	private final static StandardEvaluationContext context = new StandardEvaluationContext();
	
	/**
	 * 解析单个参数组成的表达式
	 * 
	 * @param object
	 * @param expr
	 * @return
	 */
	public static void preParse(String expr) {
		if (StringUtils.isBlank(expr)) {
			return;
		}
		loadExpression(expr);
	}
	
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
		Expression expression = loadExpression(expr);
		if (expression == null) {
			log.error("表达式解析结果为空: expr={}" + expr);
			return null;
		}
		return expression.getValue(context, object);
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
		Expression expression = loadExpression(expr);
		if (expression == null) {
			log.error("表达式解析结果为空: expr={}" + expr);
			return null;
		}
		Map<String, Object> value = new HashMap<String, Object>();
		for(int i = 0; i < objectNames.length; i++) {
			value.put(objectNames[i], objects[i]);
		}
		return expression.getValue(context, value);
	}
	
	private static Expression loadExpression(String expr) {
		if (StringUtils.isBlank(expr)) {
			return null;
		}
		Expression expression = expressionMap.get(expr);
		if (expression == null) {
			expression = new SpelExpressionParser().parseExpression(expr);
			if (expression != null) {
				expressionMap.put(expr, expression);
			}
		}
		return expression;
	}

}
