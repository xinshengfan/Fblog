package com.fblog.web.front.controller;

import com.fblog.biz.PostManager;
import com.fblog.biz.VisitStateManager;
import com.fblog.service.PostService;
import com.fblog.service.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by fansion on 22/01/2017.
 * 前台请求的控制器
 */
@Controller
@RequestMapping("/post")
public class PostFrontController {

    @Autowired
    private PostManager postManager;
    @Autowired
    private PostService postService;
    @Autowired
    private VisitStateManager visitStateManager;

    @RequestMapping(value = "/{postid}",method = RequestMethod.GET)
    public String post(@PathVariable("postid")String postid, Model model){
        PostVO postVO = postManager.loadReadById(postid);
        if (postVO!=null){
            visitStateManager.record(postid);
            model.addAttribute("post",postVO);

            //上一篇和下一篇
            model.addAttribute("prev",postService.getPrevPost(postid));
            model.addAttribute("next",postService.getNextPost(postid));
            return "front/post";
        }else {
            model.addAttribute("msg","文章找不到了");
            return "error";
        }
    }
}
