package com.fblog.web.support;

public class WebContextFactory {
    //用于解决线程中相同变量的访问冲突，ThreadLocal
    private static final ThreadLocal<WebContext> WEB_CONTEXT_HOLDER = new ThreadLocal<>();

    private WebContextFactory(){}

    public static WebContext get(){
        return WEB_CONTEXT_HOLDER.get();
    }

    public static void set(WebContext context){
        WEB_CONTEXT_HOLDER.set(context);
    }

    public static void remove(){
        WEB_CONTEXT_HOLDER.set(null);
    }

}
