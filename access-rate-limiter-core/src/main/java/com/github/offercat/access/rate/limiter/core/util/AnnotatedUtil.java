package com.github.offercat.access.rate.limiter.core.util;

import com.github.offercat.access.rate.limiter.core.annotated.AccessRateLimit;
import com.github.offercat.access.rate.limiter.core.annotated.QpsLimit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年09月02日 16:29:15
 */
public class AnnotatedUtil {

    @SuppressWarnings("unchecked")
    public static final Class<? extends Annotation>[] SUPPORT_ANNOTATION = new Class[]{AccessRateLimit.class, QpsLimit.class};

    public static boolean isAnnotatedByDefaultAnnotation(Method method) {
        return isAnnotatedBy(method, SUPPORT_ANNOTATION);
    }

    @SafeVarargs
    public static boolean isAnnotatedBy(Method method, Class<? extends Annotation>... annotationTypes) {
        for (Class<? extends Annotation> annotationType: annotationTypes) {
            if (method.isAnnotationPresent(annotationType)) {
                return true;
            }
        }
        return false;
    }
}
