package com.fblog.web.backend.form.validator;

import com.fblog.core.dao.entity.User;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.core.utils.CommRegular;
import com.fblog.core.utils.StringUtils;


public class UserFormValidator {

    public static MapContainer validateInsert(User user, String repass){
        MapContainer form = validateUser(user);
        if(StringUtils.isEmpty(user.getPassword()) || StringUtils.isEmpty(repass)){
            form.put("password", "需填写用户密码");
        }
        if(!user.getPassword().equals(repass) || !user.getPassword().matches(CommRegular.PASSWD)){
            form.put("password", "两次密码不一致或者密码格式不对");
        }

        return form;
    }

    public static MapContainer validateUpdate(User user, String repass){
        MapContainer form = validateUser(user);
        if(!StringUtils.isEmpty(user.getPassword())
                && (!user.getPassword().equals(repass) || !user.getPassword().matches(CommRegular.PASSWD))){
            form.put("password", "两次密码不一致或者密码格式不对");
        }else if(StringUtils.isEmpty(user.getId())){
            form.put("msg", "ID不合法");
        }

        return form;
    }

    private static MapContainer validateUser(User user){
        MapContainer form = new MapContainer();
        if(StringUtils.isEmpty(user.getNickName())){
            form.put("nickName", "需填写用户名称");
        }
        if(StringUtils.isEmpty(user.getEmail()) || !user.getEmail().matches(CommRegular.EMAIL)){
            form.put("email", "邮箱格式不正确");
        }
        if(StringUtils.isEmpty(user.getRealName())){
            form.put("realName", "需填写用户真实名称");
        }

        return form;
    }

    public static MapContainer validateMy(User user, String repass){
        MapContainer form = new MapContainer();
        if(StringUtils.isEmpty(user.getEmail()) || !user.getEmail().matches(CommRegular.EMAIL)){
            form.put("email", "邮箱格式不正确");
        }
        if(StringUtils.isEmpty(user.getRealName())){
            form.put("realName", "需填写用户真实名称");
        }

        if(!StringUtils.isEmpty(user.getPassword())
                && (!user.getPassword().equals(repass) || !user.getPassword().matches(CommRegular.PASSWD))){
            form.put("password", "两次密码不一致或者密码格式不对");
        }else if(StringUtils.isEmpty(user.getId())){
            form.put("msg", "ID不合法");
        }

        return form;
    }
}
