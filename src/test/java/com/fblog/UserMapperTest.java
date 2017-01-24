package com.fblog;

import com.fblog.core.dao.entity.User;
import com.fblog.core.dao.entity.PageModel;
import com.fblog.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@org.junit.runner.RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:config/spring-*.xml")
public class UserMapperTest {
    @Autowired
    private UserService userService;

    @Test
    public void testList(){
        PageModel<User> userPageModel =  userService.list(1,10);
        Assert.assertNull(userPageModel);
        System.out.print("userModel:"+userPageModel.getContent().size());
    }

}
