package com.opaigc.server.application.user.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.opaigc.server.application.user.domain.App;
import com.opaigc.server.application.user.mapper.AppMapper;
import com.opaigc.server.application.user.service.AppService;
import com.opaigc.server.application.user.service.UserService;
import com.opaigc.server.infrastructure.exception.AppException;
import com.opaigc.server.infrastructure.http.CommonResponseCode;
import com.opaigc.server.infrastructure.utils.CodeUtil;
import com.opaigc.server.infrastructure.utils.PageUtil;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 描述
 *
 * @author huhongda@fiture.com
 * @date 2023/4/11
 */

@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {
	private final static long CACHE_REFRESH_MINUTES = 10;
	private final static long CACHE_EXPIRE_MINUTES = 30;

	LoadingCache<String, App> appCache = CacheBuilder.newBuilder().refreshAfterWrite(CACHE_REFRESH_MINUTES, TimeUnit.MINUTES)
		.expireAfterAccess(CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES).maximumSize(1000L).build(CacheLoader.from(
			code -> lambdaQuery().eq(App::getCode, code).eq(App::getStatus, App.StatusEnum.ENABLED).isNull(App::getDeletedAt).one()));

	@Autowired
	private UserService userService;
	@Autowired
	private AppMapper appMapper;

	@Override
	public App getByCode(String appCode) {
		if (appCode == null) {
			return null;
		}
		return appCache.getUnchecked(appCode);
	}

	@Override
	public Boolean usageIncrement(Long appId, Boolean isPaid, Integer amount) {
		String sql;
		if (isPaid) {
			sql = "paid_use_count = paid_use_count + " + amount;
		} else {
			sql = "used_count = used_count + " + amount;
		}
		return lambdaUpdate().eq(App::getId, appId).setSql(sql).update();
	}

	@Override
	public List<AppListDTO> list(AppListParam req) {
		List<App> apps = lambdaQuery().eq(App::getStatus, App.StatusEnum.ENABLED).isNull(App::getDeletedAt)
			.orderByDesc(App::getUpvote)
			.eq(Objects.nonNull(req.getRecommend()), App::getRecommend, req.getRecommend())
			.eq(Objects.nonNull(req.getCode()), App::getCode, req.getCode())
			.eq(Objects.nonNull(req.getName()), App::getName, req.getName())
			.list();
		return converts(apps);
	}

	private List<AppListDTO> converts(List<App> apps) {
		return apps.stream().map(app -> {
			AppListDTO dto = new AppListDTO();
			BeanUtils.copyProperties(app, dto);
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public List<AppListDTO> publicApps(AppListParam req) {
		List<App> apps = lambdaQuery().eq(App::getStatus, App.StatusEnum.ENABLED).isNull(App::getDeletedAt)
			.eq(App::getCategory, App.AppCategoryEnum.PUBLIC).orderByDesc(App::getUpvote)
			.eq(Objects.nonNull(req.getRecommend()), App::getRecommend, req.getRecommend())
			.eq(Objects.nonNull(req.getCode()), App::getCode, req.getCode())
			.like(Objects.nonNull(req.getName()), App::getName, req.getName())
			.list();
		return converts(apps);
	}

	@Override
	public Boolean like(AppLikeParam req) {
		App app = lambdaQuery().eq(App::getCode, req.getCode()).eq(App::getStatus, App.StatusEnum.ENABLED).isNull(App::getDeletedAt).one();
		if (app == null) {
			return false;
		}
		return lambdaUpdate().eq(App::getId, app.getId()).setSql("upvote = upvote + 1").update();
	}

	@Override
	public App create(AppCreateParam req) {
		lambdaQuery().eq(App::getName, req.getName()).eq(App::getStatus, App.StatusEnum.ENABLED).isNull(App::getDeletedAt).oneOpt()
				.ifPresent(app -> {
					throw new AppException(CommonResponseCode.APP_NAME_EXIST);
				});

		App app = App.builder()
				.category(req.getCategory())
				.code(CodeUtil.generateRandomUserCode())
				.name(req.getName())
				.description(req.getDescription())
				.icon(req.getIcon())
				.status(App.StatusEnum.ENABLED)
				.forms(req.getForms())
				.roles(req.getRoles())
				.ext(new JSONObject().fluentPut("temperature", req.getTemperature()))
				.createdBy(req.getSession().getUsername())
				.updatedBy(req.getSession().getUsername())
				.userId(req.getSession().getId())
				.build();
		save(app);
		return app;
	}

	@Override
	public Page<AppListDTO> page(AppPageParam req) {
		PageUtil pageUtil = new PageUtil(req.getPage(), req.getPerPage());
		Page<App> page = new Page<>(pageUtil.getPage(), pageUtil.getPerPage());
		QueryWrapper<App> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(Objects.nonNull(req.getRecommend()), "recommend", req.getRecommend());
		queryWrapper.eq(Objects.nonNull(req.getCode()), "code", req.getCode());
		queryWrapper.like(Objects.nonNull(req.getNameLike()), "name", req.getNameLike());
		queryWrapper.eq(Objects.nonNull(req.getName()), "name", req.getName());
		queryWrapper.eq(Objects.nonNull(req.getCategory()), "category", req.getCategory());
		queryWrapper.orderByDesc("upvote");
		Page<App> appPage = appMapper.selectPage(page, queryWrapper);
		List<AppListDTO> appDTOList = appPage.getRecords().stream().map(AppListDTO::from).collect(Collectors.toList());
		Page<AppListDTO> appDTOPage = new Page<>(appPage.getCurrent(), appPage.getSize(), appPage.getTotal());
		appDTOPage.setRecords(appDTOList);
		return appDTOPage;
	}
}
