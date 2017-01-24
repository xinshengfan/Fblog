package com.fblog.service;

import com.fblog.core.dao.entity.Tag;
import com.fblog.core.dao.mapper.BaseMapper;
import com.fblog.core.dao.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService extends BaseService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    protected BaseMapper getMapper() {
        return tagMapper;
    }

    /**
     * 获取当前文章所有的标签
     */
    public List<String> listTagsByPost(String postId) {
        return tagMapper.getTagsByPost(postId);
    }

    /**
     * 批量插入标签
     */
    public int insertBatch(List<Tag> tags) {
        return tagMapper.insertBatch(tags);
    }

    public int deleteByPostId(String postId) {
        return tagMapper.deleteByPostid(postId);
    }

}
