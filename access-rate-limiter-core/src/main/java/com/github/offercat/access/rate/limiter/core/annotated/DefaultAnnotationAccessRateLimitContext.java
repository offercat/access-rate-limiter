package com.github.offercat.access.rate.limiter.core.annotated;

import com.github.offercat.access.rate.limiter.core.RateLimitPoint;
import com.github.offercat.access.rate.limiter.core.GeneralAccessRateLimitContext;
import com.github.offercat.access.rate.limiter.core.exception.ContextRefreshException;
import com.github.offercat.access.rate.limiter.core.util.ExceptionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

/**
 * 默认注解限流上下文
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 15:22:36
 */
public class DefaultAnnotationAccessRateLimitContext extends GeneralAccessRateLimitContext implements AnnotationAccessRateLimitContext {

    private static final Logger log = Logger.getLogger(DefaultAnnotationAccessRateLimitContext.class.getName());

    /**
     * 扫描路径
     */
    private String scanPath;

    /**
     * 是否已经被刷新过
     */
    private boolean hasRefreshed;

    /**
     * 类扫描器
     */
    private ClassScanner classScanner;

    /**
     * 注解方法扫描器
     */
    protected AnnotatedMethodScanner annotatedMethodScanner;

    /**
     * 被标识为控制器的注解类型
     */
    private Class<? extends Annotation> scanClassAnnotationType;

    public DefaultAnnotationAccessRateLimitContext() {
        classScanner = DefaultClassScanner.DEFAULT_CLASS_PATH_CLASS_SCANNER;
        annotatedMethodScanner = DefaultAnnotatedMethodScanner.DEFAULT_ANNOTATED_METHOD_SCANNER;
        scanClassAnnotationType = AccessLimitController.class;
    }

    public DefaultAnnotationAccessRateLimitContext(String scanPath) {
        this();
        this.scanPath = scanPath;
    }

    public void setClassScanner(ClassScanner classScanner) {
        ExceptionUtil.paramNull(classScanner, "classPathClassScanner 不能为空");
        this.classScanner = classScanner;
    }

    public void setAnnotatedMethodScanner(AnnotatedMethodScanner annotatedMethodScanner) {
        ExceptionUtil.paramNull(annotatedMethodScanner, "annotatedMethodScanner 不能为空");
        this.annotatedMethodScanner = annotatedMethodScanner;
    }

    @Override
    public void setScanClassAnnotationType(Class<? extends Annotation> scanClassAnnotationType) {
        ExceptionUtil.paramNull(scanClassAnnotationType, "scanClassAnnotationType 不能为空");
        this.scanClassAnnotationType = scanClassAnnotationType;
    }

    public Class<? extends Annotation> getScanClassAnnotationType() {
        return scanClassAnnotationType;
    }

    @Override
    public String scanPath() {
        return scanPath;
    }

    @Override
    public synchronized void refresh() {
        if (!this.allowRefreshManyTimes() && this.hasRefreshed()) {
            throw new ContextRefreshException("默认注解限流器不支持多次刷新上下文！");
        }
        if (ExceptionUtil.isNull(this.scanPath())) {
            throw new ContextRefreshException("扫描路径为空，刷新失败！");
        }
        List<Class<?>> controllerTypeList = classScanner.scanClass(this.scanPath(), scanClassAnnotationType);
        this.setControllerTypes(controllerTypeList);
        List<Method> annotatedMethods = annotatedMethodScanner.scanMethodByDefaultAnnotation(controllerTypeList);
        List<RateLimitPoint> rateLimitPointList = this.getLimitPointResolver().resolve(annotatedMethods, this);
        rateLimitPointList.forEach(this::registerLimitPoint);
        this.hasRefreshed = true;
    }

    @Override
    public boolean hasRefreshed() {
        return hasRefreshed;
    }

    protected void setHasRefreshed(boolean hasRefreshed) {
        this.hasRefreshed = hasRefreshed;
    }

    @Override
    public boolean allowRefreshManyTimes() {
        return false;
    }
}
