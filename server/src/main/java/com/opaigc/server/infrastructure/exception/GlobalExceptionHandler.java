package com.opaigc.server.infrastructure.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.opaigc.server.infrastructure.http.ApiResponse;
import com.opaigc.server.infrastructure.http.CommonResponseCode;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.netty.handler.ssl.SslHandshakeTimeoutException;
import jakarta.servlet.http.HttpServletResponse;
import java.net.SocketTimeoutException;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import javax.naming.AuthenticationException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Runner.dada
 * @date: 2020/12/6
 * @description: Global Exception handler
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * Global Exception
	 **/
	@ExceptionHandler({Exception.class, Throwable.class})
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResponse defaultExceptionHandler(HttpServletResponse response, Exception e) {
		log.error("Exception", e);
		if (response.getStatus() != HttpStatus.OK.value()) {
			ApiResponse.error(response.getStatus(), CommonResponseCode.SYSTEM_ERROR);
		}
		return ApiResponse.error(CommonResponseCode.SYSTEM_ERROR);
	}

	/**
	 * 无去拿先访问
	 **/
	@ExceptionHandler(ExpiredJwtException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiResponse handleExpireJwtException(ExpiredJwtException e) {
		log.warn("Session Expired", e);
		return ApiResponse.error(CommonResponseCode.LOGIN_EXPIRED);
	}


	/**
	 * 自定义错误, Http Status 为422
	 **/
	@ExceptionHandler(AppException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ApiResponse appExceptionHandler(HttpServletResponse response, AppException e) {
		if (response.getStatus() != HttpStatus.OK.value()) {
			return ApiResponse.error(response.getStatus(), e.getCode(), e.getMessage());
		}
		return ApiResponse.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getCode(), e.getMessage());
	}

	@ExceptionHandler(InsufficientAuthenticationException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiResponse handleInsufficientAuthenticationException(HttpServletResponse response, InsufficientAuthenticationException e) {
		// 在这里，您可以自定义要返回的错误代码和消息
		return ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "your_error_code", e.getMessage());
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
	public ApiResponse maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException e) {
		log.warn("Max Upload Size Exceeded", e);
		return ApiResponse.error(CommonResponseCode.ACCESS_DENIED);
	}

	@ExceptionHandler(MultipartException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse multipartExceptionHandler(MultipartException e) {
		log.warn("Multipart", e);
		return ApiResponse.error(CommonResponseCode.UPLOAD_FILE_FAIL);
	}

	@ExceptionHandler(SocketTimeoutException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
	public ApiResponse socketTimeoutExceptionHandler(SocketTimeoutException e) {
		log.warn("Socket Timeout", e);
		return ApiResponse.error(CommonResponseCode.REQ_TIMEOUT);
	}

	/**
	 * HttpRequestMethodNotSupportedException 请求方法不支持
	 **/
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public ApiResponse requestMethodNotSupportedHandler(HttpRequestMethodNotSupportedException e) {
		log.warn("Request Method Not Support", e);
		return ApiResponse.error(HttpStatus.METHOD_NOT_ALLOWED.value(),
			CommonResponseCode.SYSTEM_ERROR, "不支持" + e.getMethod() + "请求");
	}

	/**
	 * HttpMediaTypeNotSupportedException 请求体中Content-Type不支持时出现
	 **/
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public ApiResponse httpMediaTypeNotSupportedHandler(HttpMediaTypeNotSupportedException e) {
		log.warn("Http Media Type Not Supported", e);
		return ApiResponse.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
			CommonResponseCode.SYSTEM_ERROR, "不支持Type为 " + e.getContentType() + " 的请求");
	}

	/**
	 * NoHandlerFoundException 请求路径无法找到时出现
	 **/
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse noHandlerExceptionHandler(NoHandlerFoundException e) {
		log.warn("No Handler Found", e);
		return ApiResponse.error(HttpStatus.NOT_FOUND.value(),
			CommonResponseCode.SYSTEM_ERROR, "请求路径错误");
	}

	/**
	 * ConstraintViolationException 参数不满足要求，则跑出异常ConstraintViolationException
	 * HttpMessageNotReadableException Post JSON的方式调用服务时，未对请求参数做json序列化
	 * TypeMismatchException 参数类型不匹配
	 * IllegalArgumentException 错误的请求参数
	 **/
	@ExceptionHandler({
		HttpMessageNotReadableException.class,
		TypeMismatchException.class,
		IllegalArgumentException.class
	})
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse multiBadRequestExceptionHandler(RuntimeException e) {
		log.warn("Multi Bad Request", e);
		return ApiResponse.error(CommonResponseCode.INVALID_PARAMS,
			StrUtil.isBlank(e.getMessage()) ? "请求无效, 参数错误" : e.getMessage());
	}

	/**
	 * BindException 绑定注解校验规则
	 **/
	@ExceptionHandler(BindException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse bindExceptionExceptionHandler(BindException e) {
		log.warn("Bind Argument", e);
		String errorMsg = Optional.ofNullable(e.getBindingResult().getFieldError()).map(err ->
			err.contains(TypeMismatchException.class) ?
				String.format("参数类型错误, 参数名: %s", err.getField()) :
				err.getField() + " " + err.getDefaultMessage()
		).orElse("参数类型错误");

		return ApiResponse.error(CommonResponseCode.INVALID_PARAMS, errorMsg);
	}

	/**
	 * MethodArgumentTypeMismatchException 方法参数类型不匹配异常
	 **/
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse methodArgumentTypeMismatchExceptionHandler(
		MethodArgumentTypeMismatchException e) {
		log.warn("Method Argument Type Mismatch", e);
		return ApiResponse.error(CommonResponseCode.INVALID_PARAMS, String.format("参数类型错误, 参数名: %s", e.getName())
		);
	}

	/**
	 * MissingServletRequestParameterException 请求参数缺失时出现
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse missingServletRequestParameterExceptionHandler(
		MissingServletRequestParameterException e) {
		log.warn("Missing Parameter", e);
		return ApiResponse.error(CommonResponseCode.INVALID_PARAMS, String.format("参数缺失[%s - %s]", e.getParameterName(), e.getParameterType()));
	}

	/**
	 * AuthenticationException 认证异常
	 */
	@ExceptionHandler(AuthenticationException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiResponse handleAuthenticationException(AuthenticationException e) {
		log.warn("AuthenticationException ", e);
		return ApiResponse.error(CommonResponseCode.WEB_UNAUTHORIZED);
	}

	/**
	 * AuthenticationException 认证异常
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ApiResponse handleAccessDeniedException(AccessDeniedException e) {
		log.warn("AccessDeniedException ", e);
		return ApiResponse.error(CommonResponseCode.ACCESS_DENIED);
	}


}
