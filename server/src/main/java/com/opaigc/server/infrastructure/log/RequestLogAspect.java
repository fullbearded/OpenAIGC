package com.opaigc.server.infrastructure.log;

import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import com.opaigc.server.infrastructure.exception.AppException;
import com.opaigc.server.infrastructure.http.ApiResponse;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Runner.dada
 * @date: 2020/12/13
 * @description: 统一日志打印
 **/
@Aspect
@Slf4j
public class RequestLogAspect {
	private final static Integer MAX_LOG_SIZE = 2048 * 2;

	private static final Set<Class<?>> EXCLUDE_CLASSES = Set.of(ServletRequest.class, ServletResponse.class, MultipartFile.class);

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) " +
		"||@annotation(org.springframework.web.bind.annotation.DeleteMapping) " +
		"||@annotation(org.springframework.web.bind.annotation.PutMapping) " +
		"||@annotation(org.springframework.web.bind.annotation.GetMapping) ")
	public void pointCut() {
	}

	@Around("pointCut()")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		request.setAttribute("params", this.getUserArgs(joinPoint.getArgs()));

		StopWatch stopwatch = StopWatch.create(StrUtil.EMPTY);
		String requestLog = buildRequestLog(joinPoint);

		Object proceed;
		try {
			proceed = joinPoint.proceed(joinPoint.getArgs());
		} catch (AppException e) {
			stopwatch.stop();
			log.warn("API Log, AppException, url = {}, params = {}, time cost = {} ms, AppException:",
				request.getRequestURI(), requestLog, stopwatch.getLastTaskTimeMillis(), e);
			throw e;
		} catch (Exception e) {
			stopwatch.stop();
			log.error("API Log, Exception, url = {}, arams = {}, time cost = {} ms, Exception:",
				request.getRequestURI(), requestLog, stopwatch.getLastTaskTimeMillis(), e);
			throw e;
		}

		stopwatch.stop();

		log.info("api log, url = {}, params = {}, resp = {}, time cost = {} ms",
			request.getRequestURI(), requestLog,
			buildResponseLog(proceed),
			stopwatch.getLastTaskTimeMillis());

		return proceed;
	}

	protected List<Object> getUserArgs(Object[] args) {
		return Arrays.stream(args).filter((p) ->
			Objects.nonNull(p) && EXCLUDE_CLASSES.stream().noneMatch(clz -> clz.isAssignableFrom(p.getClass()))
		).collect(Collectors.toList());
	}

	private String buildRequestLog(ProceedingJoinPoint joinPoint) {
		return Strings.left(JSONUtil.toJsonStr(this.getUserArgs(joinPoint.getArgs())), MAX_LOG_SIZE);
	}

	protected String buildResponseLog(Object proceed) {
		if (proceed == null || !(ApiResponse.class.isAssignableFrom(proceed.getClass()))) {
			return Strings.EMPTY;
		}
		return Strings.left(JSONUtil.toJsonStr(proceed), MAX_LOG_SIZE);
	}
}
