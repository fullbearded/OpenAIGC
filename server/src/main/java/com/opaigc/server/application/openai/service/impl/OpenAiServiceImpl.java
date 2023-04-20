package com.opaigc.server.application.openai.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.opaigc.server.application.openai.client.OpenAiClient;
import com.opaigc.server.application.openai.domain.chat.MessageQuestion;
import com.opaigc.server.application.openai.domain.chat.MessageType;
import com.opaigc.server.application.openai.listener.OpenAISubscriber;
import com.opaigc.server.application.openai.service.OpenAiService;
import com.opaigc.server.application.user.domain.App;
import com.opaigc.server.application.user.event.ChatStreamCompletedEvent;
import com.opaigc.server.application.user.service.AppService;
import com.opaigc.server.config.AppConfig;
import com.opaigc.server.infrastructure.common.Constants;

import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 描述
 *
 * @author Runner.dada
 * @date 2023/3/23
 */
@Service
@Slf4j
public class OpenAiServiceImpl implements OpenAiService {

	@Autowired
	private AppConfig appConfig;
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private AppService appService;

	/**
	 * @param parameters ChatParameters
	 * @return Flux<String>
	 **/
	@Override
	public Flux<String> chatSend(ChatParameters parameters) {
		OpenAiClient openAiClient = buildClient();

		App app = appService.getByCode(parameters.getAppCode());
		MessageQuestion userMessage = new MessageQuestion(MessageType.TEXT, parameters.getMessages(),
			parameters.getRemoteIp(), parameters.getChatType(), Optional.ofNullable(app).map(App::getId).orElse(null));
		return sendToOpenAi(parameters.getSessionId(), openAiClient, userMessage);
	}

	private Flux<String> sendToOpenAi(String sessionId, OpenAiClient openAiClient, MessageQuestion userMessage) {
		return Flux.create(emitter -> {
			OpenAISubscriber subscriber = new OpenAISubscriber(emitter, sessionId, this, userMessage);
			Flux<String> openAiResponse =
				openAiClient.getChatResponse(appConfig.getApiToken(), sessionId, userMessage.getMessages(), null, null, null);
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
		ChatStreamCompletedEvent event = ChatStreamCompletedEvent.builder()
			.sessionId(sessionId)
			.questions(questions)
			.response(response)
			.build();
		redisTemplate.convertAndSend(Constants.CHAT_STREAM_COMPLETED_TOPIC, JSONObject.toJSONString(event));
		log.info("Chat Completed: {}", JSONObject.toJSONString(event));
	}

	@Override
	public void fail(String sessionId) {

	}
}
