package com.opaigc.server.application.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opaigc.server.application.user.service.AppService;
import com.opaigc.server.infrastructure.auth.AccountSession;
import com.opaigc.server.infrastructure.common.Constants;
import com.opaigc.server.infrastructure.exception.AppException;
import com.opaigc.server.infrastructure.http.ApiResponse;
import com.opaigc.server.infrastructure.http.CommonResponseCode;
import com.opaigc.server.infrastructure.utils.IPLimiter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述
 *
 * @author huhongda@fiture.com
 * @date 2023/4/9
 */
@Slf4j
@RestController
@RequestMapping("/api/app")
public class AppController {

	@Autowired
	private AppService appService;

	/**
	 * 获取APP列表
	 **/
	@PostMapping("/list")
	public ApiResponse list(@RequestBody AppService.AppListParam req) {
		return ApiResponse.success(appService.publicApps(req));
	}

	/**
	 * APP 点餐
	 **/
	@PostMapping("/like")
	public ApiResponse like(@RequestBody AppService.AppLikeParam req) {
		return ApiResponse.success(appService.like(req));
	}

	/**
	 * APP 创建
	 **/
	@PostMapping("/create")
	public ApiResponse create(@RequestBody AppService.AppCreateParam req, @NotNull(message = "请登录后再操作") AccountSession session) {
		req.setSession(session);
		return ApiResponse.success(appService.create(req));
	}
}
