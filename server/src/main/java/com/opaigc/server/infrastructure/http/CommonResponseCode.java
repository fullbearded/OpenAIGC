package com.opaigc.server.infrastructure.http;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Runner.dada
 * @date: 2020/12/6
 * @description: The common response code
 **/
@Getter
@AllArgsConstructor
public enum CommonResponseCode implements ApiResponseCode {
	OK("200", "请求成功", HttpStatus.OK.value()),

	// 系统类错误
	SYSTEM_ERROR("100000", "系统错误，请稍后再试", INTERNAL_SERVER_ERROR.value()),
	INVALID_PARAMS("100001", "参数错误", BAD_REQUEST.value()),
	ACCESS_DENIED("100002", "权限错误,请重新登录", FORBIDDEN.value()),
	UPLOAD_FILE_TOO_LARGE("100003", "超过文件大小上限，单个文件不能超过50MB", PAYLOAD_TOO_LARGE.value()),
	UPLOAD_FILE_FAIL("100004", "文件上传失败，请重试", BAD_REQUEST.value()),
	REQ_TIMEOUT("100005", "请求超时，请重试", REQUEST_TIMEOUT.value()),
	ILLEGAL_REQUEST("100006", "非法请求", INTERNAL_SERVER_ERROR.value()),
	RECORD_NOT_FOUND("100007", "数据未找到", NOT_FOUND.value()),
	WEBCLIENT_NETWORK_TIMEOUT("100008", "网络超时，请重试", REQUEST_TIMEOUT.value()),
	WEBCLIENT_ERROR("100009", "网络错误，请重试", INTERNAL_SERVER_ERROR.value()),

	CAPTCHA_RULE_NOT_EXIST("100010", "验证码规则不存在", NOT_FOUND.value()),
	USER_NAME_EXIST("100011", "用户名已存在", BAD_REQUEST.value()),
	MOBILE_EXIST("100012", "手机号已存在", BAD_REQUEST.value()),
	USERNAME_OR_MOBILE_EXIST("100013", "用户名或手机号已存在", BAD_REQUEST.value()),

	// 账户类错误
	WEB_UNAUTHORIZED("200000", "访问未授权", UNAUTHORIZED.value()),
	LOGIN_LOCKED("200001", "密码错误超过%s次，请%s分钟后再试", UNAUTHORIZED.value()),
	LOGIN_ACCOUNT_NOT_FOUND("200002", "账户或密码错误", UNAUTHORIZED.value()),
	ACCOUNT_BANNED("200003", "账户被禁用", UNAUTHORIZED.value()),
	LOGIN_EXPIRED("200004", "登录已过期，请重新登录！", FORBIDDEN.value()),
	USER_NOT_FOUND("200005", "用户未找到", NOT_FOUND.value()),
	ACCOUNT_LOGIN_OTHER_DEVICE("200006", "账号在其他设备登录", UNAUTHORIZED.value()),
	REMOTE_IP_MAX_LIMIT("200007", "未登录用户访问次数超过限制,请登录", UNAUTHORIZED.value()),


	// 业务类错误
	PROMOTION_NOT_FOUND("300001", "兑换码未找到", NOT_FOUND.value()),
	PROMOTION_INVALID("300002", "兑换码已失效", BAD_REQUEST.value()),
	PROMOTION_USED("300003", "兑换码已使用", BAD_REQUEST.value()),
	PROMOTION_USE_FAILED("300004", "兑换码使用失败", BAD_REQUEST.value()),

	USER_DAILY_USAGE_LIMIT("300005", "每日使用次数已达上限", BAD_REQUEST.value()),
	USER_USAGE_REACH_TOTAL_LIMIT("300005", "用户使用次数已达总额, 请重新购买", BAD_REQUEST.value()),

	APP_NAME_EXIST("300006", "应用名称已存在", BAD_REQUEST.value()),
	APP_WITH_ANONYMOUS_MAX_LIMIT("300007", "匿名应用数量已达上限,请登录", BAD_REQUEST.value()),
	APP_NOT_FOUND("300008", "应用未找到", NOT_FOUND.value()),
	;

	private final String code;

	private final String message;

	private final Integer httpCode;

}
