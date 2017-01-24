package com.fblog.core.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class ServletUtils {
    /**
     * 是否是文件上传请求
     */
    public static boolean isMultipartContent(HttpServletRequest request) {
        if (!"post".equals(request.getMethod().toLowerCase())) {
            return false;
        }
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }

    /**
     * 是否为ajax请求
     */
    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    /**
     * 获取完整的域名
     */
    public static String getDomain(HttpServletRequest request) {
        String result = request.getScheme() + "://" + request.getServerName();
        if (request.getServerPort() != 80) {
            result += ":" + request.getServerPort();
        }
        result += request.getContextPath();
        return result;
    }

    /**
     * 转发
     */
    public static void sendRedirect(HttpServletResponse response, String url) {
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 响应文本
     */
    public static void sendText(HttpServletResponse response, String text) {
        Writer writer = null;
        try {
            writer = response.getWriter();
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取请求的ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        return IpUtils.getIpAddress(request);
    }
}
