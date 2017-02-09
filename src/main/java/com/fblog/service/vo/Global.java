package com.fblog.service.vo;

import com.fblog.core.dao.constants.OptionConstants;
import com.fblog.core.plugin.ApplicationContextUtil;
import com.fblog.service.OptionsService;

/**
 * view中使用的全局对象
 */
public class Global {
    private String domain;

    public Global(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    /******* 其他全局常量 ******/
    public String getTitle() {
        return getOptionValue(OptionConstants.TITLE);
    }

    public String getSubTitle() {
        return getOptionValue(OptionConstants.SUBTITLE);
    }

    public String getDescription() {
        return getOptionValue(OptionConstants.DESCRIPTION);
//        return "默认描述，后需从数据库查询";
    }


    public String getKeyWords() {
        return getOptionValue(OptionConstants.KEYWORDS);
    }

    private String getOptionValue(String name) {
        OptionsService optionsService = ApplicationContextUtil.getBean(OptionsService.class);
        return optionsService.getOptionValue(name);
    }
}
