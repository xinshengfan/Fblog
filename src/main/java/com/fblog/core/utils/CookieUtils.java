package com.fblog.core.utils;

import com.fblog.core.Constants;
import com.fblog.core.security.Base64Codec;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Cookie数据处理
 */
public class CookieUtils {
    private Method setHttpOnlyMethod;
    private static final String PATH = "/";

    private HttpServletRequest request;
    private HttpServletResponse response;
    //域名
    private String domain;

    public CookieUtils(HttpServletRequest request, HttpServletResponse response) {
        this(request, response, null);
    }

    public CookieUtils(HttpServletRequest request, HttpServletResponse response, String domain) {
        this.request = request;
        this.response = response;
        this.domain = domain;
    }

    /**
     * 获取Cooki值，默认使用base64编码
     *
     * @param name .
     * @return value .
     */
    public String getCookie(String name) {
        return getCookie(name, true);
    }

    public String getCookie(String name, boolean decode) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                String value = cookie.getValue();
                return decode ? decode(value) : value;
            }
        }
        return null;
    }


    public void setCookie(String name, String value, boolean httpOnly){
        setCookie(name, value, PATH, httpOnly);
    }

    public void setCookie(String name, String value, String path, boolean httpOnly){
        setCookie(name, value, path, httpOnly, -1);
    }

    public void setCookie(String name, String value, boolean httpOnly, int expiry){
        setCookie(name, value, PATH, httpOnly, expiry, true);
    }

    public void setCookie(String name, String value, String path, boolean httpOnly, int expiry){
        setCookie(name, value, path, httpOnly, expiry, true);
    }

    /**
     * 设置cookie
     *
     * @param name     .
     * @param value    .
     * @param path     .
     * @param httpOnly .
     * @param expiry   存活时间
     * @param encode   编码
     */
    public void setCookie(String name, String value, String path, boolean httpOnly, int expiry, boolean encode) {
        Cookie cookie  = new Cookie(name,encode?encode(value):value);
        cookie.setPath(path);
        cookie.setMaxAge(expiry);
           /* 在javaee6以上,tomcat7以上支持Cookie.setHttpOnly方法,这里向下兼容 */
           if (getSetHttpOnlyMethod()!=null && httpOnly){
               ReflectionUtils.invokeMethod(setHttpOnlyMethod,cookie,Boolean.TRUE);
           }
           if (!StringUtils.isEmpty(domain)){
               cookie.setDomain(domain);
           }
           response.addCookie(cookie);
    }

    public void removeCookie(String name){
        setCookie(name,null,false,0);
    }

    private String encode(String value) {
        return value==null?null:new String(Base64Codec.encode(value.getBytes(Constants.ENCODING_UTF_8)));
    }

    private String decode(String value) {
        return value == null ? null : new String(Base64Codec.decode(value),Constants.ENCODING_UTF_8);
    }


    public Method getSetHttpOnlyMethod() {
        if (setHttpOnlyMethod == null) {
            setHttpOnlyMethod = ReflectionUtils.findMethod(Cookie.class, "setHttpOnly", boolean.class);
        }
        return setHttpOnlyMethod;
    }


}
