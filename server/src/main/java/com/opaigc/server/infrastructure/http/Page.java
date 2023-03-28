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
    private Integer page = 1;

    @Builder.Default
    private Integer perPage = 20;

    private Integer totalPages;

    private Integer totalElements;

    private List<T> content;
}
