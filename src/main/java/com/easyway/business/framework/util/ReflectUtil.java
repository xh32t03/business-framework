package com.easyway.business.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.easyway.business.framework.constant.Constant;

/**
 * 反射工具类
 * <p>提供类字段和方法的反射操作，支持缓存以提高性能</p>
 */
public final class ReflectUtil {
    
    /**
     * 字段缓存（按类缓存字段数组）
     */
    private static final SimpleCache<Class<?>, Field[]> FIELDS_CACHE = new SimpleCache<>();
    
    /**
     * 方法缓存（按类缓存方法数组）
     */
    private static final SimpleCache<Class<?>, Method[]> METHODS_CACHE = new SimpleCache<>();
    
    /**
     * 字段映射缓存（按类缓存字段Map，包含是否包含父类的不同配置）
     */
    private static final SimpleCache<String, Map<String, Field>> FIELD_MAP_CACHE = new SimpleCache<>();
    
    private ReflectUtil() {
        throw new UnsupportedOperationException("工具类不允许实例化");
    }
    
    // 缓存键生成器
    private static String generateCacheKey(Class<?> clazz, boolean includeParent) {
        return clazz.getName() + "#" + includeParent;
    }

    /**
     * 获取类的所有字段（直接反射获取，无缓存）
     * 
     * @param clazz
     * @return
     */
    public static Map<String, Field> getClassFields(Class<?> clazz) {
        if (clazz == null) {
            return new LinkedHashMap<>();
        }
        
        Map<String, Field> fieldMap = new LinkedHashMap<>();
        collectFields(fieldMap, clazz, true);
        
        // 过滤掉黑名单中的字段
        fieldMap.entrySet().removeIf(entry -> 
            Constant.FILTER_LIST != null && Constant.FILTER_LIST.contains(entry.getKey()));
        
        return fieldMap;
    }
    
    /**
     * 获取类的所有字段（直接反射获取，缓存）
     *
     * @param clazz               目标类
     * @param includeParentClass  是否包含父类字段
     * @return 字段名到字段对象的映射（保留字段声明顺序：子类在前，父类在后）
     */
    public static Map<String, Field> getClassFields(Class<?> clazz, boolean includeParentClass) {
        if (clazz == null) {
            return new LinkedHashMap<>();
        }
        
        String cacheKey = generateCacheKey(clazz, includeParentClass);
        Map<String, Field> cachedMap = FIELD_MAP_CACHE.get(cacheKey);
        if (cachedMap != null) {
            return new LinkedHashMap<>(cachedMap); // 返回副本保证线程安全
        }
        
        Map<String, Field> fieldMap = new LinkedHashMap<>();
        collectFields(fieldMap, clazz, includeParentClass);
        
        // 过滤掉黑名单中的字段
        fieldMap.entrySet().removeIf(entry -> 
            Constant.FILTER_LIST != null && Constant.FILTER_LIST.contains(entry.getKey()));
        
        FIELD_MAP_CACHE.put(cacheKey, new LinkedHashMap<>(fieldMap));
        return fieldMap;
    }
    
    /**
     * 收集类的所有字段
     */
    private static void collectFields(Map<String, Field> fieldMap, Class<?> clazz, boolean includeParent) {
        Class<?> currentClass = clazz;
        
        while (currentClass != null && currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            
            // 将当前类的字段放入映射（子类字段会覆盖父类同名字段）
            for (Field field : fields) {
                fieldMap.put(field.getName(), field);
            }
            
            if (!includeParent) {
                break;
            }
            
            currentClass = currentClass.getSuperclass();
        }
    }
    
    public static Field[] getCacheFields(Class<?> clazz) {
        if (clazz == null) {
            return new Field[0];
        }
        
        Field[] cachedFields = FIELDS_CACHE.get(clazz);
        if (cachedFields != null) {
            return cachedFields.clone(); // 返回副本保证线程安全
        }
        
        Field[] fields = getFields(clazz, true);
        FIELDS_CACHE.put(clazz, fields.clone());
        return fields;
    }
    
    /**
     * 获得一个类中所有字段列表，包括其父类中的字段
     * <p>如果子类与父类中存在同名字段，则只保留子类字段</p>
     *
     * @param clazz 类
     * @return 字段数组
     */
    public static Field[] getFields(Class<?> clazz) {
        if (clazz == null) {
            return new Field[0];
        }
        
        Field[] fields = getFields(clazz, true);
        return fields;
    }

    /**
     * 获得一个类中所有字段列表（直接反射获取，无缓存）
     *
     * @param clazz                目标类
     * @param withSuperClassFields 是否包括父类的字段
     * @return 字段数组
     */
    public static Field[] getFields(Class<?> beanClass, boolean withSuperClassFields) {
        if (beanClass == null) {
            return new Field[0];
        }
        
        List<Field> fieldList = new ArrayList<>();
        Class<?> searchType = beanClass;
        
        while (searchType != null && searchType != Object.class) {
            Field[] declaredFields = searchType.getDeclaredFields();
            
            // 过滤掉合成字段
            for (Field field : declaredFields) {
                if (!field.isSynthetic()) {
                    fieldList.add(field);
                }
            }
            
            if (!withSuperClassFields) {
                break;
            }
            
            searchType = searchType.getSuperclass();
        }
        
        return fieldList.toArray(new Field[0]);
    }

    public static Method[] getCacheMethods(Class<?> clazz) {
        if (clazz == null) {
            return new Method[0];
        }
        
        Method[] cachedMethods = METHODS_CACHE.get(clazz);
        if (cachedMethods != null) {
            return cachedMethods.clone(); // 返回副本保证线程安全
        }
        
        Method[] methods = getMethods(clazz, true);
        METHODS_CACHE.put(clazz, methods.clone());
        return methods;
    }
    
