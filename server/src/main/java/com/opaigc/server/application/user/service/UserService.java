package com.opaigc.server.application.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.opaigc.server.application.user.domain.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Runner.dada
 * @date: 2020/12/21
 * @description:
 **/
public interface UserService extends IService<User> {

	User create(UserRegistrationParam req);

	User findById(Long id);

	Optional<User> getByUsername(String username);

	Boolean existsByUsernameOrMobile(String username, String mobile);

	User getByCodeOrElseThrow(String code);

	Optional<User> getByCode(String code);

	Boolean delete(Long id);

	UserMemberDTO getUserInfo(String code);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class UserRegistrationParam {
		@NotNull(message = "用户名不能为空")
		@Size(min = 4, max = 12, message = "用户名长度不能小于4位不能大于12位")
		private String username;

		@Pattern(regexp = "^(1[3-9])\\d{9}$", message = "手机号格式不正确")
		@NotNull(message = "手机号不能为空")
		private String mobile;

		@NotBlank(message = "密码不能为空")
		@Size(min = 6, message = "用户名长度不能小于6位")
		private String password;

		private String registerIp;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	class UserInfoDTO {
		/**
		 * 用户Code
		 **/
		private String code;
		/**
		 * 用户名
		 **/
		private String username;
		/**
		 * 状态
		 **/
		private User.UserStatusEnum status;
		/**
		 * 用户类型
		 **/
		private User.UserType userType;
		/**
		 * 头像
		 **/
		private String avatar;
		/**
		 * 邮箱
		 **/
		private String email;
		/**
		 * 会员到期日
		 **/
		private LocalDateTime expireDate;
		/**
		 * 每日限额
		 **/
		private Integer dailyLimit;
		/**
		 * 已使用额度
		 **/
		private Integer usedQuota;
		/**
		 * 已使用额度-免费
		 **/
		private Integer freeUsedQuota;
		/**
		 * 总查询额度
		 **/
		private Integer totalQuota;

		public Boolean isExpired() {
			return expireDate.isBefore(LocalDateTime.now());
		}

		public Boolean isFreeUser() {
			return totalQuota == 0 || isExpired();
		}
	}


	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class UserMemberDTO extends UserInfoDTO {
		/**
		 * id
		 **/
		private Long id;
	}
}
