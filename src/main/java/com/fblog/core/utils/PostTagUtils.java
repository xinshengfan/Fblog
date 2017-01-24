package com.fblog.core.utils;

import com.fblog.core.dao.entity.Post;
import com.fblog.core.dao.entity.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by fansion on 20/01/2017.
 * 文章标签工具
 */
public class PostTagUtils {

    public static List<Tag> from(Post post, String tagsByComma, String creator){
        return !StringUtils.isEmpty(tagsByComma) ? from(post, Arrays.asList(tagsByComma.split(",")), creator) : Collections
                .<Tag> emptyList();
    }

    public static List<Tag> from(Post post, List<String> tags, String creator){
        List<Tag> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(tags)){
            for(String tag : tags){
                Tag t = new Tag();
                t.setId(IDGenerator.uuid19());
                t.setName(tag.trim());
                t.setCreateTime(post.getLastUpdate());
                t.setPostid(post.getId());
                t.setCreator(creator);
                t.setLastUpdate(post.getLastUpdate());
                list.add(t);
            }
        }

        return list;
    }
}
