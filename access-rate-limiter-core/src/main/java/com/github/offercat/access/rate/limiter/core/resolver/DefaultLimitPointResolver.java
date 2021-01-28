package com.github.offercat.access.rate.limiter.core.resolver;

import com.github.offercat.access.rate.limiter.core.*;
import com.github.offercat.access.rate.limiter.core.annotated.AccessRateLimit;
import com.github.offercat.access.rate.limiter.core.annotated.QpsLimit;
import com.github.offercat.access.rate.limiter.core.exception.MethodResolveException;
import com.github.offercat.access.rate.limiter.core.exception.MethodInvokeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 限制点解析器
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 17:28:49
 */
public class DefaultLimitPointResolver implements LimitPointResolver {

    public static final DefaultLimitPointResolver DEFAULT_LIMIT_POINT_RESOLVER = new DefaultLimitPointResolver();

    @Override
    public RateLimitPoint resolve(Method method, LimitPointFactory factory) {
        AccessRateLimit accessRateLimit = method.getDeclaredAnnotation(AccessRateLimit.class);
        QpsLimit qpsLimit = method.getDeclaredAnnotation(QpsLimit.class);
        String id = this.generateUniqueLimitPointId(method);
        RateLimitPoint rateLimitPoint = null;
        if (accessRateLimit != null) {
            rateLimitPoint = this.initRateLimitPoint(
                    accessRateLimit,
                    new GeneralAccessRateLimitPoint(id, method, factory),
                    method.getDeclaringClass()
            );
        } else if (qpsLimit != null) {
            rateLimitPoint = this.initQpsLimitPoint(
                    qpsLimit,
                    new QpsAccessRateLimitPoint(id, method, factory),
                    method.getDeclaringClass()
            );
        }
        return rateLimitPoint;
    }

    @Override
    public List<RateLimitPoint> resolve(List<Method> methodList, LimitPointFactory limitPointFactory) {
        return methodList.stream().map(method -> this.resolve(method, limitPointFactory)).collect(Collectors.toList());
    }

    @Override
    public String generateUniqueLimitPointId(Method method) {
        StringBuilder methodName = new StringBuilder();
        methodName.append(method.getDeclaringClass().getName());
        methodName.append(".");
        methodName.append(method.getName());
        Class<?>[] paraTypes = method.getParameterTypes();
        methodName.append("(");
        for (int i = 0; i < paraTypes.length; i++) {
            if (i == 0) {
                methodName.append(paraTypes[i].getName());
            } else {
                methodName.append(", ").append(paraTypes[i].getName());
            }
        }
        methodName.append(")");
        return methodName.toString();
    }

    private RateLimitPoint initRateLimitPoint(AccessRateLimit accessRateLimit, GeneralAccessRateLimitPoint limitPoint, Class<?> targetClass) {
        limitPoint.setCount(accessRateLimit.count());
        limitPoint.setTime(accessRateLimit.time());
        limitPoint.setTimeUnit(accessRateLimit.timeUnit());
        try {
            limitPoint.setAccessRateLimiter(accessRateLimit.accessRateLimiter().newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        limitPoint.setBlockWait(accessRateLimit.blockWait());
        limitPoint.setMaxWaitTime(accessRateLimit.maxWaitTime());
        limitPoint.setWaitTimeUnit(accessRateLimit.waitTimeUnit());
        limitPoint.setBackupFunction(this.resolveBackupFunction(accessRateLimit.backupMethod(), targetClass));
        limitPoint.setLimitFactorFunctions(this.resolveLimitFactorFunctions(accessRateLimit.limitFactorMethod(), targetClass));
        return limitPoint;
    }

    private RateLimitPoint initQpsLimitPoint(QpsLimit qpsLimit, QpsAccessRateLimitPoint limitPoint, Class<?> targetClass) {
        limitPoint.setCount(qpsLimit.value());
        limitPoint.setTime(1);
        limitPoint.setTimeUnit(TimeUnit.SECONDS);
        try {
            limitPoint.setAccessRateLimiter(qpsLimit.accessRateLimiter().newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        limitPoint.setBlockWait(qpsLimit.blockWait());
        limitPoint.setMaxWaitTime(qpsLimit.maxWaitTime());
        limitPoint.setWaitTimeUnit(qpsLimit.waitTimeUnit());
        limitPoint.setBackupFunction(this.resolveBackupFunction(qpsLimit.backupMethod(), targetClass));
        limitPoint.setLimitFactorFunctions(this.resolveLimitFactorFunctions(qpsLimit.limitFactorMethod(), targetClass));
        return limitPoint;
    }

    private BackupFunction resolveBackupFunction(String methodExpression, Class<?> targetClass) {
        if (methodExpression == null || "".equals(methodExpression) || targetClass == null) {
            return null;
        }
        Method method = this.resolveTargetMethod(methodExpression, targetClass);
        return target -> {
            try {
                return method.invoke(target);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new MethodInvokeException("备用方法 " + methodExpression + " 调用异常！");
            }
        };
    }

    private LimitFactorFunction[] resolveLimitFactorFunctions(String[] methodExpressions, Class<?> targetClass) {
        if (methodExpressions == null || methodExpressions.length == 0 || targetClass == null) {
            return null;
        }
        LimitFactorFunction[] limitFactorFunctions = new LimitFactorFunction[methodExpressions.length];
        for (int i = 0; i < methodExpressions.length; i++) {
            String methodExpression = methodExpressions[i];
            if (methodExpression == null || methodExpression.length() == 0) {
                continue;
            }
            Method method = this.resolveTargetMethod(methodExpressions[i], targetClass);
            limitFactorFunctions[i] = target -> {
                Object result;
                try {
                    result = method.invoke(target);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new MethodInvokeException("限制因子方法 " + methodExpression + " 调用异常！", e);
                }
                if (!(result instanceof String)) {
                    throw new MethodInvokeException("限制因子方法 " + methodExpression + " 调用异常，限制因子方法必须返回 String 类型！");
                }
                return (String) result;
            };
        }
        return limitFactorFunctions;
    }

    private Method resolveTargetMethod(String methodExpression, Class<?> targetClass) {
        String thisTargetClass = "this";
        String[] resolveArray = methodExpression.split("#");
        if (resolveArray.length != 2) {
            throw new MethodResolveException("方法表达式解析错误 methodExpression = " + methodExpression);
        }
        if (!thisTargetClass.equals(resolveArray[0])) {
            try {
                targetClass = Class.forName(resolveArray[0]);
            } catch (ClassNotFoundException e) {
                throw new MethodResolveException("方法表达式解析错误，不存在的类 methodExpression = " + methodExpression, e);
            }
        }
        Method method;
        try {
            method = targetClass.getDeclaredMethod(resolveArray[1]);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new MethodInvokeException("初始化方法 " + resolveArray[1] + " 异常！", e);
        }
        return method;
    }
}
