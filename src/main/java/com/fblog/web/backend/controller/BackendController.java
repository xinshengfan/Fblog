package com.fblog.web.backend.controller;

import com.fblog.biz.PostManager;
import com.fblog.core.Constants;
import com.fblog.core.dao.constants.PostConstants;
import com.fblog.core.dao.entity.User;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.core.utils.*;
import com.fblog.service.PostService;
import com.fblog.service.UploadService;
import com.fblog.service.UserService;
import com.fblog.service.shiro.StatelessToken;
import com.fblog.web.backend.form.LoginForm;
import com.fblog.web.backend.form.validator.LoginFormValidator;
import com.fblog.web.support.CookieRemeberManager;
import com.fblog.web.support.WebContextFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/backend")
public class BackendController {
    private static final String TAG = "BackendController";
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private PostManager postManager;

    @RequiresRoles(value = {"admin","editor"},logical = Logical.OR)
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("osInfo", Utility.getCurrentOsInfo());
         /* 基本站点统计信息 */
        model.addAttribute("userCount", userService.count());
        model.addAttribute("postCount", postService.count());
//        model.addAttribute("commentCount", commentService.count());
        model.addAttribute("uploadCount", uploadService.count());

        model.addAttribute("posts", postManager.listRecent(10, PostConstants.POST_CREATOR_ALL));
//        model.addAttribute("comments", commentManager.listRecent());
        return "backend/index";
    }

    @RequestMapping(value = "/wait",method = RequestMethod.GET)
    public String waitDevelop(){
        return "backend/common/empty";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(String msg, Model model){
        if(WebContextFactory.get().isLogon())
            return "redirect:/backend/index";

        if("logout".equals(msg)){
            model.addAttribute("msg", "您已登出。");
        }else if("unauthenticated".equals(msg)){
            model.addAttribute("msg", "你没有当前操作权限");
        }
        return "backend/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String dashboard(LoginForm form, HttpServletRequest request, HttpServletResponse response){
        MapContainer result = LoginFormValidator.validateLogin(form);
        if(!result.isEmpty()){
            request.setAttribute("msg", result.get("msg"));
            return "backend/login";
        }

        User user = userService.login(form.getUsername(), form.getPassword());
        if(user == null){
            request.setAttribute("msg", "用户名密码错误");
            return "backend/login";
        }

        SecurityUtils.getSubject().login(new StatelessToken(user.getId(), user.getPassword()));
        CookieUtils cookieUtil = new CookieUtils(request, response);
    /* 根据RFC-2109中的规定，在Cookie中只能包含ASCII的编码 */
        cookieUtil.setCookie(Constants.COOKIE_USER_NAME, form.getUsername(), false, 7 * 24 * 3600);
        cookieUtil.setCookie("comment_author", user.getNickName(), "/", false, 365 * 24 * 3600);
        cookieUtil.setCookie("comment_author_email", user.getEmail(), "/", false, 365 * 24 * 3600, false);
        cookieUtil.setCookie("comment_author_url", ServletUtils.getDomain(request), "/", false, 365 * 24 * 3600, false);

        CookieRemeberManager.loginSuccess(request, response, user.getId(), user.getPassword(), form.isRemeber());

        return "redirect:" + StringUtils.emptyDefault(form.getRedirectURL(), "/backend/index");
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        CookieRemeberManager.logout(request, response);
        SecurityUtils.getSubject().logout();
        CookieUtils cookieUtil = new CookieUtils(request, response);
        cookieUtil.removeCookie(Constants.COOKIE_CSRF_TOKEN);
        cookieUtil.removeCookie("comment_author");
        cookieUtil.removeCookie("comment_author_email");
        cookieUtil.removeCookie("comment_author_url");

        return "redirect:/backend/login?msg=logout";
    }


}
