package com.github.offercat.access.rate.limiter.core.annotated;

import com.github.offercat.access.rate.limiter.core.util.AnnotatedUtil;
import com.github.offercat.access.rate.limiter.core.util.ExceptionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 16:17:22
 */
public class DefaultAnnotatedMethodScanner implements AnnotatedMethodScanner {

    public static final DefaultAnnotatedMethodScanner DEFAULT_ANNOTATED_METHOD_SCANNER = new DefaultAnnotatedMethodScanner();

    @Override
    public List<Method> scanMethodByDefaultAnnotation(Class<?> target) {
        return this.scanMethod(target, AnnotatedUtil.SUPPORT_ANNOTATION);
    }

    @Override
    public List<Method> scanMethodByDefaultAnnotation(List<Class<?>> targetList) {
        return this.scanMethod(targetList, AnnotatedUtil.SUPPORT_ANNOTATION);
    }

    @SafeVarargs
    @Override
    public final List<Method> scanMethod(Class<?> target, Class<? extends Annotation> ...annotationTypes) {
        ExceptionUtil.paramNull(target, "指定扫描类 target 不能为空");
        ExceptionUtil.paramNull(annotationTypes, "annotationTypes 不能为空");
        Method[] methods = target.getDeclaredMethods();
        if (methods.length == 0) {
            return Collections.emptyList();
        }
        List<Method> result = new ArrayList<>();
        for (Method method: methods) {
            if (AnnotatedUtil.isAnnotatedBy(method, annotationTypes)){
                result.add(method);
            }
        }
        return result;
    }

    @SafeVarargs
    @Override
    public final List<Method> scanMethod(List<Class<?>> targetList, Class<? extends Annotation>... annotationTypes) {
        ExceptionUtil.paramNull(annotationTypes, "annotationTypes 不能为空");
        if (targetList == null || targetList.isEmpty()) {
            return Collections.emptyList();
        }
        List<Method> result = new ArrayList<>();
        targetList.forEach(target -> {
            Method[] methods = target.getDeclaredMethods();
            if (methods.length == 0) {
                return;
            }
            for (Method method: methods) {
                if (AnnotatedUtil.isAnnotatedBy(method, annotationTypes)){
                    result.add(method);
                }
            }
        });
        return result;
    }
}
