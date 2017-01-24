package com.fblog.service.vo;

import com.fblog.core.dao.entity.Post;
import com.fblog.core.plugin.TreeItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by fansion on 18/01/2017.
 * 页面业务对象,区别于PostVO的有上下页，需要前后节点的关联
 */
public class PageVO extends Post implements TreeItem<PageVO> {
    private List<PageVO> children;

    @Override
    public Collection<PageVO> getChildren() {
        return children;
    }

    @Override
    public void addChildren(PageVO comment) {
        if (children == null) {
            setChildren(new ArrayList<PageVO>());
        }
        getChildren().add(comment);
    }

    public void setChildren(List<PageVO> children) {
        this.children = children;
    }
}
