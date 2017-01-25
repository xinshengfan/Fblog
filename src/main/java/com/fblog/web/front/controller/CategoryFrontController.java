package com.fblog.web.front.controller;

import com.fblog.biz.PostManager;
import com.fblog.core.WebConstants;
import com.fblog.core.dao.constants.PostConstants;
import com.fblog.core.dao.entity.Category;
import com.fblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/category")
public class CategoryFrontController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostManager postManager;

    @RequestMapping(value = "/{categoryname}", method = RequestMethod.GET)
    public String post(@PathVariable(value = "categoryname") String name,
                       @RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        Category category = categoryService.loadByName(name);
        if (category != null) {
            model.addAttribute(WebConstants.PRE_TITLE_KEY,name);
            model.addAttribute("page", postManager.listByCategory(category, page, PostConstants.MAX_POST_SHOW));
        }
        model.addAttribute("category",category);
        return "index";
    }
}
