package com.fblog.core.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Created by fansion on 19/01/2017.
 * 集合工具
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection){
        return collection==null || collection.isEmpty();
    }
    public static boolean isEmpty(Map<?,?> map){
        return map==null || map.isEmpty();
    }
    public static <T> boolean isEmpty(T[] array){
        return array==null || array.length==0;
    }
    public static <K,V> K getKeyByValue(Map<K,V> map,V value){
        K key = null;
        for (Map.Entry<K,V> entry:map.entrySet()){
            if (Objects.equals(entry.getValue(),value)){
                key = entry.getKey();
                break;
            }
        }
        return key;
    }
}
