package com.opaigc.server.openai.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
/**
 * 描述
 *
 * @author Runner.dada
 * @date 2023/3/23
 */
@Component
@Order(2)
public class RequestLoggingFilter implements org.springframework.web.server.WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String method = exchange.getRequest().getMethodValue();
        String path = exchange.getRequest().getURI().getPath();
        HttpHeaders headers = exchange.getRequest().getHeaders();

        logger.info("Received {} request to {}", method, path);
        headers.forEach((key, value) -> logger.debug("{}: {}", key, value));

        return chain.filter(exchange);
    }
}
