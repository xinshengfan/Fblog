package com.fblog.service.vo;

import com.fblog.core.dao.entity.Comment;
import com.fblog.core.dao.entity.Post;
import com.fblog.core.plugin.TreeItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommentVO extends Comment implements TreeItem<CommentVO> {
    private Post post;
    private List<CommentVO> children;

    @Override
    public Collection<CommentVO> getChildren() {
        return children;
    }

    @Override
    public void addChildren(CommentVO child) {
        if (children == null) {
            setChildren(new ArrayList<CommentVO>());
        }
        getChildren().add(child);
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setChildren(List<CommentVO> children) {
        this.children = children;
    }
}
