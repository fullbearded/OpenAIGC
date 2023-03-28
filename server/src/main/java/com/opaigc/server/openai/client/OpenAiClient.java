package com.opaigc.server.openai.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.alibaba.fastjson.JSONObject;
import com.opaigc.server.config.AppConfig;
import com.opaigc.server.openai.service.OpenAiService;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.util.Collections;
import java.util.Optional;
import javax.net.ssl.SSLException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

/**
 * @author: Runner.dada
 * @date: 2023/3/23
 * @description:
 **/
@Slf4j
public class OpenAiClient {

    private final AppConfig appConfig;

    private WebClient webClient;

    public OpenAiClient(AppConfig appConfig) {
        this.appConfig = appConfig;
        String env = Optional.ofNullable(appConfig.getEnv()).orElse("local");
        if (!env.equals("prod")) {
            initWithProxy();
        } else {
            initWithNoProxy();
        }
    }

    public void initWithProxy() {
        log.info("initDev");
        SslContext sslContext = null;
        try {
            sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }

        SslContext finalSslContext = sslContext;
        HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(finalSslContext))
                .tcpConfiguration(tcpClient -> tcpClient.proxy(proxy ->
                        proxy.type(ProxyProvider.Proxy.HTTP).host(appConfig.getProxy().getHost()).port(appConfig.getProxy().getPort())));

        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
        this.webClient = WebClient.builder().clientConnector(connector)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    public void initWithNoProxy() {
        log.info("initProd");
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    public Flux<String> getChatResponse(String authorization, String user, String prompt, Integer maxTokens, Double temperature,
                                        Double topP) {
        JSONObject params = new JSONObject();

        params.put("model", "gpt-3.5-turbo");
        params.put("max_tokens", maxTokens);
        params.put("stream", true);
        params.put("temperature", temperature);
        params.put("top_p", topP);
        params.put("user", user);
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        params.put("messages", Collections.singleton(message));

        return webClient.post()
                .uri(appConfig.getApiHost() + Api.CHAT_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authorization)
                .bodyValue(params.toJSONString())
                .retrieve()
                .bodyToFlux(String.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    HttpStatus status = (HttpStatus) ex.getStatusCode();
                    String res = ex.getResponseBodyAsString();
                    log.error("OpenAI API error: {} {}", status, res);
                    return Mono.error(new RuntimeException(res));
                });

    }


    public Mono<OpenAiService.CreditGrantsResponse> getCredit(String authorization) {
        Mono<OpenAiService.CreditGrantsResponse> toMono = webClient.get()
                .uri(appConfig.getApiHost() + Api.CREDIT_GRANTS_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authorization)
                .retrieve()
                .bodyToMono(OpenAiService.CreditGrantsResponse.class);
        return toMono;
    }

    public Mono<OpenAiService.ModerationData> getModeration(String authorization, String prompt) {
        JSONObject params = new JSONObject();
        params.put("input", prompt);
        Mono<OpenAiService.ModerationData> toMono = webClient.post()
                .uri(appConfig.getApiHost() + Api.CONTENT_AUDIT_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authorization)
                .bodyValue(params.toJSONString())
                .retrieve()
                .bodyToMono(OpenAiService.ModerationData.class);
        return toMono;
    }

    public Mono<Boolean> checkContent(String authorization, String prompt) {
        Mono<OpenAiService.ModerationData> resp = getModeration(authorization, prompt);
        OpenAiService.ModerationData jsonObject = resp.block();
        return Mono.just(jsonObject.getResults().get(0).isFlagged());
    }
}
