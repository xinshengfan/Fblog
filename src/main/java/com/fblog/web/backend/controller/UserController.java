package com.fblog.web.backend.controller;

import com.fblog.core.dao.entity.User;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.core.dao.entity.PageModel;
import com.fblog.core.utils.IDGenerator;
import com.fblog.core.utils.LogUtils;
import com.fblog.core.utils.StringUtils;
import com.fblog.service.UserService;
import com.fblog.web.backend.form.validator.UserFormValidator;
import com.fblog.web.support.WebContextFactory;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequiresAuthentication
@RequestMapping("/backend/users")
public class UserController {
@Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    @RequiresRoles("admin")
    public String index(@RequestParam(value = "page",defaultValue = "1") int page, Model model){
      PageModel<User> users = userService.list(page,10);
//       List<User> users = userService.list();
        LogUtils.i("test","查询到的用户："+users.getContent().size()+" , "+users.getContent().get(0));
        model.addAttribute("page",users);
        return "backend/user/list";
    }

    @RequestMapping(method = RequestMethod.POST)
    @RequiresRoles("admin")
    public String insert(User user,String repass,Model model){
        MapContainer form = UserFormValidator.validateInsert(user, repass);
        if(!form.isEmpty()){
            model.addAllAttributes(form);
            return "backend/user/edit";
        }
        user.setId(IDGenerator.uuid19());
        user.setCreateTime(new Date());
        user.setLastUpdate(user.getCreateTime());
        userService.insert(user);
        return "redirect:/backend/users";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(User user, String repass, Model model){
        MapContainer form = UserFormValidator.validateUpdate(user, repass);
        if(!form.isEmpty()){
            model.addAllAttributes(form);
            model.addAttribute("user", user);
            return "backend/user/edit";
        }
        user.setLastUpdate(new Date());
        userService.update(user);
        return "redirect:/backend/users";
    }

    @ResponseBody
    @RequestMapping(value = "/{userid}", method = RequestMethod.DELETE)
    @RequiresRoles("admin")
    public Object remove(@PathVariable("userid") String userid){
        userService.deleteById(userid);
        return new MapContainer("success", true);
    }


    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    @RequiresRoles("admin")
    public String edit(String uid,Model model){
        if (!StringUtils.isEmpty(uid)){
            model.addAttribute("user",userService.loadById(uid));
        }
        return "backend/user/edit";
    }

    @RequestMapping(value = "my",method = RequestMethod.GET)
    public String my(Model model){
        model.addAttribute("my", WebContextFactory.get().getUser());
        return "backend/user/my";
    }

    @RequestMapping(value = "/my",method = RequestMethod.PUT)
    public String updateMy(User user,String repass,Model model){
        MapContainer form = UserFormValidator.validateMy(user, repass);
        if(!form.isEmpty()){
            model.addAllAttributes(form);
            model.addAttribute("my", user);
            return "backend/user/my";
        }
        user.setRole(WebContextFactory.get().getUser().getRole());
        user.setLastUpdate(new Date());
        userService.update(user);
        return "redirect:/backend/users";
    }

}
