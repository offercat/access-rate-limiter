package com.github.offercat.access.rate.limiter.core;

/**
 * 字符串因子
 *
 * @author 徐通 xutong34
 * @since 2020年09月03日 15:18:58
 */
@FunctionalInterface
public interface LimitFactorFunction {

    /**
     * 获取字符串因子
     *
     * @param target 字符串因子
     * @return 字符串因子
     */
    String getStrFactor(Object target);
}
