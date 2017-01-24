package com.fblog.core.utils;

import java.util.Collection;

/**
 * 字符串工具
 */
public class StringUtils {

    public static boolean isEmpty(String content) {
        return content == null || content.trim().length() == 0;
    }

    public static String emptyDefault(String content, String defaultValue) {
        return isEmpty(content) ? defaultValue : content;
    }

    public static String join(Collection<String> collect, String delimiter) {
        StringBuilder result = new StringBuilder();
        for (String temp : collect) {
            result.append(temp).append(delimiter);
        }

        if (!collect.isEmpty())
            result.delete(result.length() - delimiter.length(), result.length());

        return result.toString();
    }

}
