package com.github.offercat.access.rate.limiter.core;

import com.github.offercat.access.rate.limiter.core.resolver.DefaultLimitFactorResolver;
import com.github.offercat.access.rate.limiter.core.resolver.LimitFactorResolver;
import com.github.offercat.access.rate.limiter.core.util.ExceptionUtil;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * 通用速率限制器
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 14:43:44
 */
public class GeneralLimitPointFactory implements LimitPointFactory {

    private static final Logger log = Logger.getLogger(GeneralLimitPointFactory.class.getName());

    private boolean logEnable;
    private Boolean supportCover;
    private LimitFactorResolver limitFactorResolver;
    private Map<String, RateLimitPoint> limitPointMap = new ConcurrentHashMap<>();

    public void setSupportCover(boolean supportCover) {
        this.supportCover = supportCover;
    }

    public GeneralLimitPointFactory() {
        this.limitFactorResolver = new DefaultLimitFactorResolver();
    }

    @Override
    public boolean supportCover() {
        if (supportCover == null) {
            return true;
        }
        return supportCover;
    }

    @Override
    public int limitPointsSize() {
        return limitPointMap.size();
    }

    @Override
    public LimitPointFactory registerLimitPoint(RateLimitPoint rateLimitPoint) {
        ExceptionUtil.paramNull(rateLimitPoint, "LimitPoint 不能为空");
        ExceptionUtil.paramNull(rateLimitPoint.getId(), "LimitPoint id 不能为空");
        synchronized (this) {
            RateLimitPoint cachePoint = limitPointMap.get(rateLimitPoint.getId());
            if (cachePoint != null && cachePoint != rateLimitPoint && !this.supportCover()) {
                throw new InvalidParameterException("LimitPoint 注册失败，LimitPoint id 发生冲突");
            }
            limitPointMap.put(rateLimitPoint.getId(), rateLimitPoint);
        }
        return this;
    }

    @Override
    public Map<String, RateLimitPoint> getLimitPoints() {
        return limitPointMap;
    }

    @Override
    public RateLimitPoint lookupLimitPoint(String limitPointId) {
        return limitPointMap.get(limitPointId);
    }

    @Override
    public LimitFactorResolver getLimitFactorResolver() {
        return limitFactorResolver;
    }

    @Override
    public void setLimitFactorResolver(LimitFactorResolver limitFactorResolver) {
        ExceptionUtil.paramNull(limitFactorResolver, "limitFactorResolver 不能为空");
        this.limitFactorResolver = limitFactorResolver;
    }

    @Override
    public boolean isLogEnable() {
        return logEnable;
    }

    public void setLogEnable(boolean logEnable) {
        this.logEnable = logEnable;
    }
}