    /**
     * 获得一个类中所有方法列表，包括其父类中的方法
     *
     * @param clazz 类
     * @return 方法数组
     */
    public static Method[] getMethods(Class<?> clazz) {
        if (clazz == null) {
            return new Method[0];
        }
        
        Method[] methods = getMethods(clazz, true);
        return methods;
    }

    /**
     * 获得一个类中所有方法列表（直接反射获取，无缓存）
     *
     * @param beanClass              目标类
     * @param withSuperClassMethods  是否包括父类的方法
     * @return 方法数组
     */
    public static Method[] getMethods(Class<?> clazz, boolean withSuperClassMethods) {
        if (clazz == null) {
            return new Method[0];
        }
        
        List<Method> methodList = new ArrayList<>();
        Class<?> searchType = clazz;
        
        while (searchType != null && searchType != Object.class) {
            Method[] declaredMethods = searchType.getDeclaredMethods();
            
            for (Method method : declaredMethods) {
                // 过滤掉合成方法和桥接方法
                if (!method.isSynthetic() && !method.isBridge()) {
                    methodList.add(method);
                }
            }
            
            if (!withSuperClassMethods) {
                break;
            }
            
            searchType = searchType.getSuperclass();
        }
        
        return methodList.toArray(new Method[0]);
    }

    /**
     * 实例化对象
     *
     * @param <T>   对象类型
     * @param clazz 类的全限定名
     * @return 对象实例
     * @throws ReflectException 反射异常
     */
    public static <T> T newInstance(String clazz) throws ReflectException {
        if (clazz == null || clazz.trim().isEmpty()) {
            throw new ReflectException("类名不能为空");
        }
        
        try {
            Class<?> cls = Class.forName(clazz.trim());
            return newInstance(cls);
        } catch (ClassNotFoundException e) {
            throw new ReflectException("未找到类: " + clazz, e);
        } catch (Exception e) {
            throw new ReflectException("实例化类 [" + clazz + "] 失败", e);
        }
    }
    
    /**
     * 实例化对象
     *
     * @param <T>   对象类型
     * @param clazz 类对象
     * @return 对象实例
     * @throws ReflectException 反射异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) throws ReflectException {
        if (clazz == null) {
            throw new ReflectException("类对象不能为空");
        }
        
        try {
            return (T) clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new ReflectException("类 [" + clazz.getName() + "] 没有无参构造方法", e);
        } catch (Exception e) {
            throw new ReflectException("实例化类 [" + clazz.getName() + "] 失败", e);
        }
    }
    
    /**
     * 获取对象的字符串表示
     *
     * @param obj 对象
     * @return 字符串表示
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(obj.getClass().getSimpleName()).append('{');
        
        Field[] fields = getFields(obj.getClass());
        boolean firstField = true;
        
        for (Field field : fields) {
            // 跳过静态字段
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                
                if (!firstField) {
                    stringBuilder.append(", ");
                }
                
                stringBuilder.append(field.getName()).append('=');
                appendValue(stringBuilder, value);
                
                firstField = false;
            } catch (IllegalAccessException e) {
                // 忽略无法访问的字段
            } catch (Exception e) {
                // 其他异常也忽略，继续处理其他字段
            }
        }
        
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
    
    /**
     * 安全地追加值到StringBuilder
     */
    private static void appendValue(StringBuilder builder, Object value) {
        if (value == null) {
            builder.append("null");
        } else if (value instanceof CharSequence) {
            builder.append('"').append(value).append('"');
        } else if (value instanceof Character) {
            builder.append('\'').append(value).append('\'');
        } else {
            builder.append(value);
        }
    }
    
    /**
     * 获取字段值
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @return 字段值
     * @throws ReflectException 反射异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object obj, String fieldName) throws ReflectException {
        if (obj == null) {
            throw new ReflectException("目标对象不能为空");
        }
        
        if (fieldName == null || fieldName.trim().isEmpty()) {
            throw new ReflectException("字段名不能为空");
        }
        
        try {
            Field field = obj.getClass().getDeclaredField(fieldName.trim());
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (NoSuchFieldException e) {
            throw new ReflectException("字段 [" + fieldName + "] 不存在于类 [" + obj.getClass().getName() + "]", e);
        } catch (Exception e) {
            throw new ReflectException("获取字段 [" + fieldName + "] 的值失败", e);
        }
    }
    
    /**
     * 设置字段值
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @param value     字段值
     * @throws ReflectException 反射异常
     */
    public static void setFieldValue(Object obj, String fieldName, Object value) throws ReflectException {
        if (obj == null) {
            throw new ReflectException("目标对象不能为空");
        }
        
        if (fieldName == null || fieldName.trim().isEmpty()) {
            throw new ReflectException("字段名不能为空");
        }
        
        try {
            Field field = obj.getClass().getDeclaredField(fieldName.trim());
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException e) {
            throw new ReflectException("字段 [" + fieldName + "] 不存在于类 [" + obj.getClass().getName() + "]", e);
        } catch (Exception e) {
            throw new ReflectException("设置字段 [" + fieldName + "] 的值失败", e);
        }
    }
    
    /**
     * 清空缓存
     */
    public static void clearCache() {
        FIELDS_CACHE.clear();
        METHODS_CACHE.clear();
        FIELD_MAP_CACHE.clear();
    }
    
    /**
     * 反射异常
     */
    public static class ReflectException extends RuntimeException {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public ReflectException(String message) {
            super(message);
        }
        
        public ReflectException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
