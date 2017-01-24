package com.fblog.web.support;

import com.fblog.core.dao.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebContext {
    private String sid;
    private int longId;
    private User user;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public WebContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getLongId() {
        return longId;
    }

    public void setLongId(int longId) {
        this.longId = longId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public boolean isLogon(){
        return user != null;
    }
}
