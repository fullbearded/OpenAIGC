package com.opaigc.server.application.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author: Runner.dada
 * @date: 2020/12/6
 * @description: System User Domain Object
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "users", autoResultMap = true)
public class User implements Serializable {

	/**
	 * 自增主键
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 用户Code
	 **/
	private String code;

	/**
	 * 用户名
	 **/
	private String username;

	/**
	 * 邮箱
	 **/
	private String email;

	/**
	 * 手机号
	 **/
	private String mobile;

	/**
	 * 密码
	 **/
	private String password;

	/**
	 * 头像
	 **/
	private String avatar;

	/**
	 * 用户类型
	 **/
	@Builder.Default
	private UserType userType = UserType.USER;

	/**
	 * 性别
	 **/
	@Builder.Default
	private Sex sex = Sex.MAN;

	/**
	 * 状态
	 **/
	@Builder.Default
	private UserStatusEnum status = UserStatusEnum.ENABLE;

	/**
	 * 注册IP
	 **/
	private String registerIp;
	/**
	 * 最后登录IP
	 **/
	private String lastLoginIp;

	/**
	 * 最后登录时间
	 **/
	private LocalDateTime lastLoginDate;

	/**
	 * 备注
	 **/
	private String remark;

	/**
	 * 删除时间
	 */
	private LocalDateTime deletedAt;
	private String deletedBy;

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

	public enum Sex {
		MAN, WOMAN, UNKNOWN
	}

	@Getter
	public enum UserStatusEnum {
		ENABLE("正常"), BANNED("禁用");

		private String desc;

		UserStatusEnum(String desc) {
			this.desc = desc;
		}
	}


	@Getter
	public enum UserType {
		SYSTEM("系统用户"), USER("普通用户");

		private String desc;

		UserType(String desc) {
			this.desc = desc;
		}
	}
}
