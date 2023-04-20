package com.opaigc.server.application.sso.filter;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import com.opaigc.server.application.sso.domain.AccountType;
import com.opaigc.server.infrastructure.common.Constants;
import com.opaigc.server.infrastructure.http.ApiResponse;
import com.opaigc.server.infrastructure.jwt.JwtTokenProvider;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author: Runner.dada
 * @date: 2020/12/13
 * @description:
 **/
public class JwtUserAuthenticationFilter extends AbstractJwtAuthenticationFilter {

	public JwtUserAuthenticationFilter(
		AuthenticationManager authenticationManager,
		ApplicationContext applicationContext, String urlPath, JwtTokenProvider jwtTokenProvider) {
		super(authenticationManager, applicationContext, urlPath, jwtTokenProvider);
	}

	@Override
	protected String getAccountType() {
		return AccountType.USER.name();
	}

	@Override
	protected void setResponse(HttpServletResponse response, Authentication authentication,
														 String token) throws IOException {
		response.setHeader(Constants.AUTHORIZATION_HEADER, token);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpStatus.OK.value());
		ApiResponse resp = ApiResponse.success(Map.of("token", token));
		response.getWriter().print(JSONUtil.toJsonStr(resp));
	}
}
