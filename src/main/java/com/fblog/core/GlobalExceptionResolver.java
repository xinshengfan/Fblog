package com.fblog.core;

import com.fblog.core.utils.LogUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fansion on 18/01/2017.
 * 全局异常处理，只在测试时使用
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView model;
        if (e instanceof UnauthenticatedException) {
            //还未验证异常，需要重新登录
            model = new ModelAndView("/backend/login");
            model.addObject("msg","请重新登录");
        } else {
            model = new ModelAndView("error");
            model.addObject("msg", e);
            LogUtils.e("GlobalExceptionResolver", "访问" + httpServletRequest.getRequestURI() + " , 发生了错误：" + e);
        }
        return model;
    }
}
