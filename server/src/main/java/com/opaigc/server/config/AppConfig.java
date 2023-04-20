package com.opaigc.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cn.hutool.core.collection.ListUtil;
import java.util.List;
import java.util.Random;
import lombok.Data;

/**
 * 描述
 *
 * @author Runner.dada
 * @date 2023/3/23
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app-config")
public class AppConfig {

	/**
	 * 代理
	 **/
	private Proxy proxy;
	/**
	 * api token
	 **/
	private String apiKeys;
	/**
	 * api host
	 **/
	private String apiHost;
	/**
	 * 每日限制
	 **/
	private Integer dailyLimit;

	public String getApiToken() {
		List<String> keyList = ListUtil.toList(apiKeys.split(","));
		if (keyList.size() == 1) {
			return keyList.get(0);
		}
		Random random = new Random();
		int index = random.nextInt(keyList.size());
		return keyList.get(index);
	}

	@Data
	public static class Proxy {
		private String enable;
		private String host;
		private Integer port;
	}
}
