package com.opaigc.server.application.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.opaigc.server.application.user.domain.App;
import com.opaigc.server.application.user.service.AppService;
import com.opaigc.server.infrastructure.auth.AccountSession;
import com.opaigc.server.infrastructure.common.Constants;
import com.opaigc.server.infrastructure.exception.AppException;
import com.opaigc.server.infrastructure.http.ApiResponse;
import com.opaigc.server.infrastructure.http.CommonResponseCode;
import com.opaigc.server.infrastructure.utils.IPLimiter;
import com.opaigc.server.infrastructure.utils.PageUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述
 *
 * @author huhongda@fiture.com
 * @date 2023/4/16
 */

@Slf4j
@RestController
@RequestMapping("/api")
public class AnonymousController {
	@Autowired
	private AppService appService;

	private final IPLimiter limiter = new IPLimiter(2, 30 * 60 * 1000);

	/**
	 * 获取公共APP列表
	 **/
	@PostMapping("/app/page/anonymous")
	public ApiResponse page(@RequestBody AppService.AppPageParam req) {
		req.setCategory(App.AppCategoryEnum.PUBLIC);
		return ApiResponse.success(PageUtil.convert(appService.page(req)));
	}

	/**
	 * 获取公共APP列表
	 **/
	@PostMapping("/app/list/anonymous")
	public ApiResponse list(@RequestBody AppService.AppListParam req) {
		return ApiResponse.success(appService.publicApps(req));
	}

	/**
	 * APP 点餐
	 **/
	@PostMapping("/app/like/anonymous")
	public ApiResponse like(@RequestBody AppService.AppLikeParam req) {
		return ApiResponse.success(appService.like(req));
	}

	/**
	 * 检查App是否存在
	 **/
	@PostMapping("/app/check/anonymous")
	public ApiResponse check(@RequestBody AppCheckParam req) {
		appService.lambdaQuery().eq(App::getName, req.getName()).oneOpt().ifPresent(app -> {
			throw new AppException(CommonResponseCode.APP_NAME_EXIST);
		});
		return ApiResponse.success();
	}

	/**
	 * APP 创建
	 **/
	@PostMapping("/app/create/anonymous")
	public ApiResponse anonymousCreate(@RequestBody AppService.AppCreateParam req, HttpServletRequest request) {
		if (!limiter.isAllowed(request.getRemoteAddr())) {
			throw new AppException(CommonResponseCode.APP_WITH_ANONYMOUS_MAX_LIMIT);
		}

		req.setSession(AccountSession.builder().username(Constants.CHAT_WITH_ANONYMOUS_USER_KEY).id(0l).build());
		return ApiResponse.success(appService.create(req));
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	static class AppCheckParam {
		/**
		 * APP名称
		 **/
		@NotBlank(message = "APP名称不能为空")
		private String name;
	}
}
