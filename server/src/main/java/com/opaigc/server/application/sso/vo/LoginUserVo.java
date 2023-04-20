package com.opaigc.server.application.sso.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: Runner.dada
 * @date: 2020/12/13
 * @description: Login User Vo
 **/
@Getter
@Setter
public class LoginUserVo {

	@NotBlank(message = "用户名不能为空")
	private String username;

	@NotBlank(message = "密码不能为空")
	private String password;

	@NotBlank(message = "登录类型不能为空")
	private String type;

	private String captcha;

	private String mobile;
}
