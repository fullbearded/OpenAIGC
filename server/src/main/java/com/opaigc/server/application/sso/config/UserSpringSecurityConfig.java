package com.opaigc.server.application.sso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.opaigc.server.application.sso.filter.JwtUserAuthenticationFilter;
import com.opaigc.server.application.sso.filter.JwtUserAuthorizationFilter;
import com.opaigc.server.application.sso.handler.CustomLogoutSuccessHandler;
import com.opaigc.server.infrastructure.common.Constants;
import com.opaigc.server.infrastructure.jwt.JwtTokenProvider;
import com.opaigc.server.infrastructure.redis.RedisUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: Runner.dada
 * @date: 2020/12/13
 * @description: Scrm config，for System User Security
 **/
@Configuration
@Import(SpringSecurityConfig.class)
@Slf4j
@Order(1)
public class UserSpringSecurityConfig {

	private final UserDetailsService userDetailsService;

	private final PasswordEncoder passwordEncoder;

	private final ApplicationContext applicationContext;

	private final RedisUtil redisUtil;

	private final JwtTokenProvider jwtTokenProvider;

	private final CustomLogoutSuccessHandler customLogoutHandler;


	@Autowired(required = false)
	public UserSpringSecurityConfig(
		@Qualifier("UserDetailsService") UserDetailsService userDetailsService,
		PasswordEncoder passwordEncoder, ApplicationContext applicationContext,
		RedisUtil redisUtil, JwtTokenProvider jwtTokenProvider,
		CustomLogoutSuccessHandler customLogoutHandler
	) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
		this.applicationContext = applicationContext;
		this.redisUtil = redisUtil;
		this.jwtTokenProvider = jwtTokenProvider;
		this.customLogoutHandler = customLogoutHandler;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));

		return http
			.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
			.headers(headers -> headers.frameOptions().disable())
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(logout -> logout
				.logoutUrl(Constants.USER_LOGOUT_PATH)
				.logoutSuccessHandler(new CustomLogoutSuccessHandler(applicationContext, jwtTokenProvider))
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(Constants.URI_WHITELIST).permitAll()
				.requestMatchers(HttpMethod.GET, Constants.STATIC_WHITELIST).permitAll()
				.requestMatchers(Constants.SWAGGER_WHITELIST).anonymous()
				.anyRequest().authenticated()
			)
			.addFilter(new JwtUserAuthenticationFilter(authenticationManager, applicationContext, Constants.USER_LOGIN_PATH, jwtTokenProvider))
			.addFilter(new JwtUserAuthorizationFilter(authenticationManager, redisUtil, jwtTokenProvider, userDetailsService))
			.build();
	}
}
