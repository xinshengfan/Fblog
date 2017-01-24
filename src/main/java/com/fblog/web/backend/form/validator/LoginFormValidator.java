package com.fblog.web.backend.form.validator;

import com.fblog.core.dao.entity.MapContainer;
import com.fblog.core.utils.CommRegular;
import com.fblog.core.utils.StringUtils;
import com.fblog.web.backend.form.LoginForm;

public class LoginFormValidator {
    private static String guard;

    /**
     * 设置防止恶意登录token,由spring注入
     *
     * @param guard
     */
    public static void setLoginGuard(String guard){
        LoginFormValidator.guard = guard;
    }

    public static MapContainer validateLogin(LoginForm form){
        MapContainer result = new MapContainer();
    /* 防止用户恶意登录 */
        /**TODO: fansion 此处上传的form中的guard一直没有值，不明白此处的用法，先不用*/
//    if(!guard.equals(form.getGuard())){
//      result.put("msg", "请不要尝试登录了!");
//    }else

        if(StringUtils.isEmpty(form.getUsername()) || !form.getUsername().matches(CommRegular.USERNAME)){
            result.put("msg", "请输入正确的用户名");
        }else if(StringUtils.isEmpty(form.getPassword()) || !form.getPassword().matches(CommRegular.PASSWD)){
            result.put("msg", "密码输入有误");
        }

        return result;
    }
}
