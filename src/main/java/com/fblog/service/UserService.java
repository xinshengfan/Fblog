package com.fblog.service;

import com.fblog.core.dao.entity.User;
import com.fblog.core.dao.mapper.BaseMapper;
import com.fblog.core.dao.mapper.UserMapper;
import com.fblog.core.dao.entity.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService{

    @Autowired
    private UserMapper userMapper;

    public User login(String username,String password){
        return userMapper.loadByNameAndPassword(username, password);
    }

    public PageModel<User> list(int pageIndex,int pageSize){
        PageModel<User> model = new PageModel<User>(pageIndex, pageSize);
        super.list(model);
        return model;
    }

    @Override
    protected BaseMapper getMapper() {
        return userMapper;
    }
}
