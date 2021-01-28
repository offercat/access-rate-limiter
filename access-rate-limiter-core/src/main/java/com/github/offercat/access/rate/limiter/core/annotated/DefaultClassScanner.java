package com.github.offercat.access.rate.limiter.core.annotated;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * 默认类扫描器
 *
 * @author 徐通 xutong34
 * @since 2020年08月31日 17:09:38
 */
public class DefaultClassScanner implements ClassScanner {

    public static final DefaultClassScanner DEFAULT_CLASS_PATH_CLASS_SCANNER = new DefaultClassScanner();

    @Override
    public List<Class<?>> scanClass(String path) {
        Set<Class<?>> classes = new LinkedHashSet<>();

        String pkgDirName = path.replace('.', '/');
        try {
            Enumeration<URL> urls = this.getClass().getClassLoader().getResources(pkgDirName);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassesByFile(path, filePath, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    findClassesByJar(path, jar, classes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(classes);
    }

    @Override
    public List<Class<?>> scanClass(String path, Class<? extends Annotation> annotationType) {
        List<Class<?>> classList = this.scanClass(path);
        return classList.stream().filter(clz -> clz.isAnnotationPresent(annotationType)).collect(Collectors.toList());
    }

    /**
     * 扫描包路径下的所有class文件
     *
     * @param pkgName 包名
     * @param pkgPath 包对应的绝对地址
     * @param classes 保存包路径下class的集合
     */
    private static void findClassesByFile(String pkgName, String pkgPath, Set<Class<?>> classes) {
        File dir = new File(pkgPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }


        // 过滤获取目录，or class文件
        File[] dirfiles = dir.listFiles(pathname -> pathname.isDirectory() || pathname.getName().endsWith("class"));


        if (dirfiles == null || dirfiles.length == 0) {
            return;
        }


        String className;
        Class clz;
        for (File f : dirfiles) {
            if (f.isDirectory()) {
                findClassesByFile(pkgName + "." + f.getName(),
                        pkgPath + "/" + f.getName(),
                        classes);
                continue;
            }


            // 获取类名，干掉 ".class" 后缀
            className = f.getName();
            className = className.substring(0, className.length() - 6);

            // 加载类
            clz = loadClass(pkgName + "." + className);
            if (clz != null) {
                classes.add(clz);
            }
        }
    }

    /**
     * 扫描包路径下的所有class文件
     *
     * @param pkgName 包名
     * @param jar     jar文件
     * @param classes 保存包路径下class的集合
     */
    private static void findClassesByJar(String pkgName, JarFile jar, Set<Class<?>> classes) {
        String pkgDir = pkgName.replace(".", "/");


        Enumeration<JarEntry> entry = jar.entries();

        JarEntry jarEntry;
        String name, className;
        Class<?> claze;
        while (entry.hasMoreElements()) {
            jarEntry = entry.nextElement();

            name = jarEntry.getName();
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }


            if (jarEntry.isDirectory() || !name.startsWith(pkgDir) || !name.endsWith(".class")) {
                // 非指定包路径， 非class文件
                continue;
            }


            // 去掉后面的".class", 将路径转为package格式
            className = name.substring(0, name.length() - 6);
            claze = loadClass(className.replace("/", "."));
            if (claze != null) {
                classes.add(claze);
            }
        }
    }

    private static Class<?> loadClass(String fullClzName) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(fullClzName);
        } catch (ClassNotFoundException e) {
//            log.error("load class error! clz: {}, e:{}", fullClzName, e);
        }
        return null;
    }
}