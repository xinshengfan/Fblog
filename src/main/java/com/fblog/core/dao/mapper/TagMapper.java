package com.fblog.core.dao.mapper;

import com.fblog.core.dao.entity.Tag;

import java.util.List;

public interface TagMapper extends BaseMapper {

    int insertBatch(List<Tag> list);

    int deleteByPostid(String postid);

    List<String> getTagsByPost(String postid);

}
