package com.github.offercat.access.rate.limiter.core.annotated;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 15:41:53
 */
public interface ClassScanner {

    /**
     * 扫描路径
     *
     * @param path path
     * @return 所有类对象
     */
    List<Class<?>> scanClass(String path);

    /**
     * 扫描路径
     *
     * @param path path
     * @param annotationType annotationType
     * @return 所有类对象
     */
    List<Class<?>> scanClass(String path, Class<? extends Annotation> annotationType);
}
