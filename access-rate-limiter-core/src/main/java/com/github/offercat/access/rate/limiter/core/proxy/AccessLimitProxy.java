package com.github.offercat.access.rate.limiter.core.proxy;

import com.github.offercat.access.rate.limiter.core.util.AnnotatedUtil;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 公用动态代理
 *
 * @author 徐通 xutong34
 * @since 2019年10月7日09:42:02
 */
@SuppressWarnings("unchecked")
public class AccessLimitProxy<T> implements MethodInterceptor {

    public static final Map<Class<?>, Object> INIT_VALUE = new HashMap<>();

    static {
        INIT_VALUE.put(int.class, 0);
        INIT_VALUE.put(long.class, 0);
        INIT_VALUE.put(float.class, 0);
        INIT_VALUE.put(double.class, 0);
        INIT_VALUE.put(char.class, ' ');
        INIT_VALUE.put(boolean.class, false);
        INIT_VALUE.put(short.class, 0);
        INIT_VALUE.put(byte.class, 0);
    }

    private AbstractAop aop;
    private T target;

    public T getProxy(T target, AbstractAop aop) {
        this.aop = aop;
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        Constructor<?> constructor = target.getClass().getConstructors()[0];
        Class<?>[] paraTypes = constructor.getParameterTypes();
        return (T) enhancer.create(paraTypes, this.initNullParameters(paraTypes));
    }

    private Object[] initNullParameters(Class<?>[] paraTypes) {
        Object[] result = new Object[paraTypes.length];
        for (int i=0; i<paraTypes.length; i++) {
            result[i] = INIT_VALUE.get(paraTypes[i]);
        }
        return result;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (!AnnotatedUtil.isAnnotatedByDefaultAnnotation(method)) {
            return methodProxy.invoke(target, args);
        }
        ProxyPoint point = new ProxyPoint(target, method, args);
        try {
            Object result;
            try {
                result = aop.around(point, () -> {
                    aop.before(point);
                    return methodProxy.invoke(target, args);
                });
            } finally {
                aop.after(point);
            }
            aop.afterReturning(point);
            return result;
        } catch (Throwable e) {
            aop.afterThrowing(point, e);
            throw e;
        }
    }
}
