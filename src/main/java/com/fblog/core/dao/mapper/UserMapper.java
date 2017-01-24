package com.fblog.core.dao.mapper;

import com.fblog.core.dao.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper{
    User loadByNameAndPassword(@Param("username")String username,@Param("password")String password);
}
