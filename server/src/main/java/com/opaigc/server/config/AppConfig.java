package com.opaigc.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cn.hutool.core.util.RandomUtil;
import java.util.List;
import lombok.Data;

/**
 * 描述
 *
 * @author Runner.dada
 * @date 2023/3/23
 */
@Configuration
@ConfigurationProperties(prefix = "app-config")
@Data
public class AppConfig {

    private String env;

    private Proxy proxy;

    private String apiKey;

    private String apiHost;

    private List<String> apiKeyList;

    @Data
    public static class Proxy {
        private String host;
        private Integer port;
    }

    public String getApiToken() {
        String key = apiKey;
        if (apiKeyList != null && !apiKeyList.isEmpty()) {
            key = RandomUtil.randomEle(apiKeyList);
        }
        return key;
    }
}
