package com.fblog.biz;

import com.fblog.core.dao.constants.OptionConstants;
import com.fblog.core.dao.constants.PostConstants;
import com.fblog.core.utils.NumberUtils;
import com.fblog.core.utils.StringUtils;
import com.fblog.service.OptionsService;
import com.fblog.web.backend.form.GeneralOption;
import com.fblog.web.backend.form.MailOption;
import com.fblog.web.backend.form.PostOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OptionManager {
    @Autowired
    private OptionsService optionsService;

    /**
     * 更新网站基础设置
     */
    @Transactional
    public void updateGeneralOption(GeneralOption form) {
        optionsService.updateOptionValue(OptionConstants.TITLE, form.getTitle());
        optionsService.updateOptionValue(OptionConstants.SUBTITLE, form.getSubtitle());
        optionsService.updateOptionValue(OptionConstants.DESCRIPTION, form.getDescription());
        optionsService.updateOptionValue(OptionConstants.KEYWORDS, form.getKeywords());
        optionsService.updateOptionValue(OptionConstants.WEB_URL, form.getWeburl());
    }

    /**
     * 获取当前站点能用设置
     */
    public GeneralOption getGeneralOption() {
        String title = optionsService.getOptionValue(OptionConstants.TITLE);
        if (!StringUtils.isEmpty(title)) {
            GeneralOption option = new GeneralOption();
            option.setTitle(title);
            option.setSubtitle(optionsService.getOptionValue(OptionConstants.SUBTITLE));
            option.setDescription(optionsService.getOptionValue(OptionConstants.DESCRIPTION));
            option.setKeywords(optionsService.getOptionValue(OptionConstants.KEYWORDS));
            option.setWeburl(optionsService.getOptionValue(OptionConstants.WEB_URL));
            return option;
        }
        return null;
    }

    /**
     * 更新文章设置选项
     */
    @Transactional
    public void updatePostOption(PostOption form) {
        optionsService.updateOptionValue(OptionConstants.MAXSHOW, String.valueOf(form.getMaxshow()));
        optionsService.updateOptionValue(OptionConstants.ALLOW_COMMENT, String.valueOf(form.isAllowComment()));
        optionsService.updateOptionValue(OptionConstants.DEFAULT_CATEGORY_ID, form.getDefaultCategory());
    }

    public PostOption getPostOption() {
        PostOption option = new PostOption();
        option.setMaxshow(NumberUtils.toInteger(optionsService.getOptionValue(OptionConstants.MAXSHOW), PostConstants.MAX_POST_SHOW));
        option.setAllowComment(Boolean.parseBoolean(optionsService.getOptionValue(OptionConstants.ALLOW_COMMENT)));
        option.setDefaultCategory(optionsService.getOptionValue(OptionConstants.DEFAULT_CATEGORY_ID));
        return option;
    }

    /**
     * 更新邮件默认设置
     */
    @Transactional
    public void updaMailOption(MailOption form) {
        optionsService.updateOptionValue(OptionConstants.MAIL_HOST, form.getHost());
        optionsService.updateOptionValue(OptionConstants.MAIL_PORT, String.valueOf(form.getPort()));
        optionsService.updateOptionValue(OptionConstants.MAIL_USERNAMR, form.getUsername());
        optionsService.updateOptionValue(OptionConstants.MAIL_PASSWORD, form.getPassword());
    }

    public MailOption getMailOption() {
        String host = optionsService.getOptionValue(OptionConstants.MAIL_HOST);
        if (!StringUtils.isEmpty(host)) {
            MailOption option = new MailOption();
            option.setHost(host);
            option.setPort(Integer.parseInt(optionsService.getOptionValue(OptionConstants.MAIL_PORT)));
            option.setPassword(optionsService.getOptionValue(OptionConstants.MAIL_PASSWORD));
            option.setUsername(optionsService.getOptionValue(OptionConstants.MAIL_USERNAMR));
            return option;
        }
        return null;
    }

    /**
     * 获取下一篇文章id,使用select for update来保证自增，故需要在事务中处理
     */
    @Transactional
    public String getNextPostId() {
        String currentId = optionsService.getOptionValueForUpdate(OptionConstants.POSTID);
        int id = NumberUtils.toInteger(currentId, PostConstants.INIT_POST_ID);
        id++;
        optionsService.updateOptionValue(OptionConstants.POSTID, String.valueOf(id));
        return String.valueOf(id);
    }


}
