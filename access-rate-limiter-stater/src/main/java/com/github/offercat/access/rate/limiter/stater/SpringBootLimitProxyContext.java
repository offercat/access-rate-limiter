package com.github.offercat.access.rate.limiter.stater;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;

import java.lang.annotation.Annotation;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年09月06日 12:45:33
 */
@Slf4j
@Primary
@ConfigurationProperties("access.rate.limit")
public class SpringBootLimitProxyContext implements ApplicationRunner {
    private SpringAnnotationAccessRateLimitContext accessRateLimitContext;

    public SpringBootLimitProxyContext(SpringAnnotationAccessRateLimitContext accessRateLimitContext) {
        this.accessRateLimitContext = accessRateLimitContext;
    }

    public void setLimitEnable(boolean limitEnable) {
        if (limitEnable) {
            log.info("setLimitEnable | 开启全局限流");
        } else {
            log.info("setLimitEnable | 关闭全局限流");
        }
        accessRateLimitContext.setLimitEnable(limitEnable);
    }

    public void setLogEnable(boolean logEnable) {
        if (logEnable) {
            log.info("setLimitEnable | 开启限流日志");
        } else {
            log.info("setLimitEnable | 关闭限流日志");
        }
        accessRateLimitContext.setLogEnable(logEnable);
    }

    public void setScanAnnotationType(Class<? extends Annotation> scanAnnotationType) {
        if (scanAnnotationType != null) {
            accessRateLimitContext.setScanClassAnnotationType(scanAnnotationType);
        }
    }

    public boolean isLimitEnable() {
        return accessRateLimitContext.limitEnable();
    }

    public boolean isLogEnable() {
        return accessRateLimitContext.isLogEnable();
    }

    public Class<? extends Annotation> getScanAnnotationType() {
        return accessRateLimitContext.getScanClassAnnotationType();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        accessRateLimitContext.refresh();
    }
}
