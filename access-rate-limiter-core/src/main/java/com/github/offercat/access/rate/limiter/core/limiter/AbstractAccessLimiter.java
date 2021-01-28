package com.github.offercat.access.rate.limiter.core.limiter;

import com.github.offercat.access.rate.limiter.core.RateLimitPoint;
import com.github.offercat.access.rate.limiter.core.proxy.InjectFunction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * 抽象的限流器
 *
 * @author 徐通 xutong34
 * @since 2020年09月02日 21:39:58
 */
public abstract class AbstractAccessLimiter implements AccessRateLimiter {

    private static final Logger log = Logger.getLogger(AbstractAccessLimiter.class.getName());

    private final Map<String, LimitTicket> rateLimiterMap;

    public AbstractAccessLimiter() {
        this.rateLimiterMap = new ConcurrentHashMap<>();
    }

    @Override
    public Object handle(String limitKey, RateLimitPoint limitPoint, Supplier<Object> backupFunction, InjectFunction<Object> injectFunction) throws Throwable {
        LimitTicket limitTicket = this.getLimitTicket(limitKey, limitPoint);

        String methodName = limitPoint.injectMethod().getName();
        if (limitPoint.blockWait()) {
            if (limitPoint.maxWaitTime() <= 0) {
                long begin = System.currentTimeMillis();
                limitTicket.blockGet();
                long end = System.currentTimeMillis();
                long millisecond = end - begin;
                if (limitPoint.getLimitPointFactory().isLogEnable()) {
                    log.info("全阻塞请求，获取限流锁成功，共阻塞 " + millisecond + " ms，请求方法：" + methodName);
                }
                return injectFunction.get();
            } else {
                long begin = System.currentTimeMillis();
                boolean success = limitTicket.blockGet(limitPoint.maxWaitTime(), limitPoint.waitTimeUnit());
                long end = System.currentTimeMillis();
                long millisecond = end - begin;
                if (limitPoint.getLimitPointFactory().isLogEnable()) {
                    if (success) {
                        log.info("超时阻塞请求，获取限流锁成功，共阻塞 " + millisecond + " ms，请求方法：" + methodName);
                    } else {
                        log.info("超时阻塞请求，获取限流锁失败，共阻塞 " + millisecond + " ms，请求方法：" + methodName + "，调用备用方法");
                    }
                }
                if (success) {
                    return injectFunction.get();
                }
            }
        } else {
            boolean success = limitTicket.get();
            if (limitPoint.getLimitPointFactory().isLogEnable()) {
                if (success) {
                    log.info("非阻塞请求，获取限流锁成功，请求方法：" + methodName);
                } else {
                    log.info("非阻塞请求，获取限流锁失败，请求方法：" + methodName + "，调用备用方法");
                }
            }
            if (success) {
                return injectFunction.get();
            }
        }
        return backupFunction.get();
    }

    /**
     * 获取映射的 bucket
     *
     * @param limitKey limitKey
     * @return Bucket
     */
    protected LimitTicket getLimitTicket(String limitKey, RateLimitPoint rateLimitPoint) {
        LimitTicket tokenBucket = rateLimiterMap.get(limitKey);
        if (tokenBucket == null) {
            synchronized (rateLimiterMap) {
                tokenBucket = rateLimiterMap.get(limitKey);
                if (tokenBucket == null) {
                    tokenBucket = this.createLimitTicket(rateLimitPoint);
                    rateLimiterMap.put(limitKey, tokenBucket);
                }
            }
        }
        return tokenBucket;
    }

    /**
     * 新建限制器
     *
     * @param rateLimitPoint rateLimitPoint
     * @return Limiter
     */
    protected abstract LimitTicket createLimitTicket(RateLimitPoint rateLimitPoint);
}
