package com.github.offercat.access.rate.limiter.core;

import com.github.offercat.access.rate.limiter.core.resolver.LimitFactorResolver;

import java.util.Map;

/**
 * 限制器
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 11:12:40
 */
public interface LimitPointFactory {

    /**
     * 是否支持覆盖
     *
     * @return 是否支持覆盖
     */
    boolean supportCover();

    /**
     * 限制点数量
     *
     * @return 限制点数量
     */
    int limitPointsSize();

    /**
     * 注册限制点
     *
     * @param rateLimitPoint limitPoint
     * @return 注册成功返回自己
     */
    LimitPointFactory registerLimitPoint(RateLimitPoint rateLimitPoint);

    /**
     * 获取所有的限制点
     *
     * @return 限制点集合
     */
    Map<String, RateLimitPoint> getLimitPoints();

    /**
     * 查找限制点
     *
     * @param limitPointId 限制点id
     * @return LimitPoint
     */
    RateLimitPoint lookupLimitPoint(String limitPointId);

    /**
     * 获取限制因子解析器
     *
     * @return 限制因子解析器
     */
    LimitFactorResolver getLimitFactorResolver();

    /**
     * 设置限制因子解析器
     *
     * @param limitFactorResolver 限制因子解析器
     */
    void setLimitFactorResolver(LimitFactorResolver limitFactorResolver);

    /**
     * 是否开启日志
     *
     * @return 是否开启日志
     */
    boolean isLogEnable();
}
