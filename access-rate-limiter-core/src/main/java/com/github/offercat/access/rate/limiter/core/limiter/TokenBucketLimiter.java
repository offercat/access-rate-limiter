package com.github.offercat.access.rate.limiter.core.limiter;

import com.github.offercat.access.rate.limiter.core.RateLimitPoint;
import com.github.offercat.access.rate.limiter.core.util.TimeUtil;
import lombok.SneakyThrows;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 令牌桶限流器
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 12:13:46
 */
public class TokenBucketLimiter extends AbstractAccessLimiter {

    @Override
    protected LimitTicket createLimitTicket(RateLimitPoint rateLimitPoint) {
        return new TokenBucket(rateLimitPoint.count(), rateLimitPoint.time(), rateLimitPoint.timeUnit());
    }

    /**
     * 令牌桶
     */
    private static class TokenBucket implements LimitTicket {

        /**
         * 令牌存放的位置 , 用一个队列维护
         */
        private final ArrayBlockingQueue<String> bucket;

        public TokenBucket(int count, int time, TimeUnit timeUnit) {
            this.bucket = new ArrayBlockingQueue<>(count);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (bucket.size() < count) {
                        try {
                            bucket.put("");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 0, TimeUtil.calculateMilliseconds(time, timeUnit) / count);
        }

        @Override
        public boolean get() {
            return bucket.poll() != null;
        }

        @SneakyThrows
        @Override
        public void blockGet() {
            bucket.take();
        }

        @SneakyThrows
        @Override
        public boolean blockGet(int time, TimeUnit timeUnit) {
            return bucket.poll(time, timeUnit) != null;
        }
    }
}
