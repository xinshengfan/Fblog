package com.fblog.web.backend.controller;

import com.fblog.biz.OptionManager;
import com.fblog.biz.PostManager;
import com.fblog.biz.aop.IndexManager;
import com.fblog.core.dao.constants.PostConstants;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.core.dao.entity.PageModel;
import com.fblog.core.dao.entity.Post;
import com.fblog.core.utils.JsoupUtils;
import com.fblog.core.utils.PostTagUtils;
import com.fblog.core.utils.StringUtils;
import com.fblog.service.CategoryService;
import com.fblog.service.PostService;
import com.fblog.service.vo.PostVO;
import com.fblog.web.backend.form.validator.PostFormValidator;
import com.fblog.web.support.WebContextFactory;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

@Controller
@RequiresRoles(value = {"admin", "editor"}, logical = Logical.OR)
@RequestMapping("backend/posts")
public class PostController {

    @Autowired
    private PostManager postManager;
    @Autowired
    private OptionManager optionManager;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostService postService;
    @Autowired
    private IndexManager indexManager;

    /**
     * 在添加Lucene搜索功能后，对原先数据库中已的插入的数据建立索引
     */
    @RequestMapping(value = "/updateLucene", method = RequestMethod.GET)
    public String updateLucene(Model model) {
        PageModel<PostVO> pageModel = postManager.listPost(1, 100);
        for (Post post : pageModel.getContent()) {
            indexManager.remove(post.getId(),PostConstants.TYPE_POST);
            indexManager.insert(post);
        }
        model.addAttribute("msg", "更新Lucene索引成功：" + pageModel.getContent().size());
        return "error";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(String pid, Model model) {
        if (!StringUtils.isEmpty(pid)) {
            model.addAttribute("post", postManager.loadReadById(pid));
        }
        model.addAttribute("categorys", categoryService.list());
        return "backend/post/edit";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        PageModel<PostVO> pageModel = postManager.listPost(page, PostConstants.MAX_POST_SHOW);
        model.addAttribute("page", pageModel);
        model.addAttribute("categorys", categoryService.list());
        return "backend/post/list";
    }

    /**
     * 插入文章
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Object insert(Post post, String tags) {
        MapContainer mapContainer = PostFormValidator.validatePublish(post);
        if (!mapContainer.isEmpty()) {
            return mapContainer.put("success", false);
        }
        String content = HtmlUtils.htmlUnescape(post.getContent());
        content = JsoupUtils.filter(content);
        post.setContent(content);
        String cleanTxt = JsoupUtils.plainText(content);
        cleanTxt = cleanTxt.length() > PostConstants.EXCERPT_LENGTH ? cleanTxt.substring(0, PostConstants.EXCERPT_LENGTH) : cleanTxt;
        post.setExcerpt(cleanTxt + "…");
        post.setId(optionManager.getNextPostId());
        post.setCreator(WebContextFactory.get().getUser().getId());
        post.setCreateTime(new Date());
        post.setLastUpdate(post.getCreateTime());
        String title = post.getTitle();
        title = title.length() > PostConstants.MAX_TITLE_LENGTH ? title.substring(0, PostConstants.MAX_TITLE_LENGTH) + "…": title;
        post.setTitle(title);

        postManager.insertPost(post, PostTagUtils.from(post, tags, post.getCreator()));
        return new MapContainer("success", true);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public Object update(Post post, String tags) {
        MapContainer form = PostFormValidator.validateUpdate(post);
        if (!form.isEmpty()) {
            return form.put("success", false);
        }

    /* 由于加入xss的过滤,html内容都被转义了,这里需要unescape */
        String content = HtmlUtils.htmlUnescape(post.getContent());
        post.setContent(JsoupUtils.filter(content));
        String cleanTxt = JsoupUtils.plainText(content);
        String excerpt = cleanTxt.length() > PostConstants.EXCERPT_LENGTH ? cleanTxt.substring(0,
                PostConstants.EXCERPT_LENGTH) : cleanTxt;
        post.setExcerpt(excerpt + "…");

        post.setType(PostConstants.TYPE_POST);
        post.setLastUpdate(new Date());
        String title = post.getTitle();
        title = title.length() > PostConstants.MAX_TITLE_LENGTH ? title.substring(0, PostConstants.MAX_TITLE_LENGTH) + "…": title;
        post.setTitle(title);
        postManager.updatePost(post, PostTagUtils.from(post, tags, WebContextFactory.get().getUser().getId()));
        return new MapContainer("success", true);
    }

    @ResponseBody
    @RequestMapping(value = "/fast", method = RequestMethod.PUT)
    public Object fast(Post post, String tags) {
        MapContainer form = PostFormValidator.validateFastUpdate(post);
        if (!form.isEmpty()) {
            return form.put("success", false);
        }

        Post old = postService.loadById(post.getId());
        if (old == null) {
            return form.put("success", false).put("msg", "非法请求");
        }

        post.setContent(old.getContent());
        post.setExcerpt(old.getExcerpt());

        post.setType(PostConstants.TYPE_POST);
        post.setLastUpdate(new Date());
        postManager.updatePost(post, PostTagUtils.from(post, tags, WebContextFactory.get().getUser().getId()), true);
        return new MapContainer("success", true);
    }

    @ResponseBody
    @RequestMapping(value = "/{postid}", method = RequestMethod.DELETE)
    public Object remove(@PathVariable("postid") String postid) {
        postManager.deletePost(postid, PostConstants.TYPE_POST);
        return new MapContainer("success", true);
    }

}
