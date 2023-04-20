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
import lombok.NoArgsConstructor;
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
@TableName(value = "user_chat", autoResultMap = true)
public class UserChat {

	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 使用的用户id
	 */
	private Long userId;

	/**
	 * 使用的APP id
	 */
	private Long appId;

	/**
	 * 聊天类型，FREE 免费， PAID 付费
	 */
	private ChatCategoryEnum category;

	/**
	 * token 大小
	 */
	private Integer token;

	/**
	 * 问题
	 */
	@TableField(typeHandler = FastjsonTypeHandler.class)
	private JSONObject questions;

	/**
	 * 答案
	 **/
	@TableField(typeHandler = FastjsonTypeHandler.class)
	private JSONObject answers;

	/**
	 * 创建时间
	 **/
	private LocalDateTime createdAt;

	/**
	 * 创建人
	 **/
	private String createdBy;

	public enum ChatCategoryEnum {
		FREE,
		PAID
	}

}
