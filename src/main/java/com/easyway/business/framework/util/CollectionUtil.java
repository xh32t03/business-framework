package com.easyway.business.framework.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class CollectionUtil {
    public static boolean checkIsEmpty(Collection collection) {
        return CollectionUtil.isEmpty(collection);
    }

    public static boolean checkIsNotEmpty(Collection collection) {
        return CollectionUtil.isNotEmpty(collection);
    }

    public static boolean checkIsEmpty(Object[] objArray) {
        return CollectionUtil.isEmpty(objArray);
    }

    public static boolean checkIsNotEmpty(Object[] objArray) {
        return CollectionUtil.isNotEmpty(objArray);
    }

    public static boolean isEmpty(Collection collection) {
        return (collection == null) || (collection.isEmpty());
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(Object[] objArray) {
        return (objArray == null) || (objArray.length == 0);
    }

    public static boolean isNotEmpty(Object[] objArray) {
        return !isEmpty(objArray);
    }

    public static void addNotEmptyStr(String str, List<String> list) {
        if (StringUtil.isEmpty(str)) {
            return;
        }
        list.add(str);
    }

    public static void addNotEmptyVal(Object obj, List<Object> list) {
        if (obj == null) {
            return;
        }
        list.add(obj);
    }

    /**
     * 判断list列表字符串
     * 
     * @param str
     * @param list
     * @return
     */
    public static boolean strInList(String str, List<String> list) {
        if (isEmpty(list)) {
            return false;
        }
        if (str == null) {
            return false;
        }
        for (String listStr : list) {
            if (str.equals(listStr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 移除空字符串
     * 
     * @param collections
     */
    public static void removeEmptyString(Collection<String> collections) {
        List<String> removeStringList = new ArrayList<String>();
        for (String string : collections) {
            if (StringUtil.isEmpty(string)) {
                removeStringList.add(string);
            }
        }
        collections.removeAll(removeStringList);
    }

    /**
     * 取最小的对象
     * 
     * @param collections
     */
    public static <T extends Comparable<? super T>> List<T> min(Collection<T> collections) {
        T min = null;
        for (T e : collections) {
            if (min == null) {
                min = e;
            }
            if (e.compareTo(min) < 0) {
                min = e;
            }
        }
        return equal(collections, min);
    }

    /**
     * 取最大的对象
     * 
     * @param collections
     */
    public static <T extends Comparable<? super T>> List<T> max(Collection<T> collections) {
        T max = null;
        for (T e : collections) {
            if (max == null) {
                max = e;
            }
            if (e.compareTo(max) > 0) {
                max = e;
            }
        }
        return equal(collections, max);
    }

    /**
     * 取相等值
     * 
     * @param <T>
     * @param collections
     * @param object
     * @return
     */
    public static <T extends Comparable<? super T>> List<T> equal(Collection<T> collections, T object) {
        List<T> returnList = new ArrayList<T>();
        for (T e : collections) {
            if (e.compareTo(object) == 0) {
                returnList.add(e);
            }
        }
        return returnList;
    }

    /**
     * coll1是否全包含coll2的值
     * 
     * @param coll1
     * @param coll2
     * @return
     */
    public static <T> boolean containsAll(final Collection<T> coll1, final Collection<T> coll2) {
        // size为0
        if (coll1.size() <= 0 || coll2.size() <= 0)
            return false;

        for (T t : coll2) {
            // 只要有一个不包含，则返回false
            if (!coll1.contains(t)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将list反转排序
     * 
     * @param list
     */
    public static void reverse(List<?> list) {
        if (list == null) {
            return;
        }
        Collections.reverse(list);
    }

    /**
     * 把数组转换为一个用指定分隔符的字符串
     * 
     * @param list
     * @param split
     * @return
     */
    public static String convertArrayToSplitStr(List<String> list, String split) {
        StringBuffer result = new StringBuffer();
        if (CollectionUtil.isNotEmpty(list)) {
            for (String str : list) {
                result.append(str).append(split);
            }
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    /**
     * 把数组转换为一个用逗号分隔的字符串,以便于用in+String查询
     */
    public static String arrayToString(String[] ig) {
        String str = "";
        if (ig != null && ig.length > 0) {
            for (int i = 0; i < ig.length; i++) {
                str += ig[i] + ",";
            }
        }
        str = str.substring(0, str.length() - 1);
        return str;
    }

    /**
     * 把list转换为一个用逗号分隔的字符串
     */
    public static String listToString(List list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {// 当循环到最后一个的时候 就不添加逗号,
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    /**
     * Java8 数组转为List
     * 
     * @param arrays
     * @return
     */
    public static List<String> arrayToList(String[] arrays) {
        return ListUtil.arrayToList(arrays);
    }

    /**
     * Java8 List转为数组
     * 
     * @param list
     * @return
     */
    public static String[] listToArray(List<String> list) {
        return ListUtil.listToArray(list);
    }
}
