package com.fblog.core.utils;

import com.fblog.core.WebConstants;
import com.fblog.service.vo.OsInfo;
import com.fblog.web.support.WebContextFactory;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

/**
 * 常用工具方法
 */
public class Utility {

    public static OsInfo getCurrentOsInfo() {
        OsInfo info = new OsInfo();
        info.setOsName(System.getProperty("os.name"));
        info.setOsVersion(System.getProperty("os.version"));
        info.setJavaVersion(System.getProperty("java.version"));
        if (WebContextFactory.get()!=null && WebContextFactory.get().getRequest() != null) {
            ServletContext context = WebContextFactory.get().getRequest().getServletContext();
            info.setServerInfo(context.getServerInfo());
        }
        info.setTotalMemory(Runtime.getRuntime().totalMemory() / 1024 / 1024);
        return info;
    }

    public static Throwable getRootCause(Throwable t) {
        Throwable cause = null;
        while ((cause = t.getCause()) != null) {
            t = cause;
        }
        return t;
    }
/**
*去掉图片地址中的http域名前缀
*/
    public static List<String> extractImagePath(List<String> imgPaths){
        List<String> paths = new ArrayList<>();
        String domain = WebConstants.getDomain();
        for(String imgPath:imgPaths){
            if (imgPath.startsWith(domain)){
                paths.add(imgPath.substring(domain.length()));
            }
        }
        return paths;
    }

}
