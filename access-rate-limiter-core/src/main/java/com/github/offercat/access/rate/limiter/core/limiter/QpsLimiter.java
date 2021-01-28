package com.github.offercat.access.rate.limiter.core.limiter;

import com.github.offercat.access.rate.limiter.core.RateLimitPoint;
import com.github.offercat.access.rate.limiter.core.proxy.InjectFunction;
import com.google.common.util.concurrent.RateLimiter;
import com.github.offercat.access.rate.limiter.core.QpsAccessRateLimitPoint;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年09月02日 11:46:46
 */
@SuppressWarnings("UnstableApiUsage")
public class QpsLimiter extends AbstractAccessLimiter {

    @Override
    public Object handle(String limitKey, RateLimitPoint limitPoint, Supplier<Object> backupFunction, InjectFunction<Object> injectFunction) throws Throwable {
        if (!(limitPoint instanceof QpsAccessRateLimitPoint)) {
            throw new IllegalArgumentException("非法的limitPoint类型，请传递QpsAccessLimitPoint类型！");
        }
        QpsAccessRateLimitPoint qpsPoint = (QpsAccessRateLimitPoint) limitPoint;
        return super.handle(limitKey, qpsPoint, backupFunction, injectFunction);
    }

    @Override
    protected LimitTicket createLimitTicket(RateLimitPoint rateLimitPoint) {
        return new GuavaRateLimitTicket(rateLimitPoint.count());
    }

    private static class GuavaRateLimitTicket implements LimitTicket {

        private RateLimiter rateLimiter;

        public GuavaRateLimitTicket(int qps) {
            rateLimiter = RateLimiter.create(qps);
        }

        @Override
        public boolean get() {
            return rateLimiter.tryAcquire();
        }

        @Override
        public void blockGet() {
            rateLimiter.acquire();
        }

        @Override
        public boolean blockGet(int time, TimeUnit timeUnit) {
            return rateLimiter.tryAcquire(1, time, timeUnit);
        }
    }
}
