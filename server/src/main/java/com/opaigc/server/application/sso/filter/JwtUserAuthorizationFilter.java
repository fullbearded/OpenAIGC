package com.opaigc.server.application.sso.filter;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.opaigc.server.application.sso.domain.AccountType;
import com.opaigc.server.infrastructure.jwt.JwtTokenProvider;
import com.opaigc.server.infrastructure.redis.RedisUtil;

/**
 * @author: Runner.dada
 * @date: 2020/12/13
 * @description:
 **/
public class JwtUserAuthorizationFilter extends AbstractJwtAuthorizationFilter {


	public JwtUserAuthorizationFilter(
		AuthenticationManager authenticationManager, RedisUtil redisUtil,
		JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
		super(authenticationManager, redisUtil, jwtTokenProvider, userDetailsService);
	}

	@Override
	protected String getAccountType() {
		return AccountType.USER.name();
	}
}
