package com.fblog.web.backend.form.validator;

import com.fblog.core.dao.entity.MapContainer;
import com.fblog.core.utils.StringUtils;
import com.fblog.web.backend.form.GeneralOption;
import com.fblog.web.backend.form.MailOption;
import com.fblog.web.backend.form.PostOption;

/**
 * Created by fansion on 19/01/2017.
 * 选项表单的验证器
 */
public class OptionFormValidator {

    public static MapContainer validateGeneral(GeneralOption option) {
        MapContainer container = new MapContainer();
        if (StringUtils.isEmpty(option.getTitle())) {
            container.put("title", "需要填写站名名称");
        }
        if (StringUtils.isEmpty(option.getSubtitle())) {
            container.put("subtitle", "需填写站点描述");
        }
        if (StringUtils.isEmpty(option.getDescription())) {
            container.put("description", "需填写站点关键字");
        }
        if (StringUtils.isEmpty(option.getKeywords())) {
            container.put("keywords", "需填写站点关键字");
        }
        if (StringUtils.isEmpty(option.getWeburl())) {
            container.put("weburl", "需填写网站url");
        }
        return container;
    }

    public static MapContainer validetePost(PostOption option) {
        MapContainer container = new MapContainer();
        if (option.getMaxshow() < 1) {
            container.put("maxshow", "输入错误");
        }
        if (StringUtils.isEmpty(option.getDefaultCategory())) {
            container.put("defaultCategory", "默认分类格式错误");
        }
        return container;
    }

    public static MapContainer validateMail(MailOption form) {
        MapContainer result = new MapContainer();
        if (StringUtils.isEmpty(form.getHost())) {
            result.put("host", "请输入host");
        }
        if (form.getPort() < 10) {
            result.put("port", "请输入合法端口号");
        }
        if (StringUtils.isEmpty(form.getUsername())) {
            result.put("username", "请输入用户名");
        }
        if (StringUtils.isEmpty(form.getPassword())) {
            result.put("password", "请输入密码");
        }
        return result;
    }
}
