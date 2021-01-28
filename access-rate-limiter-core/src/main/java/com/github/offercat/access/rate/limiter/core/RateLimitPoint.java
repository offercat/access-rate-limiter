package com.github.offercat.access.rate.limiter.core;

import com.github.offercat.access.rate.limiter.core.limiter.AccessRateLimiter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 限制点
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 11:22:40
 */
public interface RateLimitPoint {

    /**
     * 获取唯一id
     *
     * @return 唯一id
     */
    String getId();

    /**
     * 限制次数
     *
     * @return 限制次数
     */
    int count();

    /**
     * 限制时间长度
     *
     * @return 限制时间长度
     */
    int time();

    /**
     * 限制时间单位
     *
     * @return 限制时间长度
     */
    TimeUnit timeUnit();

    /**
     * 是否阻塞等待
     *
     * @return 是否阻塞等待
     */
    boolean blockWait();

    /**
     * 阻塞时间
     *
     * @return 阻塞时间
     */
    int maxWaitTime();

    /**
     * 是否开启限流
     *
     * @return 是否开启限流
     */
    boolean limitEnable();

    /**
     * 注入的方法
     *
     * @return 注入的方法
     */
    Method injectMethod();

    /**
     * 阻塞时间单位
     *
     * @return 阻塞时间单位
     */
    TimeUnit waitTimeUnit();

    /**
     * 限制因子获取方法
     *
     * @return 限制因子获取方法
     */
    LimitFactorFunction[] limitFactorFunctions();

    /**
     * 备用方法
     *
     * @return 备用方法
     */
    BackupFunction backupFunction();

    /**
     * 获取饱和策略
     *
     * @return 饱和策略
     */
    AccessRateLimiter getAccessRateLimiter();

    /**
     * 获取频率限制器
     *
     * @return 频率限制器
     */
    LimitPointFactory getLimitPointFactory();

    /**
     * 生成 limitKey
     *
     * @param limitFlags limitFlags
     * @return accessKey
     */
    String generateLimitKey(List<String> limitFlags);
}
