package com.github.offercat.access.rate.limiter.core.annotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 访问限制控制器
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 16:54:19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface AccessLimitController {
}
