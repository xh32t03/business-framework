package com.easyway.business.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.easyway.business.framework.common.exception.BaseException;
import com.easyway.business.framework.constant.Constant;

@SuppressWarnings("all")
public final class ReflectUtil {

    /**
     * 字段缓存
     */
    private static final SimpleCache<Class<?>, Field[]>  FIELDS_CACHE  = new SimpleCache<>();
    /**
     * 方法缓存
     */
    private static final SimpleCache<Class<?>, Method[]> METHODS_CACHE = new SimpleCache<>();

    /**
     * 获取类实例的属性值
     *
     * @param clazz 类名
     * @return 类名.属性名=属性类型
     */
    public static Map<String, Field> getClassFields(Class clazz, boolean includeParentClass) {
        Map<String, Field> map = new HashMap<String, Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String key = field.getName();
            if (!Constant.FILTER_LIST.contains(key)) {
                map.put(key, field);
            }
        }
        if (includeParentClass) {
            getParentClassFields(map, clazz.getSuperclass());
        }
        return map;
    }

    /**
     * 获取类实例的父类的属性值
     *
     * @param map 类实例的属性值Map
     * @param clazz 类名
     * @return 类名.属性名=属性类型
     */
    private static Map<String, Field> getParentClassFields(Map<String, Field> map, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String key = field.getName();
            if (!Constant.FILTER_LIST.contains(key)) {
                map.put(key, field);
            }
        }
        if (clazz.getSuperclass() == null) {
            return map;
        }
        getParentClassFields(map, clazz.getSuperclass());
        return map;
    }

    /**
     * 获取类实例的方法
     *
     * @param clazz 类名
     * @return List
     */
    public static List<Method> getMothds(Class clazz) {
        List<Method> list = new ArrayList<Method>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            list.add(method);
        }
        getParentClassMothds(list, clazz.getSuperclass());
        return list;
    }

    /**
     * 获取类实例的父类的方法
     *
     * @param list 类实例的方法List
     * @param clazz 类名
     * @return List
     */
    public static List<Method> getParentClassMothds(List<Method> list, Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            list.add(method);
        }
        if (clazz.getSuperclass() == Object.class || clazz.getSuperclass() == null) {
            return list;
        }
        getParentClassMothds(list, clazz.getSuperclass());
        return list;
    }

    /**
     * 获得一个类中所有字段列表，包括其父类中的字段<br>
     * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后
     *
     * @param beanClass 类
     * @return 字段列表
     */
    public static Field[] getFields(Class<?> beanClass) {
        Field[] allFields = FIELDS_CACHE.get(beanClass);
        if (null != allFields) {
            return allFields;
        }

        allFields = getFields(beanClass, true);
        return FIELDS_CACHE.put(beanClass, allFields);
    }

    /**
     * 获得一个类中所有字段列表，直接反射获取，无缓存<br>
     * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后
     *
     * @param beanClass 类
     * @param withSuperClassFields 是否包括父类的字段列表
     * @return 字段列表
     * @throws SecurityException 安全检查异常
     */
    public static Field[] getFields(Class<?> beanClass, boolean withSuperClassFields) {
        Assert.assertNotNull(beanClass);

        Field[] allFields = null;
        Class<?> searchType = beanClass;
        Field[] declaredFields;
        while (searchType != null) {
            declaredFields = searchType.getDeclaredFields();
            if (null == allFields) {
                allFields = declaredFields;
            } else {
                allFields = ArrayUtil.append(allFields, declaredFields);
            }
            searchType = withSuperClassFields ? searchType.getSuperclass() : null;
        }

        return allFields;
    }

    /**
     * 获得一个类中所有方法列表，包括其父类中的方法
     *
     * @param beanClass 类
     * @return 方法列表
     */
    public static Method[] getMethods(Class clazz) {
        Method[] allMethods = METHODS_CACHE.get(clazz);
        if (null != allMethods) {
            return allMethods;
        }

        allMethods = getMethods(clazz, true);
        return METHODS_CACHE.put(clazz, allMethods);
    }

    /**
     * 获得一个类中所有方法列表，直接反射获取，无缓存
     *
     * @param beanClass 类
     * @param withSuperClassMethods 是否包括父类的方法列表
     * @return 方法列表
     */
    public static Method[] getMethods(Class<?> beanClass, boolean withSuperClassMethods) {
        Assert.assertNotNull(beanClass);

        Method[] allMethods = null;
        Class<?> searchType = beanClass;
        Method[] declaredMethods;
        while (searchType != null) {
            declaredMethods = searchType.getDeclaredMethods();
            if (null == allMethods) {
                allMethods = declaredMethods;
            } else {
                allMethods = ArrayUtil.append(allMethods, declaredMethods);
            }
            searchType = withSuperClassMethods ? searchType.getSuperclass() : null;
        }

        return allMethods;
    }

    /**
     * 实例化对象
     *
     * @param <T>   对象类型
     * @param clazz 类名
     * @return 对象
     */
    public static <T> T newInstance(String clazz) throws BaseException {
        try {
            return (T) Class.forName(clazz).newInstance();
        } catch (Exception e) {
            throw new BaseException("Instance class [" + clazz + "] error!", e);
        }
    }
    
    public static String toString(Object obj) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(obj.getClass().getSimpleName()).append("{");
        Field[] list = getFields(obj.getClass());
        for (Field field : list) {
            try {
                Object value = field.get(obj);
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (stringBuilder.length() > obj.getClass().getSimpleName().length() + 1) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(field.getName()).append("=").append(value);
            } catch (Exception e) {
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
