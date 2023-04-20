package com.opaigc.server.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.server.WebFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.opaigc.server.infrastructure.auth.AccountSessionResolver;

import java.util.List;

/**
 * 描述
 *
 * @author huhongda@fiture.com
 * @date 2023/4/9
 */

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new AccountSessionResolver());
	}

	/**
	 * 全局异常处理, 处理 Webflux 异常导致的错误，比如 WebClientRequestException
	 * 避免 Spring Security 重定向到 /error
	 **/
//	@Bean
//	@Order(-1)
//	public WebFilter webFluxFilterExceptionFilter() {
//		return new WebFluxFilterExceptionFilter();
//	}

}
