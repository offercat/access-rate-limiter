package com.github.offercat.access.rate.limiter.core.annotated;

import com.github.offercat.access.rate.limiter.core.limiter.AccessRateLimiter;
import com.github.offercat.access.rate.limiter.core.limiter.TokenBucketLimiter;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 访问限制接口
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 12:08:25
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface AccessRateLimit {

    /**
     * 允许被访问的次数
     */
    int count() default -1;

    /**
     * 时间段，与 count 配合使用
     */
    int time() default 1;

    /**
     * time 的时间单位，与 time, count 配合使用，标识 time / timeUnit 之内允许访问 count 次
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

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
     * 可以自定义限流器，默认为令牌桶算法限流器
     */
    Class<? extends AccessRateLimiter> accessRateLimiter() default TokenBucketLimiter.class;
}
