package com.opaigc.server.application.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opaigc.server.application.user.service.UserService;
import com.opaigc.server.infrastructure.http.ApiResponse;
import com.opaigc.server.infrastructure.http.CommonResponseCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述
 *
 * @author huhongda@fiture.com
 * @date 2023/4/9
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {


	@Autowired
	private UserService userService;

	@PostMapping("/registration")
	public ApiResponse registerUserAccount(@RequestBody @Valid UserService.UserRegistrationParam req, HttpServletRequest request) {
		if (userService.existsByUsernameOrMobile(req.getUsername(), req.getMobile())) {
			return ApiResponse.error(CommonResponseCode.USERNAME_OR_MOBILE_EXIST);
		}
		String remoteAddr = request.getRemoteAddr();
		if (remoteAddr.equals("0:0:0:0:0:0:0:1")) {
			remoteAddr = "127.0.0.1";
		}
		req.setRegisterIp(remoteAddr);
		userService.create(req);
		return ApiResponse.success();
	}

}
