package com.easyway.business.framework.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public final class ObjectUtil {

    public static final String EMPTY_STRING = "";
    public static final String ARRAY_START  = "{";
    public static final String ARRAY_END    = "}";
    public static final String EMPTY_ARRAY  = ARRAY_START + ARRAY_END;

    public static boolean checkIsEmpty(Object obj) {
        return ObjectUtil.isEmpty(obj);
    }
    
    public static boolean checkIsNotEmpty(Object obj) {
        return ObjectUtil.isNotEmpty(obj);
    }
    
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return !isNotEmpty(obj);
    }
    
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isNotEmpty(Object[] array) {
        return (array != null && array.length != 0);
    }
    
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static <A, O extends A> A[] addObjectToArray(A[] array, O obj) {
        Class<?> compType = Object.class;
        if (array != null) {
            compType = array.getClass().getComponentType();
        } else if (obj != null) {
            compType = obj.getClass();
        }
        int newArrLength = (array != null ? array.length + 1 : 1);
        @SuppressWarnings("unchecked")
        A[] newArr = (A[]) Array.newInstance(compType, newArrLength);
        if (array != null) {
            System.arraycopy(array, 0, newArr, 0, array.length);
        }
        newArr[newArr.length - 1] = obj;
        return newArr;
    }
    
}
