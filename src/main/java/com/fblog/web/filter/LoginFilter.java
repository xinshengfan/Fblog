package com.fblog.web.filter;

import com.fblog.core.Constants;
import com.fblog.core.WebConstants;
import com.fblog.core.utils.LogUtils;
import com.fblog.core.utils.ServletUtils;
import com.fblog.core.utils.Utility;
import com.fblog.service.shiro.StatelessToken;
import com.fblog.service.vo.Global;
import com.fblog.web.support.CookieRemeberManager;
import com.fblog.web.support.WebContext;
import com.fblog.web.support.WebContextFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义过滤器,继承OncePerRequestFilter保证一次请求只过滤一次(以兼容不同的servlet container
 * <p>
 * 作用是登录过滤，并保存上下文，在登录后转发到上的地
 */
public class LoginFilter extends OncePerRequestFilter {
    private static final String TAG = "LoginFilter";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        WebContext context = WebContextFactory.get();
        if (context != null) {
            return;
        }
        try {
            //处理此处的异常
            context = new WebContext(httpServletRequest, httpServletResponse);
            context.setUser(CookieRemeberManager.extractValidRemeberMeCoolieUser(httpServletRequest, httpServletResponse));
//            //保存上下文
            WebContextFactory.set(context);
//            //入口控制
            accessControl();
//            //ajax处理
            boolean isAjax = ServletUtils.isAjax(httpServletRequest);
            if (!isAjax) {
                httpServletRequest.setAttribute("g", new Global(ServletUtils.getDomain(httpServletRequest)));
            }
//            //设置域名domain
            WebConstants.setDomain(ServletUtils.getDomain(httpServletRequest));

            //放行
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            if (ServletUtils.isAjax(httpServletRequest)) {
                LogUtils.e(TAG, e);
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setCharacterEncoding(Constants.ENCODING_UTF_8.name());
                httpServletResponse.getWriter().write("{'status':'500','success':false,'msg':'操作失败,服务端出错'}");
            } else {
                handleException(Utility.getRootCause(e), httpServletRequest, httpServletResponse);
            }
        } finally {
            WebContextFactory.remove();
            cleanup();
        }
    }

    /**
     * 入口控制
     */
    private void accessControl() {
        WebContext context = WebContextFactory.get();
        if (context.isLogon()) {
            //使用shiro来验证登录
            SecurityUtils.getSubject().login(new StatelessToken(context.getUser().getId(), context.getUser().getPassword()));
        }
    }

    private void handleException(Throwable cause, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (cause instanceof AuthorizationException) {
            //用户验证异常
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ServletUtils.sendRedirect(httpServletResponse, "/WEB-INF/jsp/backend/login.jsp");
        } else {
            LogUtils.e(TAG, cause);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ServletUtils.sendRedirect(httpServletResponse, "/resource/error/500.html");
        }
    }

    /**
     * 此处需要unbind,同见
     * {@link org.apache.shiro.web.servlet.AbstractShiroFilter#doFilterInternal}中
     * subject.execute(..) ,不能Subject.logout()(并未移除ThreadContext中的Subject对象)
     *
     * @see <a href="http://shiro.apache.org/subject.html">Subject</a>
     */
    private void cleanup() {
        // TODO：暂不加shiro，2017.01.16
//        ThreadContext.unbindSubject();
    }


}
