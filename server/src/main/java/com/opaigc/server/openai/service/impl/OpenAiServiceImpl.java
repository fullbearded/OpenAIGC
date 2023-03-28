package com.opaigc.server.openai.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opaigc.server.config.AppConfig;
import com.opaigc.server.openai.client.OpenAiClient;
import com.opaigc.server.openai.domain.chat.Message;
import com.opaigc.server.openai.domain.chat.MessageQuestion;
import com.opaigc.server.openai.domain.chat.MessageType;
import com.opaigc.server.openai.listener.OpenAISubscriber;
import com.opaigc.server.openai.service.OpenAiService;

import java.util.Objects;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 描述
 *
 * @author Runner.dada
 * @date 2023/3/23
 */
@Service
public class OpenAiServiceImpl implements OpenAiService {

    @Autowired
    private AppConfig appConfig;

    @Override
    public Flux<String> chatSend(MessageType type, String prompt, String sessionId) {
        OpenAiClient openAiClient = buildClient();

        MessageQuestion userMessage = new MessageQuestion(MessageType.TEXT, prompt);

        return Flux.create(emitter -> {
            OpenAISubscriber subscriber = new OpenAISubscriber(emitter, sessionId, this, userMessage);
            Flux<String> openAiResponse =
                    openAiClient.getChatResponse(appConfig.getApiKey(), sessionId, prompt, null, null, null);
            openAiResponse.subscribe(subscriber);
            emitter.onDispose(subscriber);
        });
    }

    @Override
    public CreditGrantsResponse creditGrants(String key) {
        OpenAiClient client = buildClient();
        return client.getCredit(Objects.isNull(key) ? appConfig.getApiToken() : key).block();
    }

    @Override
    public ModerationData moderation(String prompt) {
        OpenAiClient client = buildClient();
        Mono<ModerationData> toMono = client.getModeration(appConfig.getApiToken(), prompt);
        return toMono.block();
    }

    @Override
    public Mono<Boolean> checkContent(String prompt) {
        OpenAiClient client = buildClient();
        return client.checkContent(appConfig.getApiToken(), prompt);
    }

    private OpenAiClient buildClient() {
        return new OpenAiClient(appConfig);
    }

    @Override
    public void completed(MessageQuestion questions, String sessionId, String response) {

    }

    @Override
    public void fail(String sessionId) {

    }
}
