package com.github.offercat.access.rate.limiter.core;

import com.github.offercat.access.rate.limiter.core.limiter.TokenBucketLimiter;
import com.github.offercat.access.rate.limiter.core.util.ExceptionUtil;
import com.github.offercat.access.rate.limiter.core.limiter.AccessRateLimiter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 抽象的通行限制点
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 14:08:14
 */
@Setter
@ToString
public class GeneralAccessRateLimitPoint implements RateLimitPoint {

    /**
     * LimitPoint 唯一标识
     */
    private String id;

    /**
     * 时间长度
     */
    private int time;

    /**
     * 时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 单位时间内的调用次数
     */
    private int count;

    /**
     * LimitPoint 工厂，LimitPoint 容器
     */
    private LimitPointFactory limitPointFactory;

    /**
     * LimitPoint 通行流量限制器
     */
    private AccessRateLimiter accessRateLimiter;

    /**
     * 是否阻塞登录
     */
    private boolean blockWait = false;

    /**
     * 阻塞时长
     */
    private int maxWaitTime;

    /**
     * 是否开启限流
     */
    private boolean limitEnable = true;

    /**
     * 注入的方法
     */
    private Method injectMethod;

    /**
     * 阻塞时长
     */
    private TimeUnit waitTimeUnit;

    /**
     * 备用方法
     */
    private BackupFunction backupFunction;

    /**
     * 限制因子获取方法
     */
    private LimitFactorFunction[] limitFactorFunctions;

    public GeneralAccessRateLimitPoint(String id, Method injectMethod, LimitPointFactory limitPointFactory) {
        ExceptionUtil.paramNull(id, "LimitPoint id 不能为空");
        ExceptionUtil.paramNull(injectMethod, "LimitPoint injectMethod 不能为空");
        this.id = id;
        this.injectMethod = injectMethod;
        this.limitPointFactory = limitPointFactory.registerLimitPoint(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int time() {
        return time;
    }

    @Override
    public TimeUnit timeUnit() {
        return timeUnit;
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public boolean blockWait() {
        return blockWait;
    }

    @Override
    public int maxWaitTime() {
        return maxWaitTime;
    }

    @Override
    public boolean limitEnable() {
        return limitEnable;
    }

    @Override
    public Method injectMethod() {
        return injectMethod;
    }

    @Override
    public TimeUnit waitTimeUnit() {
        return waitTimeUnit;
    }

    @Override
    public LimitFactorFunction[] limitFactorFunctions() {
        return limitFactorFunctions;
    }

    @Override
    public BackupFunction backupFunction() {
        if (backupFunction == null) {
            return target -> null;
        }
        return backupFunction;
    }

    @Override
    public LimitPointFactory getLimitPointFactory() {
        return limitPointFactory;
    }

    @Override
    public String generateLimitKey(List<String> limitFlags) {
        StringBuilder limitKey = new StringBuilder("{");
        for (int i = 0; i < limitFlags.size(); i++) {
            if (i == 0) {
                limitKey.append(limitFlags.get(i));
            } else {
                limitKey.append(", ").append(limitFlags.get(i));
            }
        }
        limitKey.append("}");
        return limitKey.toString();
    }

    @Override
    public AccessRateLimiter getAccessRateLimiter() {
        if (this.accessRateLimiter != null) {
            return this.accessRateLimiter;
        }
        synchronized (this) {
            if (this.accessRateLimiter == null) {
                this.accessRateLimiter = new TokenBucketLimiter();
            }
            return accessRateLimiter;
        }
    }
}
