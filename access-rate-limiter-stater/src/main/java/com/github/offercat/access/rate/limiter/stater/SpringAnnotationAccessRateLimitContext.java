package com.github.offercat.access.rate.limiter.stater;

import com.github.offercat.access.rate.limiter.core.RateLimitPoint;
import com.github.offercat.access.rate.limiter.core.annotated.DefaultAnnotationAccessRateLimitContext;
import com.github.offercat.access.rate.limiter.core.exception.ContextRefreshException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * spring boot 注解限流容器
 *
 * @author 徐通 xutong34
 * @since 2020年09月04日 09:35:52
 */
@Slf4j
public class SpringAnnotationAccessRateLimitContext extends DefaultAnnotationAccessRateLimitContext implements BeanPostProcessor {

    @Override
    public synchronized void refresh() {
        if (!this.allowRefreshManyTimes() && this.hasRefreshed()) {
            throw new ContextRefreshException("默认注解限流器不支持多次刷新上下文！");
        }
        List<Method> annotatedMethods = annotatedMethodScanner.scanMethodByDefaultAnnotation(this.getControllerTypes());
        List<RateLimitPoint> rateLimitPointList = this.getLimitPointResolver().resolve(annotatedMethods, this);
        log.info("开始预注册限流点共计 {} 个", rateLimitPointList.size());
        rateLimitPointList.forEach(this::registerLimitPoint);
        this.setHasRefreshed(true);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (this.getControllerTypes() == null) {
            this.setControllerTypes(new ArrayList<>());
        }
        if (bean.getClass().isAnnotationPresent(this.getScanClassAnnotationType())) {
            this.getControllerTypes().add(bean.getClass());
            return this.getProxy(bean);
        }
        return bean;
    }
}
