package com.fblog.web.front.controller;

import com.fblog.biz.PostManager;
import com.fblog.core.WebConstants;
import com.fblog.core.dao.constants.PostConstants;
import com.fblog.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 首页
 */
@Controller
public class IndexController {
    @Autowired
    private PostManager postManager;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(@RequestParam(value = "page", defaultValue = "1") int page, String word, Model model) {
        if (StringUtils.isEmpty(word)) {
            model.addAttribute("page", postManager.listPost(page, PostConstants.MAX_POST_SHOW));
        } else {
            //搜索
            word = word.trim();
            model.addAttribute("page", postManager.search(word, page));
            model.addAttribute("search", word);
            model.addAttribute(WebConstants.PRE_TITLE_KEY, word);
        }
        return "front/home";
    }

}
