package com.fblog.service;

import com.fblog.core.dao.entity.BaseEntity;
import com.fblog.core.dao.mapper.BaseMapper;
import com.fblog.core.dao.entity.PageModel;
import com.fblog.core.utils.LogUtils;

import java.util.List;

public abstract class BaseService {
    public <T extends BaseEntity> int insert(T t) {
        return getMapper().insert(t);
    }

    public <T extends BaseEntity> T loadById(String id) {
        return getMapper().loadById(id);
    }

    public <T> List<T> list() {
        return getMapper().list();
    }

    public <T> void list(PageModel<T> model) {
        //先查询所有的文章的id，须在子service中的mapper.xml中具体实现
        List<T> result = getMapper().list(model);
        //将这些文章的id集合放入model中
        model.setContent(result);
    }

    public <T extends BaseEntity> int update(T t) {
        return getMapper().update(t);
    }

    public int deleteById(String id) {
        return getMapper().deleteById(id);
    }

    public long count() {
        return getMapper().count();
    }

    protected abstract BaseMapper getMapper();
}
