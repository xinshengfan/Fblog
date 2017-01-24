package com.fblog.core.dao.mapper;

import com.fblog.core.dao.entity.Category;
import com.fblog.core.dao.entity.MapContainer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper extends BaseMapper {

    List<MapContainer> list();

    Category loadByName(String name);

    /**
     * 获取指定分类的子类
     */
    List<Category> loadChildren(Category category);

    void  updateInsertLeftv(int leftv);

    void updateInsertRightv(int right);

    void delete(@Param("leftv")int leftv,@Param("rightv")int rightv);

    void updateDeleteLeftv(@Param("leftv")int leftv,@Param("length")int length);

    void updateDeleteRight(@Param("rightv")int rightv,@Param("length")int length);

}
