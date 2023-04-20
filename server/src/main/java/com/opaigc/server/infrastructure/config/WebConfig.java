package com.opaigc.server.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import com.opaigc.server.infrastructure.auth.AccountSessionResolver;

import java.util.Objects;

/**
 * @author: Runner.dada
 * @date: 2020/12/27
 * @description:
 **/
//@Configuration
//public class WebConfig implements WebFluxConfigurer {
//
//
//	private final HandlerMethodArgumentResolver[] handlerMethodArgumentResolvers;
//
//	@Autowired(required = false)
//	public WebConfig(HandlerMethodArgumentResolver[] handlerMethodArgumentResolvers) {
//		this.handlerMethodArgumentResolvers = handlerMethodArgumentResolvers;
//	}
//
//	@Override
//	public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
//		if (Objects.nonNull(handlerMethodArgumentResolvers)) {
//			configurer.addCustomResolver(handlerMethodArgumentResolvers);
//		}
//		configurer.addCustomResolver(new AccountSessionResolver());
//	}
//
//	@Override
//	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
//		configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024); // 可根据需要设置
//	}
//}
