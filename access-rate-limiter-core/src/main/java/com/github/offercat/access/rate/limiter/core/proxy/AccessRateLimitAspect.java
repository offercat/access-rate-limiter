package com.github.offercat.access.rate.limiter.core.proxy;

import com.github.offercat.access.rate.limiter.core.RateLimitPoint;
import com.github.offercat.access.rate.limiter.core.resolver.LimitFactorResolver;
import com.github.offercat.access.rate.limiter.core.AccessRateLimitContext;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 通行频率访问控制
 *
 * @author 徐通 xutong34
 * @since 2020年09月01日 13:08:07
 */
@AllArgsConstructor
public class AccessRateLimitAspect extends AbstractAop {

    private AccessRateLimitContext accessRateLimitContext;

    @Override
    public Object around(ProxyPoint point, InjectFunction<Object> injectFunction) throws Throwable {
        if (!accessRateLimitContext.limitEnable()) {
            return injectFunction.get();
        }
        RateLimitPoint limitPoint = accessRateLimitContext.lookupAndRegister(point.getMethod());
        if (limitPoint.count() <= 0 || limitPoint.time() <= 0 || !limitPoint.limitEnable()) {
            return injectFunction.get();
        }
        // 解析 limitFlags
        LimitFactorResolver resolver = accessRateLimitContext.getLimitFactorResolver();
        List<String> limitFlags = resolver.resolve(
                point.getMethod().getParameters(),
                point.getArgs(),
                limitPoint.limitFactorFunctions(),
                point.getProxy()
        );
        // 初始化 limitKey
        return limitPoint.getAccessRateLimiter().handle(
                limitPoint.generateLimitKey(limitFlags),
                limitPoint,
                () -> limitPoint.backupFunction().invoke(point.getProxy()),
                injectFunction
        );
    }
}
