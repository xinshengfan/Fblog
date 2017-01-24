package com.fblog.service.vo;

import com.fblog.core.dao.entity.Post;
import com.fblog.core.dao.entity.Upload;
import com.fblog.core.dao.entity.User;

/**
 * Created by fansion on 18/01/2017.
 * 附件值对象
 */
public class UploadVO extends Upload {
    private Post post;
    private User user;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
