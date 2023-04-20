package com.opaigc.server.infrastructure.utils;


import com.opaigc.server.infrastructure.http.Page;

import java.util.Objects;
import java.util.Optional;

/**
 * @author: Runner.dada
 * @date: 2021/1/26
 * @description: 分页工具类，转换为自定义的类
 **/
public class PageUtil {

	private Integer page;
	private Integer perPage;


	/**
	 * 将Pageable转换为业务定义的Page格式
	 **/
	public static <T> Page<T> convert(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page) {
		return Page.<T>builder()
			.content(page.getRecords())
			.page(page.getCurrent())
			.perPage(page.getSize())
			.total((int) page.getTotal())
			.totalPages(page.getPages()).build();
	}

	public PageUtil(Integer page, Integer perPage) {
		this.page = page;
		this.perPage = perPage;
	}

	public Integer getPage() {
		if (Objects.isNull(page) || page < 1) {
			return 1;
		}
		if (page > 1000) {
			return 1000;
		}
		return page;
	}

	public Integer getPerPage() {
		return Optional.ofNullable(perPage).map(size -> size > 50 ? 50 : size).orElse(20);
	}
}
