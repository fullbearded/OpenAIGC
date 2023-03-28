package com.opaigc.server.openai.listener;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import com.alibaba.fastjson.JSON;
import com.opaigc.server.openai.domain.chat.ChatCompletionResponse;
import com.opaigc.server.openai.domain.chat.Message;
import com.opaigc.server.openai.domain.chat.MessageQuestion;
import com.opaigc.server.openai.domain.chat.MessageResponse;
import com.opaigc.server.openai.domain.chat.MessageType;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.FluxSink;

/**
 * @author: Runner.dada
 * @date: 2023/3/23
 * @description: OpenAI 数据订阅者，实现Subscriber接口
 **/
@Slf4j
public class OpenAISubscriber implements Subscriber<String>, Disposable {
    private final FluxSink<String> emitter;
    private Subscription subscription;
    private final String sessionId;
    private final CompletedCallBack completedCallBack;
    private final StringBuilder sb = new StringBuilder();
    private final MessageQuestion questions;

    public OpenAISubscriber(FluxSink<String> emitter, String sessionId, CompletedCallBack completedCallBack, MessageQuestion questions) {
        this.emitter = emitter;
        this.sessionId = sessionId;
        this.completedCallBack = completedCallBack;
        this.questions = questions;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(String data) {
        log.info("OpenAI返回数据：{}", data);
        if ("[DONE]".equals(data)) {
            log.info("OpenAI返回数据结束了");
            subscription.request(1);
            emitter.next(JSON.toJSONString(new MessageResponse(MessageType.TEXT, "", true)));
            completedCallBack.completed(questions, sessionId, sb.toString());
            emitter.complete();
        } else {
            ChatCompletionResponse openAiResponse = JSON.parseObject(data, ChatCompletionResponse.class);
            String content = Optional.ofNullable(openAiResponse.getChoices().get(0).getDelta().getContent()).orElse("");
            emitter.next(JSON.toJSONString(new MessageResponse(MessageType.TEXT, content, false)));
            sb.append(content);
            subscription.request(1);
        }

    }

    @Override
    public void onError(Throwable t) {
        log.error("OpenAI返回数据异常：{}", t.getMessage());
        emitter.error(t);
        completedCallBack.fail(sessionId);
    }

    @Override
    public void onComplete() {
        log.info("OpenAI返回数据完成");
        emitter.complete();
    }

    @Override
    public void dispose() {
        log.warn("OpenAI返回数据关闭");
        emitter.complete();
    }
}