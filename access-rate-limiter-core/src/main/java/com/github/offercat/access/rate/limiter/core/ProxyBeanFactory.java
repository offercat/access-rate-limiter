package com.github.offercat.access.rate.limiter.core;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年09月01日 13:03:21
 */
public interface ProxyBeanFactory {

    /**
     * 获取代理bean
     *
     * @param bean bean
     * @return 代理bean
     */
    <T> T getProxy(T bean);

    /**
     * 获取代理bean
     *
     * @param type type
     * @return 代理bean
     */
    <T> T getProxy(Class<T> type);
}
