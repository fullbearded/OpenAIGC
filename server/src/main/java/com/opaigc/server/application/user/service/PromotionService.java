package com.opaigc.server.application.user.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.opaigc.server.application.user.domain.Promotion;
import com.opaigc.server.application.user.domain.UserPromotion;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * promotion
 *
 * @author Sim@fiture.com 2021-11-18
 */
public interface PromotionService extends IService<Promotion> {

	/**
	 * 检查该促销是否可用
	 *
	 * @return
	 */
	PromotionDTO checkPromotion(CheckPromotionParam param);

	/**
	 * 锁定促销
	 */
	ChargePromotionDTO chargePromotion(ChargePromotionParam param);

	/**
	 * 兑换码列表
	 **/
	List<ListPromotionDTO> listPromotion(ListPromotionParam param);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class ListPromotionParam {
		/**
		 * 促销码
		 */
		private String code;

		private List<String> codes;

		/**
		 * 促销名称
		 */
		private String name;

		/**
		 *
		 */
		private String type;

		/**
		 * 促销状态，unavailable 不可用，available 可用
		 */
		private String status;

		private List<String> statuses;

		/**
		 * 使用次数
		 */
		private Integer usageLimit;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class ListPromotionDTO extends PromotionDTO {
		private UserPromotion userPromotion;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	class ChargePromotionDTO {
		private String code;
		private String name;
		private Promotion.Type type;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	class ChargePromotionParam {

		/**
		 * 业务场景
		 */
		private String scene;
		/**
		 * 用户code
		 */
		private String userCode;
		/**
		 * 兑换码
		 */
		private String code;

		/**
		 * 渠道
		 **/
		private String channel;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	class CheckPromotionParam {
		@NotBlank(message = "code cannot be empty")
		private String code;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	class PromotionDTO {
		/**
		 * 促销码
		 */
		private String code;
		/**
		 * 促销名称
		 */
		private String name;
		/**
		 * 促销状态，unavailable 不可用，available 可用
		 */
		private Promotion.Status status;
		/**
		 * 券类型
		 */
		private Promotion.Type type;
		/**
		 * 规则
		 */
		private JSONObject rule;
		/**
		 * 使用次数
		 */
		private Integer usageLimit;
		/**
		 * 可用开始时间
		 */
		private LocalDateTime startAt;
		/**
		 * 可用结束时间
		 */
		private LocalDateTime endAt;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	class RevokeUserPromotionParam {
		private String bizNo;
		private String code;
		private String accountId;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	class PromotionUseRule {
		private String processor;
	}
}
