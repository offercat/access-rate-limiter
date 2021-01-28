package com.github.offercat.access.rate.limiter.core;

import com.github.offercat.access.rate.limiter.core.resolver.LimitPointResolver;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 限流上下文
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 15:19:31
 */
public interface AccessRateLimitContext extends ProxyBeanFactory, LimitPointFactory {

    /**
     * 刷新上下文
     */
    void refresh();

    /**
     * 是否已经刷新
     *
     * @return 是否已经刷新
     */
    boolean hasRefreshed();

    /**
     * 是否允许刷新多次
     *
     * @return 是否允许刷新多次
     */
    boolean allowRefreshManyTimes();

    /**
     * 是否开启全局限流
     *
     * @return 是否开启限流
     */
    boolean limitEnable();

    /**
     * 设置限制点解析器
     *
     * @param limitPointResolver limitPointResolver
     */
    void setLimitPointResolver(LimitPointResolver limitPointResolver);

    /**
     * 获取限制点解析器
     *
     * @return 限制点解析器
     */
    LimitPointResolver getLimitPointResolver();

    /**
     * 解析并注册
     *
     * @param method method
     * @return LimitPoint
     */
    RateLimitPoint lookupAndRegister(Method method);

    /**
     * 所有的控制器类型
     *
     * @return 所有的控制器类型
     */
    List<Class<?>> controllerTypes();
}
