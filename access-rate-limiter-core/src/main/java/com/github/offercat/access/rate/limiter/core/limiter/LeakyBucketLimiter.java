package com.github.offercat.access.rate.limiter.core.limiter;

import com.github.offercat.access.rate.limiter.core.RateLimitPoint;
import com.github.offercat.access.rate.limiter.core.util.TimeUtil;
import lombok.SneakyThrows;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 漏桶算法限流器
 *
 * @author 徐通 xutong34
 * @since 2020年09月02日 21:32:48
 */
public class LeakyBucketLimiter extends AbstractAccessLimiter {

    @Override
    protected LimitTicket createLimitTicket(RateLimitPoint rateLimitPoint) {
        return new LeakyBucket(rateLimitPoint.count(), rateLimitPoint.time(), rateLimitPoint.timeUnit());
    }

    /**
     * 漏桶
     */
    private static class LeakyBucket implements LimitTicket {

        /**
         * 放水的桶
         */
        final ArrayBlockingQueue<String> bucket;

        public LeakyBucket(int count, int time, TimeUnit timeUnit) {
            this.bucket = new ArrayBlockingQueue<>(count);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (bucket.size() == 0) {
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
