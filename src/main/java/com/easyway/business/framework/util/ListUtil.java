package com.easyway.business.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("all")
public final class ListUtil {

    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     * 
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n; // (先计算出余数)
        int number = source.size() / n; // 然后是商
        int offset = 0;// 偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    public static <T> List<T> deepCopy(List<T> src) throws Exception {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);
        
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }
    
    /**
     * 调换集合中两个指定位置的元素, 若两个元素位置中间还有其他元素，需要实现中间元素的前移或后移的操作。
     * 
     * @param list 集合
     * @param oldPosition 需要调换的元素
     * @param newPosition 被调换的元素
     * @param <T>
     */
    public static <T> void swap(List<T> list, int oldPosition, int newPosition) {
        if (null == list) {
            throw new IllegalStateException("The list can not be empty..");
        }

        // 向前移动，前面的元素需要向后移动
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        }

        // 向后移动，后面的元素需要向前移动
        if (oldPosition > newPosition) {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
    }
    
    /**
     * Java8 数组转为List
     * 
     * @param arrays
     * @return
     */
    public static List<String> arrayToList(String[] arrays) {
        List<String> result = Stream.of(arrays).filter(Objects::nonNull).collect(Collectors.toList());
        return result;
    }

    /**
     * Java8 List转为数组
     * 
     * @param list
     * @return
     */
    public static String[] listToArray(List<String> list) {
        if (list != null && !list.isEmpty()) {
            String[] result = list.stream().toArray(String[]::new);
            Arrays.stream(result).forEach(str -> System.err.println(str));
            return result;
        }
        return null;
    }

    /**
     * 把array转换为一个用逗号分隔的字符串
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
     * 将逗号分隔的字符串转list
     * 
     * @param str
     * @return
     */
    public static List<String> strToList(String str) {
        if (StringUtil.checkIsNotEmpty(str)) {
            return Arrays.asList(str.split(",")).stream().map(s -> String.valueOf(s.trim()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 将逗号分隔的字符串转array
     * 
     * @param str
     * @return
     */
    public static String[] strToArray(String str) {
        if (StringUtil.checkIsNotEmpty(str)) {
            return ListUtil.listToArray(strToList(str));
        }
        return null;
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
        if (list != null && !list.isEmpty()) {
            for (String str : list) {
                result.append(str).append(split);
            }
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }
    
    /**
     * 拼接IN条件值
     * 
     * @param str
     * @return
     */
    public static String getInCond(String str) {
        String[] strArr = str.split(",");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strArr.length; i++) {
            if (i == strArr.length - 1) {
                sb.append("'" + strArr[i] + "'");
            } else {
                sb.append("'" + strArr[i] + "'" + ",");
            }
        }
        return sb.toString();
    }
    
    /**
     * 拼接IN条件值
     * 
     * @param str
     * @return
     */
    public static String getInCond(List<String> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && !list.isEmpty()) {
            for (String value : list) {
                if (value != null) {
                    sb.append("'").append(value).append("',");
                }
            }
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    /**
     * 拼接IN条件值
     * 
     * @param str
     * @return
     */
    public static String getInCond(String[] values) {
        StringBuffer sb = new StringBuffer();
        if (values != null && values.length > 0) {
            for (String value : values) {
                if (value != null) {
                    sb.append("'").append(value).append("',");
                }
            }
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }
}
