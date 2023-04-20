package com.opaigc.server.application.sso.filter;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.opaigc.server.application.sso.domain.AccountType;
import com.opaigc.server.application.sso.domain.dto.AccountLoginDto;
import com.opaigc.server.application.sso.utils.LoginUtil;
import com.opaigc.server.infrastructure.common.Constants;
import com.opaigc.server.infrastructure.exception.AppException;
import com.opaigc.server.infrastructure.http.CommonResponseCode;
import com.opaigc.server.infrastructure.jwt.JwtTokenProvider;
import com.opaigc.server.infrastructure.redis.RedisUtil;

import cn.hutool.json.JSONUtil;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Runner.dada
 * @date: 2020/12/13
 * @description:
 **/
@Slf4j
public abstract class AbstractJwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final JwtTokenProvider jwtTokenProvider;

	private final RedisUtil redisUtil;

	private final UserDetailsService userDetailsService;

	public AbstractJwtAuthorizationFilter(
		AuthenticationManager authenticationManager, RedisUtil redisUtil,
		JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.redisUtil = redisUtil;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
																	FilterChain chain) throws IOException, ServletException {
		String authorization = request.getHeader(Constants.AUTHORIZATION_HEADER);

		if (Objects.isNull(authorization)) {
			chain.doFilter(request, response);
			return;
		}
		UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(authorization);

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		super.doFilterInternal(request, response, chain);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String authorization) {
		String authToken = authorization.replace(Constants.AUTHORIZATION_BEARER_PREFIX, StringUtil.EMPTY_STRING);

		// 1.通过jwt的签名获取登录用户名
		String username = jwtTokenProvider.getUsername(authToken);
		log.info("check username {}", username);

		// 2.判断token是否有效以及是否获取到用户名
		if (Objects.isNull(username) || jwtTokenProvider.invalidToken(authToken)) {
			return null;
		}

		// 3.判断是否token是否过期
		String storeToken = redisUtil.get(LoginUtil.buildStoreTokenKey(AccountType.valueOfName(getAccountType()), username));
		if (!Objects.equals(authToken, storeToken)) {
			throw new AppException(CommonResponseCode.ACCOUNT_LOGIN_OTHER_DEVICE);
		}
		// 4.判断账户是否被禁用
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		AccountLoginDto systemUserLoginDto = (AccountLoginDto) userDetails;
		if (systemUserLoginDto.isBanned()) {
			throw new AppException(CommonResponseCode.ACCOUNT_BANNED);
		}
		systemUserLoginDto.setToken(authToken);
		return new UsernamePasswordAuthenticationToken(JSONUtil.toJsonStr(systemUserLoginDto), null, Collections.emptyList());
	}

	protected abstract String getAccountType();

}
