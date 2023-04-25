package com.opaigc.server.application.openai.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opaigc.server.application.openai.domain.chat.MessageType;
import com.opaigc.server.application.openai.listener.CompletedCallBack;
import com.opaigc.server.application.user.domain.UserChat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 描述
 *
 * @author Runner.dada
 * @date 2023/3/23
 */
public interface OpenAiService extends CompletedCallBack {

	Flux<String> chatSend(ChatParameters parameters);

	CreditGrantsResponse creditGrants(String key);

	ModerationData moderation(String prompt);

	Mono<Boolean> checkContent(String prompt);


	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class ChatParameters {
		private MessageType type;
		private List<Message> messages;
		private String sessionId;
		private String remoteIp;
		private UserChat.ChatCategoryEnum chatType;
		private String appCode;
		private Double temperature;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class CompletionsAnonymousRequest {
		@NotEmpty(message = "messages is required")
		private List<Message> messages;

		@NotNull(message = "temperature is required")
		private Double temperature;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class CompletionsV2AnonymousRequest {
		@NotEmpty(message = "messages is required")
		private JSONObject messages;

		@NotBlank(message = "Code is required")
		private String code;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class Message {
		private String role;
		private String content;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class CompletionsRequest {
		@NotEmpty(message = "messages is required")
		private List<Message> messages;
		@NotBlank(message = "token is required")
		private String token;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	class ModerationData {

		private String model;
		private String id;
		private List<Result> results;

		@Data
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Result {
			private boolean flagged;
			private Map<String, Boolean> categories;
			private Map<String, Double> category_scores;
		}
	}




	/**
	 * @author: Runner.dada
	 * @date: 2023/3/23
	 * @description: Balancing operations
	 **/
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	class CreditGrantsResponse {
		private String object;

		/**
		 * 总金额：美元
		 */
		@JsonProperty("total_granted")
		private BigDecimal totalGranted;

		/**
		 * 总使用金额：美元
		 */
		@JsonProperty("total_used")
		private BigDecimal totalUsed;

		/**
		 * 总剩余金额：美元
		 */
		@JsonProperty("total_available")
		private BigDecimal totalAvailable;

		/**
		 * 余额明细
		 */
		private Grants grants;


		@Data
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Grants {

			private String object;

			@JsonProperty("data")
			private List<Datum> data;

			@Data
			@Builder
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Datum {
				private String object;
				private String id;

				/**
				 * 赠送金额：美元
				 */
				@JsonProperty("grant_amount")
				private BigDecimal grantAmount;

				/**
				 * 使用金额：美元
				 */
				@JsonProperty("used_amount")
				private BigDecimal usedAmount;

				/**
				 * 生效时间戳
				 */
				@JsonProperty("effective_at")
				private Long effectiveAt;

				/**
				 * 过期时间戳
				 */
				@JsonProperty("expires_at")
				private Long expiresAt;
			}
		}
	}
}
