package com.github.offercat.access.rate.limiter.core;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * qps 限流点
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 14:29:48
 */
public class QpsAccessRateLimitPoint extends GeneralAccessRateLimitPoint {

    public QpsAccessRateLimitPoint(String id, Method injectMethod, LimitPointFactory limitPointFactory) {
        super(id, injectMethod, limitPointFactory);
    }

    @Override
    public int time() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
