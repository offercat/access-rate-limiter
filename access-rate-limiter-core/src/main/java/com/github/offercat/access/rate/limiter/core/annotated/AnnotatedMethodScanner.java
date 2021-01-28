package com.github.offercat.access.rate.limiter.core.annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 15:54:10
 */
public interface AnnotatedMethodScanner {

    /**
     * 方法扫描器
     *
     * @param target 扫描目标
     * @return 方法列表
     */
    List<Method> scanMethodByDefaultAnnotation(Class<?> target);

    /**
     * 方法扫描器
     *
     * @param targetList 扫描目标
     * @return 方法列表
     */
    List<Method> scanMethodByDefaultAnnotation(List<Class<?>> targetList);

    /**
     * 方法扫描器
     *
     * @param target 扫描目标
     * @param annotationTypes 指定注解
     * @return 方法列表
     */
    List<Method> scanMethod(Class<?> target, Class<? extends Annotation> ...annotationTypes);

    /**
     * 方法扫描器
     *
     * @param targetList 扫描目标
     * @param annotationType 指定注解
     * @return 方法列表
     */
    List<Method> scanMethod(List<Class<?>> targetList, Class<? extends Annotation>  ...annotationType);
}
