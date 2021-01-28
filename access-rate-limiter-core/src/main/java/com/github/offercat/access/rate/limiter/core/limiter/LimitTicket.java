package com.github.offercat.access.rate.limiter.core.limiter;

import java.util.concurrent.TimeUnit;

/**
 * 限制通行资格
 *
 * @author 徐通 xutong34
 * @since 2020年09月03日 17:51:20
 */
public interface LimitTicket {

    /**
     * 获取通行资格
     *
     * @return 是否获取成功
     */
    boolean get();

    /**
     * 获取通行资格，获取不到就阻塞
     */
    void blockGet();

    /**
     * 获取通行资格，指定时间阻塞
     *
     * @param time     time
     * @param timeUnit timeUnit
     * @return 是否获取成功
     */
    boolean blockGet(int time, TimeUnit timeUnit);
}
