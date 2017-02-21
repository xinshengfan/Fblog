package com.fblog.web.backend.controller;

import com.fblog.biz.EhCacheManager;
import com.fblog.core.dao.entity.MapContainer;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

/**
 * Created by fansion on 20/01/2017.
 * 系统工具
 */
@Controller
@RequiresRoles(value = {"admin"},logical = Logical.OR)
@RequestMapping("/backend/tool")
public class ToolController {

    @Autowired
    private EhCacheManager enCacheManager;

    @RequestMapping(value = "/ehcache", method = RequestMethod.GET)
    public String ehcache(Model model){
        Collection<MapContainer> caches = enCacheManager.stats();
        model.addAttribute("caches", caches);
        model.addAttribute("vcount",enCacheManager.getVisitCount());
        return "backend/tool/caches";
    }

    @ResponseBody
    @RequestMapping(value = "/ehcache/${cacheName}", method = RequestMethod.DELETE)
    public Object clearEhcache(@PathVariable String cacheName){
        enCacheManager.clear(cacheName);
        return new MapContainer("success", true);
    }

}
