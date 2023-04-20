package com.opaigc.server.application.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.opaigc.server.application.user.domain.Promotion;
import com.opaigc.server.application.user.service.PromotionService;
import com.opaigc.server.infrastructure.auth.AccountSession;
import com.opaigc.server.infrastructure.http.ApiResponse;
import com.opaigc.server.infrastructure.utils.CodeUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Runner.dada
 * @date: 2023/4/5
 * @description: 兑换码
 **/
@Slf4j
@RestController
@RequestMapping("/api/promotion")
public class PromotionController {

	private static final int CODE_DUPLICATE_MAX_RETRY_COUNT = 15;

	@Autowired
	private PromotionService promotionService;

	/**
	 * 获取兑换码分类
	 **/
	@PostMapping("/types")
	public ApiResponse types() {
		List<PromotionTypeDTO> list = new ArrayList<>();
		for (Promotion.Type type : Promotion.Type.values()) {
			list.add(PromotionTypeDTO.builder().type(type).description(type.getDescription()).dailyUsageLimit(type.getDailyUsageLimit())
				.totalCapacity(type.getTotalCapacity()).effectiveDays(type.getEffectiveDays()).paymentMethod(type.getPaymentMethod())
				.paymentValue(type.getPaymentValue()).price(type.getPrice())
				.build());
		}
		return ApiResponse.success(list);
	}

	/**
	 * 兑换码校验
	 **/
	@PostMapping("/check")
	public ApiResponse check(@RequestBody @Valid PromotionService.CheckPromotionParam param) {
		PromotionService.PromotionDTO promotion = promotionService.checkPromotion(param);
		return ApiResponse.success(promotion);
	}

	/**
	 * 兑换码兑换
	 **/
	@PostMapping("/charge")
	public ApiResponse charge(@RequestBody @Validated ChargePromotionParam param,
														@NotNull(message = "请登录后再操作") AccountSession accountSession) {
		PromotionService.ChargePromotionDTO usePromotionDTO = promotionService.chargePromotion(PromotionService.ChargePromotionParam.builder()
			.userCode(accountSession.getCode())
			.code(param.getCode())
			.channel(param.getChannel())
			.build());
		return ApiResponse.success(usePromotionDTO);
	}

	/**
	 * 列表展示
	 */
	@PostMapping("/list")
	public ApiResponse list(@RequestBody @Validated PromotionService.ListPromotionParam req) {
		return ApiResponse.success(promotionService.listPromotion(req));
	}

	/**
	 * 批量生成兑换码
	 **/
	@PostMapping("/create/batch")
	public ApiResponse batchCreate(@RequestBody @Valid BatchCreateParam param) {
		List<String> result = new ArrayList<>();

		IntStream.range(0, param.getCount()).forEach(index -> {
			int retryCount = 0;
			while (retryCount < CODE_DUPLICATE_MAX_RETRY_COUNT) {
				String code = CodeUtil.generateNewCode(param.getType().getCodePrefix(), param.getType().getCodeLength());
				try {
					String name = Objects.isNull(param.getName()) ? param.getType().getDescription() : param.getName();
					Promotion promotion =
						Promotion.builder().code(code).name(name).type(param.getType()).status(Promotion.Status.AVAILABLE)
							.usageLimit(param.getUsageLimit()).rule(new JSONObject()
								.fluentPut("useRule", param.getUseRule())
								.fluentPut("summary", param.getSummary())
								.fluentPut("scene", param.getScene())
								.fluentPut("source", param.getSource()))
							.startAt(LocalDateTime.now())
							.createdBy(param.getOperator())
							.build();
					promotionService.save(promotion);
					result.add(promotion.getCode());
					return;
				} catch (DuplicateKeyException e) {
					log.warn("生成兑换码失败，code已被占用 code={}。重新生成", code);
					retryCount++;
				} catch (Exception e) {
					log.error("生成兑换码异常，code = {}", code);
					throw e;
				}
			}
		});
		return ApiResponse.success(result);
	}


	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	static class PromotionTypeDTO {
		private Promotion.Type type;
		private String description;
		private int dailyUsageLimit;
		private int effectiveDays;
		private int totalCapacity;
		private int price;
		private String paymentMethod;
		private String paymentValue;
	}


	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class ChargePromotionParam {
		/**
		 * 兑换码
		 **/
		@NotBlank(message = "兑换码不能为空")
		private String code;
		/**
		 * 核销渠道
		 **/
		private String channel;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BatchCreateParam {
		/**
		 * 批量生成兑换码的最大数量
		 **/
		@NotNull
		private Integer count;
		/**
		 * 兑换码类型
		 **/
		@NotNull
		private Promotion.Type type;
		/**
		 * 兑换码名称, 不传则以 Type为准
		 **/
		private String name;
		/**
		 * 兑换码使用规则
		 **/
		private PromotionService.PromotionUseRule useRule;
		/**
		 * 兑换码使用场景
		 **/
		private String scene;
		/**
		 * 兑换码使用说明
		 **/
		private String summary;
		/**
		 * 兑换码生成来源
		 **/
		private String source;
		/**
		 * 单个兑换码最大使用次数
		 **/
		@Builder.Default
		private Integer usageLimit = 1;
		/**
		 * 操作人
		 **/
		@NotBlank
		private String operator;
	}
}
