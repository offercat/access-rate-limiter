package com.github.offercat.access.rate.limiter.core;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年09月03日 14:25:45
 */
@FunctionalInterface
public interface BackupFunction {

    /**
     * 后备策略
     *
     * @param target target
     * @return target
     */
    Object invoke(Object target);
}
