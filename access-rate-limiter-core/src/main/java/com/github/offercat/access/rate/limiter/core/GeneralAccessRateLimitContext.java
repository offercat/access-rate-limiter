package com.github.offercat.access.rate.limiter.core;

import com.github.offercat.access.rate.limiter.core.proxy.AccessLimitProxy;
import com.github.offercat.access.rate.limiter.core.proxy.AccessRateLimitAspect;
import com.github.offercat.access.rate.limiter.core.resolver.DefaultLimitPointResolver;
import com.github.offercat.access.rate.limiter.core.resolver.LimitPointResolver;
import com.github.offercat.access.rate.limiter.core.util.ExceptionUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年09月01日 11:48:30
 */
public class GeneralAccessRateLimitContext extends GeneralLimitPointFactory implements AccessRateLimitContext {

    private boolean limitEnable = true;
    private List<Class<?>> controllerTypes;
    private LimitPointResolver limitPointResolver;

    public GeneralAccessRateLimitContext() {
        limitPointResolver = DefaultLimitPointResolver.DEFAULT_LIMIT_POINT_RESOLVER;
    }

    @Override
    public void refresh() {

    }

    public void setLimitEnable(boolean limitEnable) {
        this.limitEnable = limitEnable;
    }

    @Override
    public boolean hasRefreshed() {
        return true;
    }

    public void setControllerTypes(List<Class<?>> controllerTypes) {
        this.controllerTypes = controllerTypes;
    }

    public List<Class<?>> getControllerTypes() {
        return controllerTypes;
    }

    @Override
    public boolean allowRefreshManyTimes() {
        return false;
    }

    @Override
    public boolean limitEnable() {
        return limitEnable;
    }

    @Override
    public void setLimitPointResolver(LimitPointResolver limitPointResolver) {
        ExceptionUtil.paramNull(limitPointResolver, "limitPointResolver 不能为空");
        this.limitPointResolver = limitPointResolver;
    }

    @Override
    public LimitPointResolver getLimitPointResolver() {
        return limitPointResolver;
    }

    @Override
    public RateLimitPoint lookupAndRegister(Method method) {
        String limitPointId = limitPointResolver.generateUniqueLimitPointId(method);
        RateLimitPoint rateLimitPoint = this.getLimitPoints().get(limitPointId);
        if (rateLimitPoint == null) {
            synchronized (this) {
                rateLimitPoint = this.getLimitPoints().get(limitPointId);
                if (rateLimitPoint == null) {
                    rateLimitPoint = limitPointResolver.resolve(method, this);
                    this.registerLimitPoint(rateLimitPoint);
                    return rateLimitPoint;
                }
            }
        }
        return rateLimitPoint;
    }

    @Override
    public List<Class<?>> controllerTypes() {
        return controllerTypes;
    }

    @Override
    public <T> T getProxy(T bean) {
        AccessLimitProxy<T> accessLimitProxy = new AccessLimitProxy<>();
        return accessLimitProxy.getProxy(bean, new AccessRateLimitAspect(this));
    }

    @Override
    public <T> T getProxy(Class<T> type) {
        T instance;
        try {
            instance = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        AccessLimitProxy<T> accessLimitProxy = new AccessLimitProxy<>();
        return accessLimitProxy.getProxy(instance, new AccessRateLimitAspect(this));
    }
}
