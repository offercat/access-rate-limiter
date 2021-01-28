package com.github.offercat.access.rate.limiter.core.resolver;

import com.github.offercat.access.rate.limiter.core.LimitFactorFunction;

import java.lang.reflect.Parameter;
import java.util.List;

/**
 * 限制因子解析器
 *
 * @author 徐通 xutong34
 * @since 2020年09月01日 21:37:42
 */
public interface LimitFactorResolver {

    /**
     * 限流因子解析
     *
     * @param parameters parameters
     * @param args args
     * @param limitFactorFunctions limitFactorFunctions
     * @param target target
     * @return list
     */
    List<String> resolve(Parameter[] parameters, Object[] args, LimitFactorFunction[] limitFactorFunctions, Object target);
}
