package com.github.offercat.access.rate.limiter.core.proxy;

/**
 * 统一aop接口
 *
 * @author 徐通 xutong34
 * @since 2019年10月7日09:54:47
 */
public abstract class AbstractAop {

    public void before(ProxyPoint point) {}

    public Object around(ProxyPoint point, InjectFunction<Object> function) throws Throwable {
        return function.get();
    }

    public void after(ProxyPoint point) {}

    public void afterReturning(ProxyPoint point) {}

    public void afterThrowing(ProxyPoint point, Throwable e) {}
}
