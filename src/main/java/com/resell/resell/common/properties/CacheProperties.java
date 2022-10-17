package com.resell.resell.common.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;


/*
 * 트래픽에 따라 캐시 별 만료기간을 쉽게 변경할 수 있도록 properties에서 cache 이름별 만료 기간을 Map에 바인딩
 */

@Getter
@ConfigurationProperties(prefix = "cache.redis")
public class CacheProperties {

    private final Map<String, Long> ttl = new HashMap<>();
}