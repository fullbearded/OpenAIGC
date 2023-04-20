package com.opaigc.server.application.user.domain;

import com.alibaba.fastjson.JSONArray;
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
@TableName(value = "app", autoResultMap = true)
public class App {

	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * APP编码
	 */
	private String code;
	/**
	 * APP类型
	 **/
	@TableField(value = "category")
	private AppCategoryEnum category;
	/**
	 * APP推荐
	 **/
	private RecommendEnum recommend;
	/**
	 * APP点赞
	 **/
	private Integer upvote;
	/**
	 * APP名称
	 */
	private String name;
	/**
	 * APP图标
	 */
	private String icon;
	/**
	 * APP描述
	 */
	private String description;
	/**
	 * 使用次数
	 */
	private Integer usedCount;
	/**
	 * 付费使用次数
	 */
	private Integer paidUseCount;
	/**
	 * APP表单模板
	 * e
	 */
	@TableField(typeHandler = FastjsonTypeHandler.class)
	private JSONArray forms;
	/**
	 * APP预置问题模板
	 * e
	 */
	@TableField(typeHandler = FastjsonTypeHandler.class)
	private JSONArray roles;
	/**
	 * APP状态
	 **/
	private StatusEnum status;
	/**
	 * 删除时间
	 */
	private LocalDateTime deletedAt;
	/**
	 * 删除人
	 **/
	private String deletedBy;
	/**
	 * 创建时间
	 */
	private LocalDateTime createdAt;
	/**
	 * 创建人
	 **/
	private String createdBy;
	/**
	 * 修改时间
	 */
	private LocalDateTime updatedAt;
	/**
	 * 更新人
	 **/
	private String updatedBy;

	@Getter
	@RequiredArgsConstructor
	public enum RecommendEnum {
		NONE,
		HOME;
	}

	@Getter
	@RequiredArgsConstructor
	public enum AppCategoryEnum {
		PUBLIC,
		PRIVATE;
	}

	@Getter
	@RequiredArgsConstructor
	public enum StatusEnum {
		/**
		 * 已启用
		 */
		ENABLED("已启用"),
		/**
		 * 已禁用
		 */
		DISABLED("已禁用");


		private final String description;
	}

}
