package com.opaigc.server.infrastructure.auth;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import com.opaigc.server.application.sso.domain.dto.AccountLoginDto;

import cn.hutool.json.JSONUtil;
import java.util.Objects;

/**
 * @author: Runner.dada
 * @date: 2020/12/27
 * @description: 获取Session的自定义参数解析器
 **/
public class AccountSessionResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(AccountSession.class);
	}


	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
																NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (Objects.isNull(authentication)) {
			return null;
		}
		AccountLoginDto systemUserLoginDto = JSONUtil.toBean(authentication.getPrincipal().toString(), AccountLoginDto.class);
		AccountSession accountSession = new AccountSession();
		BeanUtils.copyProperties(systemUserLoginDto, accountSession);
		return accountSession;
	}
}
