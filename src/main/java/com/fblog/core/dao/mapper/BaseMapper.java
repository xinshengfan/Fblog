package com.fblog.core.dao.mapper;

import com.fblog.core.dao.entity.PageModel;

import java.util.List;

public interface BaseMapper {
    /**
     * 增
     */
    <T> int insert(T t);

    /**
     * 删
     */
    <T> int deleteById(String id);

    /**
     * 查
     */
    <T> T loadById(String id);

    <T> List<T> list();

    /**
     * 改
     */
    <T> int update(T t);

    long count();

    <T> List<T> list(PageModel<T> model);

}
