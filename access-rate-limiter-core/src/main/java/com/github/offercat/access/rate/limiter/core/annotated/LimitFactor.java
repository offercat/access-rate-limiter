package com.github.offercat.access.rate.limiter.core.annotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 20:34:52
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface LimitFactor {

    String[] value() default "";
}
