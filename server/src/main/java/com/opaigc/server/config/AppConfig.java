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
@Configuration
@ConfigurationProperties(prefix = "app-config")
@Data
public class AppConfig {

    private String env;

    private Proxy proxy;

    private String apiKeys;

    private String apiHost;

    @Data
    public static class Proxy {
        private String host;
        private Integer port;
    }

    public String getApiToken() {
        List<String> keyList = ListUtil.toList(apiKeys.split(","));
        if (keyList.size() == 1) {
            return keyList.get(0);
        }
        Random random = new Random();
        int index = random.nextInt(keyList.size());
        return keyList.get(index);
    }
}
