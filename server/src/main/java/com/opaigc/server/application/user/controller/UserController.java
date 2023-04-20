package com.opaigc.server.application.user.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opaigc.server.application.user.domain.User;
import com.opaigc.server.application.user.service.UserService;
import com.opaigc.server.infrastructure.auth.AccountSession;
import com.opaigc.server.infrastructure.http.ApiResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述
 *
 * @author huhongda@fiture.com
 * @date 2023/4/9
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 获取用户信息
	 **/
	@PostMapping("/info")
	public ApiResponse info(@NotNull(message = "请登录后再操作") AccountSession session) {
		UserService.UserMemberDTO userMemberDTO = userService.getUserInfo(session.getCode());
		UserService.UserInfoDTO userInfoDTO = new UserService.UserInfoDTO();
		BeanUtils.copyProperties(userMemberDTO, userInfoDTO);
		return ApiResponse.success(userInfoDTO);
	}



}
