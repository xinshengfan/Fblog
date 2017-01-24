package com.fblog.web.front.controller;

import com.fblog.biz.PostManager;
import com.fblog.core.dao.constants.PostConstants;
import com.fblog.core.utils.LogUtils;
import com.fblog.service.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 首页
 * */
@Controller
public class IndexController {
    @Autowired
    private PostManager postManager;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(@RequestParam(value = "page",defaultValue ="1")int page, Model model){
        model.addAttribute("page",postManager.listPost(page, PostConstants.MAX_POST_SHOW));
        return "index";
    }

}
