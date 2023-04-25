package com.opaigc.server.application.openai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opaigc.server.application.openai.domain.chat.MessageType;
import com.opaigc.server.application.openai.service.OpenAiService;
import com.opaigc.server.application.user.domain.App;
import com.opaigc.server.application.user.domain.UserChat;
import com.opaigc.server.application.user.service.AppService;
import com.opaigc.server.application.user.service.UserChatService;
import com.opaigc.server.application.user.service.UserService;
import com.opaigc.server.config.AppConfig;
import com.opaigc.server.infrastructure.common.Constants;
import com.opaigc.server.infrastructure.exception.AppException;
import com.opaigc.server.infrastructure.http.ApiResponse;
import com.opaigc.server.infrastructure.http.CommonResponseCode;
import com.opaigc.server.infrastructure.jwt.JwtTokenProvider;
import com.opaigc.server.infrastructure.utils.IPLimiter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
	private final MessageSource messageSource;
	@Autowired
	private OpenAiService openAiService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private UserService userService;
	@Autowired
	private UserChatService userChatService;

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private AppService appService;

	/**
	 * Chat 流式返回
	 * 给已有应用查询，已创建应用查询
	 * @param req
	 */
	@PostMapping(value = "/v2/chat/stream/anonymous", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@CrossOrigin(origins = "*")
	public Flux<String> streamCompletionsV2Anonymous(@RequestBody OpenAiService.CompletionsV2AnonymousRequest req,
																									 HttpServletRequest request) {
		IPLimiter queryLimit = new IPLimiter(appConfig.getAnonymousQueryLimit(),  24 * 60 * 60 * 1000);
		if (!queryLimit.isAllowed(request.getRemoteAddr())) {
			throw new AppException(CommonResponseCode.REMOTE_IP_MAX_LIMIT);
		}
		App app = appService.getByCode(req.getCode());
		if (Objects.isNull(app)) {
			throw new AppException(CommonResponseCode.APP_NOT_FOUND);
		}

		OpenAiService.ChatParameters parameters = OpenAiService.ChatParameters.builder().chatType(UserChat.ChatCategoryEnum.FREE)
			.messages(buildMessages(app.getRoles(), req.getMessages())).remoteIp(request.getRemoteAddr()).type(MessageType.TEXT)
			.temperature(app.getExt().getDouble("temperature"))
			.sessionId(Constants.CHAT_WITH_ANONYMOUS_USER_KEY).build();
		return openAiService.chatSend(parameters);
	}

	/**
	 * 预览应用查询接口
	 * @param req
	 * @param request
	 */
	@PostMapping(value = "/chat/stream/anonymous", produces = MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
	@CrossOrigin(origins = "*")
	public Flux<String> streamCompletionsAnonymous(@RequestBody OpenAiService.CompletionsAnonymousRequest req, HttpServletRequest request) {
		IPLimiter previewLimit = new IPLimiter(appConfig.getAnonymousPreviewLimit(),  24 * 60 * 60 * 1000);
		if (!previewLimit.isAllowed(request.getRemoteAddr())) {
			throw new AppException(CommonResponseCode.REMOTE_IP_MAX_LIMIT);
		}

		OpenAiService.ChatParameters parameters =
			OpenAiService.ChatParameters.builder().chatType(UserChat.ChatCategoryEnum.FREE).messages(req.getMessages())
				.temperature(req.getTemperature())
				.remoteIp(request.getRemoteAddr()).type(MessageType.TEXT).sessionId(Constants.CHAT_WITH_ANONYMOUS_USER_KEY).build();
		return openAiService.chatSend(parameters);
	}

	/**
	 * 非匿名接口，需要用户登录，无查询的限制，但是会有费用的限制
	 */
	@PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
	@CrossOrigin(origins = "*")
	public Flux<String> streamCompletions(@RequestBody OpenAiService.CompletionsRequest req, HttpServletRequest request) {
		String userCode = null;
		String securityToken = req.getToken();
		if (jwtTokenProvider.validateToken(securityToken)) {
			userCode = jwtTokenProvider.getUserCode(securityToken);
		}
		UserChat.ChatCategoryEnum chatType = validateUserAndGetChatType(userCode);

		OpenAiService.ChatParameters parameters =
			OpenAiService.ChatParameters.builder().chatType(chatType).messages(req.getMessages()).remoteIp(request.getRemoteAddr())
				.type(MessageType.TEXT).sessionId(userCode).build();
		return openAiService.chatSend(parameters);
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

	private List<OpenAiService.Message> buildMessages(JSONArray roles, JSONObject messages) {
		List<OpenAiService.Message> result = new ArrayList<>();
		for (int i = 0; i < roles.size(); i++) {
			JSONObject role = roles.getJSONObject(i);
			String template = role.getString("template");

			for (String key : messages.keySet()) {
				String value = messages.getString(key);
				template = template.replace("${" + key + "}", value);
			}

			OpenAiService.Message message = new OpenAiService.Message();
			message.setRole(role.getString("type"));
			message.setContent(template);
			result.add(message);
		}

		return result;
	}

	private UserChat.ChatCategoryEnum validateUserAndGetChatType(String userCode) {
		if (Objects.isNull(userCode)) {
			throw new AppException(CommonResponseCode.ACCESS_DENIED);
		}
		UserService.UserMemberDTO userMemberDTO = userService.getUserInfo(userCode);
		if (userMemberDTO.isFreeUser()) {
			validateFreeUser(userMemberDTO);
			return UserChat.ChatCategoryEnum.FREE;
		} else {
			validateVipUser(userMemberDTO);
			return UserChat.ChatCategoryEnum.PAID;
		}
	}

	private void validateVipUser(UserService.UserMemberDTO userMemberDTO) {
		if (userMemberDTO.getUsedQuota() >= userMemberDTO.getTotalQuota()) {
			throw new AppException(CommonResponseCode.USER_USAGE_REACH_TOTAL_LIMIT);
		}
	}

	private void validateFreeUser(UserService.UserMemberDTO userMemberDTO) {
		LocalDate today = LocalDate.now();
		LocalDateTime startOfDay = today.atStartOfDay();
		LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

		Long todayUserChatCount =
			userChatService.lambdaQuery().eq(UserChat::getUserId, userMemberDTO.getId()).between(UserChat::getCreatedAt, startOfDay, endOfDay)
				.count();
		if (todayUserChatCount >= userMemberDTO.getDailyLimit()) {
			throw new AppException(CommonResponseCode.USER_DAILY_USAGE_LIMIT);
		}
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
