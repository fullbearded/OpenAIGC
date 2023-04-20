package com.opaigc.server.application.sso.filter;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.opaigc.server.application.sso.domain.AccountType;
import com.opaigc.server.application.sso.domain.dto.LoginUserDto;
import com.opaigc.server.application.sso.event.AccountAuthenticationFailureEvent;
import com.opaigc.server.application.sso.event.AccountAuthenticationSuccessEvent;
import com.opaigc.server.application.sso.vo.LoginUserVo;
import com.opaigc.server.infrastructure.http.ApiResponse;
import com.opaigc.server.infrastructure.http.CommonResponseCode;
import com.opaigc.server.infrastructure.jwt.JwtTokenProvider;
import com.opaigc.server.infrastructure.utils.HttpServletRequestUtil;

import cn.hutool.json.JSONUtil;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Runner.dada
 * @date: 2020/12/13
 * @description: the Abstract jwt auth filter
 **/
@Slf4j
public abstract class AbstractJwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final JwtTokenProvider jwtTokenProvider;

	private final AuthenticationManager authenticationManager;

	private final ApplicationContext applicationContext;


	public AbstractJwtAuthenticationFilter(AuthenticationManager authenticationManager,
																				 ApplicationContext applicationContext, String urlPath, JwtTokenProvider jwtTokenProvider) {
		this.authenticationManager = authenticationManager;
		this.applicationContext = applicationContext;
		this.jwtTokenProvider = jwtTokenProvider;
		super.setFilterProcessesUrl(urlPath);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
																							HttpServletResponse response) {
		try {
			LoginUserVo loginUser = JSONUtil
				.toBean(HttpServletRequestUtil.getRequestBody(request), LoginUserVo.class);
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				loginUser.getUsername(), loginUser.getPassword());
			SecurityContextHolder.getContext().setAuthentication(authRequest);
			return authenticationManager.authenticate(authRequest);
		} catch (IOException e) {
			log.error("账号密码检测失败{}, {}", e.getMessage(), e);
			return null;
		}
	}

	protected void setResponse(HttpServletResponse response, Authentication authentication,
														 String token) throws IOException {
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
																					HttpServletResponse response, FilterChain chain, Authentication authentication)
		throws IOException {
		LoginUserDto user = (LoginUserDto) authentication.getPrincipal();

		String token = jwtTokenProvider.createToken(user.getUsername(), user.getCode(), null);
		setResponse(response, authentication, token);
		publishAccountAuthenticationSuccessEvent(authentication, token);
	}

	private void publishAccountAuthenticationSuccessEvent(
		Authentication authentication, String token) {
		try {
			LoginUserDto loginUserDto = (LoginUserDto) authentication.getPrincipal();

			AccountAuthenticationSuccessEvent event =
				new AccountAuthenticationSuccessEvent(loginUserDto.getUsername());
			event.setUsername(loginUserDto.getUsername());
			event.setAccountType(AccountType.valueOfName(getAccountType()));
			event.setToken(token);

			applicationContext.publishEvent(event);
		} catch (Exception e) {
			log.error("publish authentication success event exception {}, {}, {}",
				JSONUtil.toJsonStr(authentication), e.getMessage(), e);
		}
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
																						HttpServletResponse response, AuthenticationException authenticationException)
		throws IOException {

		if (authenticationException instanceof BadCredentialsException) {
			publishAccountAuthenticationFailureEvent();
		}

		ApiResponse resp = ApiResponse.error(CommonResponseCode.LOGIN_ACCOUNT_NOT_FOUND);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter().write(JSONUtil.toJsonStr(resp));
	}

	private void publishAccountAuthenticationFailureEvent() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = Optional.ofNullable(authentication).map(Authentication::getPrincipal)
			.map(String::valueOf).orElse(StringUtil.EMPTY_STRING);

		try {
			log.info("Publish authentication failure event with {}", username);
			AccountAuthenticationFailureEvent event = new AccountAuthenticationFailureEvent(username);
			event.setAccountType(AccountType.valueOfName(getAccountType()));
			event.setUsername(username);
			applicationContext.publishEvent(event);
		} catch (Exception e) {
			log.error("When publish authentication failure event with {} error occurred {}",
				username, e.getMessage(), e);
		}
	}

	/**
	 * 账户类型
	 **/
	protected abstract String getAccountType();

}
