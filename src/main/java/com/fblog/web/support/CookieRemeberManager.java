package com.fblog.web.support;

import com.fblog.core.Constants;
import com.fblog.core.dao.entity.User;
import com.fblog.core.plugin.ApplicationContextUtil;
import com.fblog.core.security.HashCalculator;
import com.fblog.core.security.Hex;
import com.fblog.core.utils.CookieUtils;
import com.fblog.core.utils.IDGenerator;
import com.fblog.core.utils.StringUtils;
import com.fblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基于cookie的传话管理器
 */
public class CookieRemeberManager {
    private static final String COOKIE_KEY = IDGenerator.uuid19();

    /**
     * 提取有效的user信息
     */
    public static User extractValidRemeberMeCoolieUser(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils cookieUtils = new CookieUtils(request, response);
        String token = cookieUtils.getCookie(Constants.COOKIE_CONTEXT_ID);
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String[] cookieTokens = token.split(":");
        if (cookieTokens.length != 3) {
            return null;
        }
        long tokenExpiryTime;
        try {
            tokenExpiryTime = new Long(cookieTokens[1]).longValue();
        } catch (Exception e) {
            return null;
        }
        if (isTokenExpired(tokenExpiryTime)) {
            return null;
        }
        UserService userService = ApplicationContextUtil.getBean(UserService.class);
        User user = userService.loadById(cookieTokens[0]);
        if (user == null)
            return null;
        String expectTokenSignature = makeTokenSignature(cookieTokens[0], tokenExpiryTime, user.getPassword());
        return expectTokenSignature.equals(cookieTokens[2]) ? user : null;
    }

    /**
     * 登录成功后使用userid和password生成cookie
     */
    public static void loginSuccess(HttpServletRequest request, HttpServletResponse response, String userId, String password, boolean remeber) {
        long tokenExpiryTime = remeber ? System.currentTimeMillis() + Constants.COOKIE_EXPIRY_TIME : -1;
        CookieUtils cookieUtils = new CookieUtils(request, response);
        String cookieValue = userId + ":" + tokenExpiryTime + ":" + makeTokenSignature(userId, tokenExpiryTime, password);
        if (remeber) {
            cookieUtils.setCookie(Constants.COOKIE_CONTEXT_ID, cookieValue, true, Constants.COOKIE_EXPIRY_TIME);
        } else {
            cookieUtils.setCookie(Constants.COOKIE_CONTEXT_ID, cookieValue, true);
        }
    }

    /**
     * 生成token,利用userId,存活时间及密码生成一个md5值
     *
     * @param userId .
     * @return 计算出的token
     */
    private static String makeTokenSignature(String userId, long tokenExpiryTime, String password) {
        String str = userId + ":" + Long.toString(tokenExpiryTime) + ":" + password;
        return Hex.bytes2Hex(HashCalculator.md5(str.getBytes()));
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response){
        CookieUtils cookieUtil = new CookieUtils(request, response);
        cookieUtil.removeCookie(Constants.COOKIE_CONTEXT_ID);
    }

    /**
     * 当前登录的token是否过期,TODO:此处好像是记不过期？
     *
     * @param tokenExpiryTime .
     * @return
     * @throws
     */
    private static boolean isTokenExpired(long tokenExpiryTime) {
        return tokenExpiryTime == -1 ? false : tokenExpiryTime < System.currentTimeMillis();
    }
}
