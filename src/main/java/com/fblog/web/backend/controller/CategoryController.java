package com.fblog.web.backend.controller;

import com.fblog.biz.CategoryManager;
import com.fblog.core.dao.entity.Category;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.core.utils.CollectionUtils;
import com.fblog.core.utils.IDGenerator;
import com.fblog.core.utils.LogUtils;
import com.fblog.service.CategoryService;
import com.fblog.web.backend.form.validator.CategoryFormValidator;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequiresRoles(value = {"admin","editor"},logical = Logical.OR)
@RequestMapping("/backend/categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryManager categoryManager;

    @RequestMapping(method = RequestMethod.GET)
    public String index(){
        return "backend/post/category";
    }

    @ResponseBody
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public Object data(){
        List<MapContainer> list = categoryManager.listAsTree();
        for (MapContainer item:list){
            item.put("text",item.remove("name"));
            List<MapContainer> nodes = item.get("nodes");
            if (CollectionUtils.isEmpty(nodes)){
                continue;
            }
            for (MapContainer child:nodes){
                child.put("text",child.remove("name"));
                child.put("icon","glyphicon glyphicon-star");
            }
        }
        return list;
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Object insert(Category category,String parent){
        MapContainer form = CategoryFormValidator.validateInsert(category);
        if (!form.isEmpty()){
            return form.put("success",false);
        }
        category.setId(IDGenerator.uuid19());
        category.setCreateTime(new Date());
        category.setLastUpdate(category.getCreateTime());
        return new MapContainer("success",categoryService.insertChildren(category, parent));
    }

    @ResponseBody
    @RequestMapping(value = "/{categoryName}",method = RequestMethod.DELETE)
    public Object remove(String categoryName){
        categoryManager.remove(categoryName);
        return new MapContainer("success",false);
    }

}
