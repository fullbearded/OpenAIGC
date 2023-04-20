package com.opaigc.server.application.sso.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.alibaba.fastjson.JSONObject;
import com.opaigc.server.infrastructure.exception.AppException;
import com.opaigc.server.infrastructure.http.ApiResponse;
import com.opaigc.server.infrastructure.http.CommonResponseCode;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 描述
 *
 * @author huhongda@fiture.com
 * @date 2023/4/8
 */

@Component
public class GlobalExceptionHandlerFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlerFilter.class);

	public void handler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception e) throws
		IOException {
		ApiResponse apiResponse = null;
		Integer httpStatus = 200;
		if (e instanceof AppException) {
			apiResponse = ApiResponse.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ((AppException) e).getCode(), e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY.value();
		} else if (e.getCause() instanceof AppException) {
			apiResponse =
				ApiResponse.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ((AppException) e.getCause()).getCode(), e.getCause().getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY.value();
		} else {
			logger.error("GlobalExceptionHandlerFilter", e);
			apiResponse =
				ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), CommonResponseCode.SYSTEM_ERROR.getCode(), e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
		}
		httpServletResponse.setStatus(httpStatus);
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.getWriter().write(JSONObject.toJSONString(apiResponse)); // 转换 ApiResponse 为 JSON 格式
		httpServletResponse.getWriter().flush();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws ServletException, IOException {
		try {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} catch (Exception e) {
			this.handler(httpServletRequest, httpServletResponse, e);
		}
	}
}
