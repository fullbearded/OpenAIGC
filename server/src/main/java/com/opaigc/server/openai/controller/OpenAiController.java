package com.opaigc.server.openai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.opaigc.server.infrastructure.http.ApiResponse;
import com.opaigc.server.openai.domain.chat.MessageType;
import com.opaigc.server.openai.service.OpenAiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * @author: Runner.dada
 * @date: 2023/3/23
 * @description:
 **/
@Slf4j
@RestController
@RequestMapping({"/api"})
@RequiredArgsConstructor
public class OpenAiController {

    @Autowired
    private OpenAiService openAiService;

    @Autowired
    private final MessageSource messageSource;

    /**
     * Chat 流式返回
     */
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin
    public Flux<String> streamCompletions(@RequestBody CompletionsRequest req) {
        return openAiService.chatSend(MessageType.TEXT, req.getPrompt(), req.getUser());
    }

    @PostMapping("/dashboard/billing/credit")
    public ApiResponse creditGrants(@RequestBody CreditRequest req) {
        return ApiResponse.success(openAiService.creditGrants(req.getKey()));
    }

    @PostMapping("/chat/moderation")
    public ApiResponse moderation(@RequestBody @Valid ModerationRequest req) {
        OpenAiService.ModerationData moderation = openAiService.moderation(req.getPrompt());
        List<String> errors = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : moderation.getResults().get(0).getCategories().entrySet()) {
            String key = entry.getKey();
            Boolean value = entry.getValue();
            if (value) {
                errors.add(messageSource.getMessage("openai.moderation." + key, null, Locale.getDefault()));
            }
        }
        return ApiResponse.success(ModerationResponse.builder().errorMessages(errors).source(moderation).build());
    }

    @PostMapping("/chat/moderation/status")
    public ApiResponse checkContent(@RequestBody @Validated ModerationRequest req) {
        return ApiResponse.success(new JSONObject().fluentPut("status", openAiService.checkContent(req.getPrompt()).block()));
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CompletionsRequest {
        private String user;
        private String prompt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ModerationResponse {
        private OpenAiService.ModerationData source;
        private List<String> errorMessages;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModerationRequest {
        @NotNull(message = "prompt not null")
        @NotNull(message = "prompt not blank")
        private String prompt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditRequest {
        private String key;
    }
}
