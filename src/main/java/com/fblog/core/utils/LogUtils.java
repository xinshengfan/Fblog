package com.fblog.core.utils;

import org.apache.log4j.Logger;

public class LogUtils {

    private static Logger logger = Logger.getLogger("");

    public static void i(String tag,String msg){
        logger.info(tag+":"+msg);
    }

    public static void d(String tag,String msg){
        logger.debug(tag+":"+msg);
    }

    public static void e(String tag,String msg){
        logger.error(tag+":"+msg);
    }

    public static void e(String msg,Throwable t){
        logger.error(msg,t);
    }

}
