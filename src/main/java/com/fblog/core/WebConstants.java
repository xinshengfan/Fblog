package com.fblog.core;

/**
 * web请求时的一些常量
 */
public class WebConstants {
    private WebConstants() {
    }

    /**
     * 站点标题前缀
     */
    public static final String PRE_TITLE_KEY = "ptitle";
    /**
     * 应用路径在servlet初始化时赋值,用于存放图片等资源根路径
     */
    public static String APPLICATION_PATH;
    /**
     * 表态路径前缀
     */
    public static final String PREFIX = "/WEB-INF/jsp";
    /*域名*/
    private static String DOMAIN;

    public static final String FILE_PATH = "/fBlog_file";

    public static String getDomain() {
        return DOMAIN;
    }

    public static void setDomain(String domain) {
        DOMAIN = domain;
    }

}
