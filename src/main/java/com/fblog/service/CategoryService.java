package com.fblog.service;

import com.fblog.core.dao.constants.CategoryConstants;
import com.fblog.core.dao.entity.Category;
import com.fblog.core.dao.mapper.BaseMapper;
import com.fblog.core.dao.mapper.CategoryMapper;
import com.fblog.core.dao.mapper.PostMapper;
import com.fblog.core.utils.IDGenerator;
import com.fblog.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fansion on 18/01/2017.
 * 分类业务
 */
@Service
public class CategoryService extends BaseService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private PostMapper postMapper;

    @Override
    protected BaseMapper getMapper() {
        return categoryMapper;
    }

    @Transactional
    public boolean insertChildren(Category category, String parentName) {
        Category parent = loadByName(StringUtils.isEmpty(parentName) ? CategoryConstants.ROOT : parentName);
        //已经存在的兄弟个数
        int existSibling = parent.getRightv();
        category.setLeftv(existSibling);
        category.setRightv(existSibling + 1);
        //???
        categoryMapper.updateInsertLeftv(parent.getRightv());
        categoryMapper.updateInsertRightv(parent.getRightv());
        insert(category);
        return true;
    }

    public Category loadByName(String parentName) {
        return categoryMapper.loadByName(parentName);
    }

    @Transactional
    public boolean insertAfter(Category category, Category sibling) {
        category.setLeftv(sibling.getRightv() + 1);
        category.setRightv(sibling.getRightv() + 2);

        categoryMapper.updateInsertLeftv(sibling.getRightv());
        categoryMapper.updateInsertRightv(sibling.getRightv());
        insert(category);
        return true;
    }

    @Transactional
    public void remove(Category category) {
        int length = category.getRightv() - category.getLeftv() + 1;
    /* 注意:delete须第一个执行,因为updateDeleteLeftv会有影响 */
        categoryMapper.delete(category.getLeftv(), category.getRightv());
        categoryMapper.updateDeleteLeftv(category.getLeftv(), length);
        categoryMapper.updateDeleteRight(category.getRightv(), length);
    }

    public List<Category> loadChildren(Category category){
        return categoryMapper.loadChildren(category);
    }

    public void init(){
        Category  root = new Category();
        root.setId(IDGenerator.uuid19());
        root.setLeftv(1);
        root.setName(CategoryConstants.ROOT);
        root.setRightv(2);
        insert(root);
    }

}
