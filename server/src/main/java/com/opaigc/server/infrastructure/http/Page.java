package com.opaigc.server.infrastructure.http;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Runner.dada
 * @date: 2021/1/26
 * @description: 业务自定义的分页
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {

	@Builder.Default
	private Long page = 1l;

	@Builder.Default
	private Long perPage = 20l;

	private Long totalPages;

	private Integer total;

	private List<T> content;
}
