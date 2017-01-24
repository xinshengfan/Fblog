package com.fblog.web.backend.controller;

import com.fblog.biz.OptionManager;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.service.CategoryService;
import com.fblog.web.backend.form.GeneralOption;
import com.fblog.web.backend.form.MailOption;
import com.fblog.web.backend.form.PostOption;
import com.fblog.web.backend.form.validator.OptionFormValidator;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 常规选项
 */
@Controller
@RequestMapping("backend/options")
@RequiresRoles(value = {"admin"},logical = Logical.OR)
public class OptionsController {

    @Autowired
    private OptionManager optionManager;
    @Autowired
    private CategoryService categoryService;


    @RequestMapping(value = "/general", method = RequestMethod.GET)
    public String general(Model model) {
        model.addAttribute("form", optionManager.getGeneralOption());
        return "backend/options/general";
    }

    @RequestMapping(value = "/general", method = RequestMethod.POST)
    public String general(GeneralOption option, Model model) {
        //需要数据回填
        model.addAttribute("form", option);
        MapContainer validateResult = OptionFormValidator.validateGeneral(option);
        if (validateResult.isEmpty()) {
            optionManager.updateGeneralOption(option);
            model.addAttribute("success", true);
        } else {
            model.addAllAttributes(validateResult);
        }
        return "backend/options/general";
    }

    @RequestMapping(value = "/post", method = RequestMethod.GET)
    public String postAndEdit(Model model) {
        model.addAttribute("categorys", categoryService.list());
        model.addAttribute("form", optionManager.getPostOption());
        return "backend/options/post";
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String postAndEdit(PostOption option, Model model) {
        model.addAttribute("categorys", categoryService.list());
        model.addAttribute("form", option);
        MapContainer validateResult = OptionFormValidator.validetePost(option);
        if (validateResult.isEmpty()) {
            optionManager.updatePostOption(option);
            model.addAttribute("success", true);
        }
        return "backend/options/post";
    }

    @RequestMapping(value = "/email", method = RequestMethod.GET)
    public String email(Model model) {
        model.addAttribute("form", optionManager.getMailOption());
        return "backend/options/email";
    }

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public String email(MailOption option, Model model) {
        model.addAttribute("form", option);
        MapContainer validateResult = OptionFormValidator.validateMail(option);
        if (validateResult.isEmpty()) {
            optionManager.updaMailOption(option);
            model.addAttribute("success", true);
        }
        return "backend/options/email";
    }

}
