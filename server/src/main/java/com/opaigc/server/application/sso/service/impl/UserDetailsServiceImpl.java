package com.opaigc.server.application.sso.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import com.opaigc.server.application.sso.domain.dto.AccountLoginDto;
import com.opaigc.server.application.user.domain.User;
import com.opaigc.server.application.user.service.UserService;
import com.opaigc.server.infrastructure.common.Constants;
import com.opaigc.server.infrastructure.http.CommonResponseCode;
import com.opaigc.server.infrastructure.redis.RedisUtil;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author: Runner.dada
 * @date: 2020/12/12
 * @description: System User Detail Service Implements
 **/
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final String SYSTEM_USER_LOGIN_TIMES = "system_user.login.times.%s";
	private final UserService userService;
	private final RedisUtil redisUtil;
	@Value("login.max-fail-times")
	private String maxFailTimes;

	public UserDetailsServiceImpl(UserService UserService,
																RedisUtil redisUtil) {
		this.userService = UserService;
		this.redisUtil = redisUtil;
	}

	public String getRemoteAddress() {
		RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
		if (attribs instanceof NativeWebRequest) {
			HttpServletRequest request = (HttpServletRequest) ((NativeWebRequest) attribs).getNativeRequest();
			return request.getRemoteAddr();
		}
		return null;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		checkLoginTimes(username);

		return userService.getByUsername(username).map(u -> {

			User updatedUser = new User();
			updatedUser.setLastLoginDate(LocalDateTime.now());
			updatedUser.setLastLoginIp(getRemoteAddress());
			updatedUser.setId(u.getId());
			userService.updateById(updatedUser);

			AccountLoginDto systemUserLoginDto = new AccountLoginDto();
			BeanUtils.copyProperties(u, systemUserLoginDto);
			systemUserLoginDto.setUser(u);
			return systemUserLoginDto;
		}).orElseThrow(() ->
			new UsernameNotFoundException(CommonResponseCode.LOGIN_ACCOUNT_NOT_FOUND.getMessage()));
	}

	private void checkLoginTimes(String username) {
		if (StrUtil.isBlank(username)) {
			return;
		}
		String key = String.format(SYSTEM_USER_LOGIN_TIMES, username);
		String loginStatus = redisUtil.get(key);
		if (Objects.equals(Constants.LOGIN_LOCKED_VALUE, loginStatus)) {
			Long minutes = redisUtil.getExpire(key, TimeUnit.MINUTES);
			String msg = String.format(CommonResponseCode.LOGIN_LOCKED.getMessage(),
				maxFailTimes, ++minutes);
			throw new LockedException(msg);
		}
	}
}
