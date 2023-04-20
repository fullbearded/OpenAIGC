package com.opaigc.server.infrastructure.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * @author: Runner.dada
 * @date: 2023/4/5
 * @description: MybatisPlus配置
 **/
@Configuration
@EnableTransactionManagement
@MapperScan("com.opaigc.**.mapper")
public class MybatisPlusConfig {

	@Bean
	public OptimisticLockerInnerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInnerInterceptor();
	}

	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}
}
