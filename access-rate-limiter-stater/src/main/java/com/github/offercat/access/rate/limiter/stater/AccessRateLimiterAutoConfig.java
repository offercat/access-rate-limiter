package com.github.offercat.access.rate.limiter.stater;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 限制器自动注册
 *
 * @author 徐通 xutong34
 * @since 2020年09月03日 18:33:54
 */
@Configuration
@EnableConfigurationProperties(SpringBootLimitProxyContext.class)
public class AccessRateLimiterAutoConfig {

    @Bean
    SpringAnnotationAccessRateLimitContext springBootAnnotationAccessRateLimitContext() {
        return new SpringAnnotationAccessRateLimitContext();
    }
}
