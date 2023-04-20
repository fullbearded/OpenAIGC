package com.opaigc.server.application.user.domain;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author: Runner.dada
 * @date: 2023/4/4
 * @description:
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_promotion", autoResultMap = true)
public class UserPromotion {

	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 使用的用户id
	 */
	private Long userId;

	/**
	 * 业务编号
	 */
	private String scene;

	/**
	 * promotion的id
	 */
	private Long promotionId;

	/**
	 * 优惠码编码
	 */
	private String promotionCode;

	/**
	 * 促销名称
	 */
	private String promotionName;

	/**
	 * 状态，locked 已锁定，used 已核销，revoked 已作废
	 */
	private Status status;

	/**
	 * 锁定参数
	 e*/
	@TableField(typeHandler = FastjsonTypeHandler.class)
	private JSONObject chargeData;

	/**
	 * 核销时间
	 */
	private LocalDateTime chargeAt;

	/**
	 * 作废时间
	 */
	private LocalDateTime revokeAt;


	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;

	@Getter
	@RequiredArgsConstructor
	public enum Status {
		USED("已使用"), REVOKED("已作废"),
		;

		private final String description;
	}
}
