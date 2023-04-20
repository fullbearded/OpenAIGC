package com.opaigc.server.application.user.domain;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author: Runner.dada
 * @date: 2023/4/4s
 * @description:
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "promotion", autoResultMap = true)
public class Promotion {

	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 促销码
	 */
	private String code;
	/**
	 * 促销名称
	 */
	private String name;
	/**
	 * 券类型
	 */
	private Type type;
	/**
	 * 规则
	 */
	@TableField(typeHandler = FastjsonTypeHandler.class)
	private JSONObject rule;
	/**
	 * 促销状态，unavailable 不可用，available 可用
	 */
	private Status status;
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
	/**
	 * 创建时间
	 */
	private LocalDateTime createdAt;
	private String createdBy;
	/**
	 * 修改时间
	 */
	private LocalDateTime updatedAt;
	private String updatedBy;

	/**
	 * 促销使用次数，默认值为0，如果为0，则认为是不限制为99999次
	 */
	public Integer normalizeUsageLimit() {
		return getUsageLimit() == 0 ? 999999 : getUsageLimit();
	}

	public Boolean isOutOfTime() {
		LocalDateTime now = LocalDateTime.now();
		return now.isAfter(normalizeEndAt()) || now.isBefore(normalizeStartAt());
	}

	public LocalDateTime normalizeStartAt() {
		return Optional.ofNullable(getStartAt()).orElse(LocalDateTime.MIN);
	}

	public LocalDateTime normalizeEndAt() {
		return Optional.ofNullable(getEndAt()).orElse(LocalDateTime.MAX);
	}

	@Getter
	@RequiredArgsConstructor
	public enum Status {
		UNAVAILABLE("未启用"),
		AVAILABLE("启用");

		private final String description;
	}

	@Getter
	@RequiredArgsConstructor
	public enum Type {
		EXPERIENCE_X("次数体验版", "E", 10, 100, 31,
			100, 899, "redeem", "https://opaigc.com"),
		EXPERIENCE_S("次数体验版", "E", 10, 600, 31,
			600, 2999, "redeem", "https://opaigc.com"),
		MONTH_PACKAGE_X("月度体验版", "X", 10, 100, 31,
			3500, 1999, "redeem", "https://opaigc.com"),
		MONTH_PACKAGE_S("月度基础版", "S", 10, 200, 31,
			10000, 4999, "redeem", "https://opaigc.com"),
		MONTH_PACKAGE_L("月度进阶版", "M", 10, 600, 31,
			20000, 9999, "redeem", "https://opaigc.com"),
		MONTH_PACKAGE_B("月度专业版", "P", 10, 2000, 31,
			50000, 14999, "redeem", "https://opaigc.com"),
		;
		private final String description;
		private final String codePrefix;
		private final int codeLength;
		private final int dailyUsageLimit;
		private final int effectiveDays;
		private final int totalCapacity;
		private final int price;
		private final String paymentMethod;
		private final String paymentValue;

	}
}
