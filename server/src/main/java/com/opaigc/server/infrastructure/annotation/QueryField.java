package com.opaigc.server.infrastructure.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Runner.dada
 * @date: 2021/1/21
 * @description: 查询条件
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryField {

	/**
	 * 对应对象的字段名
	 *
	 * @return String
	 **/
	String field() default "";

	/**
	 * 查询方式，默认为等于 username = "runner.dada"
	 *
	 * @return String
	 **/
	Operator operator() default Operator.EQ;

	/**
	 * 默认为true, 当value为空字符串或者空集合时过滤该条件
	 **/
	boolean skipEmpty() default true;

	/**
	 * 默认为true，当value为null时过滤该条件
	 **/
	boolean skipNull() default true;

	/**
	 * 是否忽略该字段的查询条件
	 **/
	boolean ignore() default false;

	enum Operator {
		// 等于
		EQ
		// 不等于
		, NE
		// 模糊查询
		, LIKE
		// 前置匹配
		, LEFT_LIKE
		// 前置匹配
		, RIGHT_LIKE
		// 大于
		, GT
		// 大于等于
		, GTE
		// 小于
		, LT
		// 小于等于
		, LTE
		// 包含
		, IN
		// 不包含
		, NOT_IN
		// 在其中
		, BETWEEN
		// 不为空
		, NOT_NULL
		// 为空
		, IS_NULL
	}
}

