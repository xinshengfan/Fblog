package com.fblog.core.utils;

import java.io.File;
import java.util.Arrays;

/**
 * Created by fansion on 19/01/2017.
 * 文件工具类
 */
public class FileUtils {
    /**
     * 判定指定格式是否为图片
     */
    public static boolean isImageExt(String ext) {
        return ext != null && Arrays.asList("jpg", "jpeg", "png", "bmp", "gif").contains(ext.toLowerCase());
    }

    /**
     * 获取文件名的后缀，不含.
     */
    public static String getFileExt(String fileName) {
        int point = fileName.lastIndexOf(".");
        return fileName.substring(point + 1);
    }

    /**
     * 获取整个文件名，包括后缀
     */
    public static String getFileNameWithExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("/") + 1);
    }

    /**
     * 获取文件名，不包含后缀
     */
    public static String getFileName(String fileName) {
        fileName = getFileNameWithExt(fileName);
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 生成惟一存储文件
     */
    public static File determainFile(File parent, String fileName) {
        String name = getFileName(fileName);
        String ext = getFileExt(fileName);
        File temp = new File(parent, fileName);
        //生成不重复和文件名
        for (int i = 0; temp.exists(); i++) {
            temp = new File(parent, name + i + "." + ext);
        }
        return temp;
    }
}
