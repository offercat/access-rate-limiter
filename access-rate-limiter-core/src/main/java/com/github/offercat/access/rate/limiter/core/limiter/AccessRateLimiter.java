package com.github.offercat.access.rate.limiter.core.limiter;

import com.github.offercat.access.rate.limiter.core.RateLimitPoint;
import com.github.offercat.access.rate.limiter.core.proxy.InjectFunction;

import java.util.function.Supplier;

/**
 * 限流器
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 11:15:37
 */
public interface AccessRateLimiter {

    /**
     * 处理方法
     *
     * @param limitKey       限制点唯一key
     * @param rateLimitPoint 限制点
     * @param backupFunction 备用方法的回调
     * @param injectFunction 注入方法的回调
     * @return 返回值
     * @throws Throwable 回调的异常
     */
    Object handle(String limitKey, RateLimitPoint rateLimitPoint, Supplier<Object> backupFunction, InjectFunction<Object> injectFunction) throws Throwable;
}
