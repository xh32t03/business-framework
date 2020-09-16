package com.easyway.business.framework.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
}
