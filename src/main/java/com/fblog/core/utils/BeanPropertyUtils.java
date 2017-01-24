package com.fblog.core.utils;

import java.lang.reflect.Field;

/**
 * Created by fansion on 18/01/2017.
 * 读取bean下的属性property
 */
public class BeanPropertyUtils {
    private static final String TAG = "BeanPropertyUtils";

    public static Object getFieldValue(Object obj, String fieldName) {
        Object value = null;
        try {
            Field field = getFieldByFieldName(obj, fieldName);
            if (field != null) {
                boolean access = field.isAccessible();
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(access);
            }
        } catch (IllegalAccessException e) {
            LogUtils.d(TAG,"can't get field " + fieldName + " value in class " + obj);
        }
        return value;
    }

    private static Field getFieldByFieldName(Object obj, String fieldName) {
        if (obj==null || fieldName==null)
        return null;
        for (Class<?> superClass = obj.getClass();superClass!=Object.class;superClass=superClass.getSuperclass()){
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                LogUtils.d(TAG,"can't found field " + fieldName + " in class " + obj.getClass());
            }
        }
        return null;
    }

    public static boolean setFieldValue(Object obj, String fieldName, Object value){
        boolean isSuccess = true;
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            boolean access = field.isAccessible();
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(access);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            isSuccess = false;
            LogUtils.d(TAG,"can't set field value:"+e.getMessage());
        }
        return isSuccess;
    }
}
