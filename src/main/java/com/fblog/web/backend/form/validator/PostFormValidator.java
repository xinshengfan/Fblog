package com.fblog.web.backend.form.validator;

import com.fblog.core.dao.constants.PostConstants;
import com.fblog.core.dao.entity.Post;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.core.utils.StringUtils;

/**
 * Created by fansion on 20/01/2017.
 * 文章上传时验证
 */
public class PostFormValidator {

    public static MapContainer validatePublish(Post post){
        return validatePost(post, true);
    }

    public static MapContainer validateUpdate(Post post){
        MapContainer form = validatePublish(post);
        if(StringUtils.isEmpty(post.getId())){
            form.put("msg", "文章ID不合法");
        }

        return form;
    }

    public static MapContainer validateFastUpdate(Post post){
        MapContainer form = validatePost(post, false);
        if(StringUtils.isEmpty(post.getId())){
            form.put("msg", "文章ID不合法");
        }

        return form;
    }

    private static MapContainer validatePost(Post post, boolean verifyContent){
        MapContainer form = new MapContainer();
        if(StringUtils.isEmpty(post.getTitle())){
            form.put("msg", "文章标题未填写");
        }else if(verifyContent && StringUtils.isEmpty(post.getContent())){
            form.put("msg", "请填写文章内容");
        }else if(PostConstants.TYPE_POST.equals(post.getType()) && StringUtils.isEmpty(post.getCategoryid())){
            form.put("msg", "请选择文章分类");
        }

        return form;
    }

}
