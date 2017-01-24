package com.fblog.core.plugin;

import com.fblog.core.utils.StringUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by fansion on 18/01/2017.
 * 森林构建工具
 */
public class TreeUtils {
    /**
     * 将一个list构建成一个森林
     */
    public static <T extends TreeItem<T>> void rebuildTree(List<T> list) {
        while (!buildFinish(list)) {
            Iterator<T> iterator = list.iterator();
            while (iterator.hasNext()) {
                T item = iterator.next();
                if (hasChild(list, item.getId())) {
                    //因为添加子节点时是一次必遍历所有节点，故不需要重复添加
                    continue;
                }
                if (!StringUtils.isEmpty(item.getParent())) {
                    //若存在父节点，则获取父节点，并把所有属于他的子节点添加到的节点下
                    for (T temp : list) {
                        if (item.getParent().equals(temp.getId())) {
                            iterator.remove();
                            temp.addChildren(item);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 是否构建森林成功；
     * 若在当前所有顶级节点中没有斜体一个子节点，就认为森林构建成功
     */
    private static <T extends TreeItem<T>> boolean buildFinish(List<T> list) {
        for (T t : list) {
            if (hasChild(list, t.getId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 指定的id下是否还有子节点
     */
    private static <T extends TreeItem<T>> boolean hasChild(List<T> list, String id) {
        for (T t : list) {
            if (id.equals(t.getParent())) {
                return true;
            }
        }
        return false;
    }
}
