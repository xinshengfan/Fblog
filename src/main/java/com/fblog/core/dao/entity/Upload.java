package com.fblog.core.dao.entity;

/**
 * Created by fansion on 17/01/2017.
 * 上传的图片
 */
public class Upload extends BaseEntity{
    /* 对应文章id */
    private String postid;
    /* 图片名称 */
    private String name;
    /* 图片访问路径 */
    private String path;

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
