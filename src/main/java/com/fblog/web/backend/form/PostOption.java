package com.fblog.web.backend.form;

/**
 * Created by fansion on 18/01/2017.
 * 文章选项
 */
public class PostOption {
    private int maxshow;
    private boolean allowComment;
    private String defaultCategory;

    public int getMaxshow() {
        return maxshow;
    }

    public void setMaxshow(int maxshow) {
        this.maxshow = maxshow;
    }

    public boolean isAllowComment() {
        return allowComment;
    }

    public void setAllowComment(boolean allowComment) {
        this.allowComment = allowComment;
    }

    public String getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(String defaultCategory) {
        this.defaultCategory = defaultCategory;
    }
}
