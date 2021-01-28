package com.github.offercat.access.rate.limiter.core.annotated;

import com.github.offercat.access.rate.limiter.core.limiter.AccessRateLimiter;
import com.github.offercat.access.rate.limiter.core.limiter.QpsLimiter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * qps 限流
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 12:10:25
 */
@AccessRateLimit
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface QpsLimit {

    /**
     * 每秒允许被访问的次数
     */
    int value() default -1;

    /**
     * 当被限流时，可定义此参数，指定是否阻塞等待
     */
    boolean blockWait() default false;

    /**
     * 与 blockWait 配合使用，等待的时间长度
     */
    int maxWaitTime() default -1;

    /**
     * 与 blockWait, maxWaitTime 配合使用，等待的时间单位
     */
    TimeUnit waitTimeUnit() default TimeUnit.SECONDS;

    /**
     * 用于获取限制因子的方法，支持多个
     */
    String[] limitFactorMethod() default {};

    /**
     * 需要使用方提供一个获取默认返回值的方法，当服务繁忙被限流时，作为默认返回值返回给调用者
     */
    String backupMethod() default "";

    /**
     * 可以自定义限流器，默认为qps限流器，内部使用 guava RateLimiter 实现
     */
    Class<? extends AccessRateLimiter> accessRateLimiter() default QpsLimiter.class;
}
