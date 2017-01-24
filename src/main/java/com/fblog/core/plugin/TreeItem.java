package com.fblog.core.plugin;

import java.util.Collection;

/**
 * Created by fansion on 18/01/2017.
 * 构建森林接口
 */
public interface TreeItem<T> {
    String getId();

    String getParent();

    Collection<T> getChildren();

    void addChildren(T child);
}
