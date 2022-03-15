package com.rainmonth.utils;

import java.lang.reflect.Field;

/**
 * 反射工具类
 *
 * @author RandyZhang
 * @date 2021/9/3 2:15 下午
 */
public class ReflectionUtils {

    /**
     * 通过动态代理模式创建对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T createNullProxy(Class<T> clazz) {
        // todo
        throw new RuntimeException("to be impl");
    }

//    fun <A : Annotation> defaultsFor(annotation: Class<A>): A? {
//        return annotation.cast(
//                Proxy.newProxyInstance(annotation.classLoader, arrayOf<Class<*>>(annotation)
//        ) { proxy, method, args -> method.defaultValue })
//    }

    /**
     * 获取对象的某个字段
     *
     * @param o         待反射对象
     * @param fieldName 获取的字段名
     * @param <R>       字段类型
     * @return 字段的值
     */
    public <R> R getField(Object o, String fieldName) {
        // todo
        throw new RuntimeException("to be impl");
    }

    /**
     * 设置对象的某个字段
     *
     * @param o             待反射对象
     * @param fieldName     设置的字段
     * @param fieldNewValue 要设置的值
     */
    public void setField(Object o, String fieldName, Object fieldNewValue) {
        // todo
        throw new RuntimeException("to be impl");
    }

    public <T> void setField(Class<T> clazz, String fieldName, Object fieldNewValue) {
        // todo
        throw new RuntimeException("to be impl");
    }

    public <R> R getStaticField(Field field, String fieldName) {
        // todo
        try {
            return (R) field.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <R> R getStaticField(Field field) {
        // todo
        throw new RuntimeException("to be impl");
    }

    public void setStaticField(Field field, Object fieldNewValue) {
        // todo
        throw new RuntimeException("to bo impl");
    }

    public void setStaticField(Class<?> clazz, String fieldName, Object fieldNewValue) {
        try {
            setStaticField(clazz.getDeclaredField(fieldName), fieldNewValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <R> R callConstructor(Class<R> clazz) {
        // todo
        throw new RuntimeException("to bo impl");
    }

    public <R> R callInstanceMethod(Object instance, String methodName) {
        // todo
        throw new RuntimeException("to be impl");
    }

    public Class<?> loadClass(ClassLoader classLoader, String fullyQualifiedName) {
        // todo
        throw new RuntimeException("to bo impl");
    }

    public <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
