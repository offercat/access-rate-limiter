package com.github.offercat.access.rate.limiter.core.resolver;

import com.github.offercat.access.rate.limiter.core.RateLimitPoint;
import com.github.offercat.access.rate.limiter.core.LimitPointFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 限制点解析器
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 17:13:57
 */
public interface LimitPointResolver {

    /**
     * 解析限制点
     *
     * @param method method
     * @param limitPointFactory rateLimiter
     * @return 限制点
     */
    RateLimitPoint resolve(Method method, LimitPointFactory limitPointFactory);

    /**
     * 解析限制点集合
     *
     * @param methodList 方法列表
     * @param limitPointFactory rateLimiter
     * @return 限制点集合
     */
    List<RateLimitPoint> resolve(List<Method> methodList, LimitPointFactory limitPointFactory);

    /**
     * 生成唯一的方法名
     *
     * @param method method
     * @return 唯一的方法名
     */
    String generateUniqueLimitPointId(Method method);
}
