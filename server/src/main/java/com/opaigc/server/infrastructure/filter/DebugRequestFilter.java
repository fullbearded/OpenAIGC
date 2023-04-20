package com.opaigc.server.infrastructure.filter;

import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DebugRequestFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
		requestWrapper.getInputStream().mark(0);
		// 调用 chain.doFilter 以确保请求被处理并缓存
		chain.doFilter(requestWrapper, response);

		// 获取并打印请求体内容
		String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
		System.out.println("Request body: " + requestBody);

		// 获取并打印字符编码
		System.out.println("Character encoding: " + request.getCharacterEncoding());
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}
}
