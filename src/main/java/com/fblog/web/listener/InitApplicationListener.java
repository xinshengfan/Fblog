package com.fblog.web.listener;

import com.fblog.core.WebConstants;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
自定义系统监听器
 */
public class InitApplicationListener implements ServletContextListener{
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
      //      取出servlet的的路径
        WebConstants.APPLICATION_PATH = servletContextEvent.getServletContext().getRealPath("/");
        /* 给log4j设置环境变量，必须要在jvm加载log4j.properties前设置 */
        System.setProperty("log4jHome", WebConstants.APPLICATION_PATH);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
