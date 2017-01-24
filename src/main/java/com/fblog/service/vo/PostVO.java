package com.fblog.service.vo;

import com.fblog.core.dao.entity.Category;
import com.fblog.core.dao.entity.Post;
import com.fblog.core.dao.entity.User;

import java.util.List;

/**
 * Created by fansion on 18/01/2017.
 * 文章值对象
 */
public class PostVO extends Post {
    private User user;
    private Category category;
    private List<String> tags;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
