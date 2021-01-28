package com.github.offercat.access.rate.limiter.core.annotated;

import com.github.offercat.access.rate.limiter.core.AccessRateLimitContext;

import java.lang.annotation.Annotation;

/**
 * 注解限流上下文
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 14:58:19
 */
public interface AnnotationAccessRateLimitContext extends AccessRateLimitContext {

    /**
     * 限制器上下文
     *
     * @return 扫码路径
     */
    String scanPath();

    /**
     * 设置初始化注解类型
     *
     * @param scanClassAnnotationType scanClassAnnotationType
     */
    void setScanClassAnnotationType(Class<? extends Annotation> scanClassAnnotationType);
}
