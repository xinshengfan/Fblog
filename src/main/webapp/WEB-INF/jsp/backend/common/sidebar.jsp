<%--
  Created by IntelliJ IDEA.
  User: fansion
  Date: 17/01/2017
  Time: 3:29 PM
  后台编辑页面菜单栏
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<ul class="side-menu">
    <li><a class="nav-header" href="#">
        <i class="left glyphicon glyphicon-home" aria-hidden="true"></i>
        系统设置
        <i class="right glyphicon glyphicon-chevron-down" aria-hidden="true"></i>
    </a>
        <ul class="sub-menu" style="display: block">
            <li><a href="${g.domain}/backend/index">首页</a></li>
            <li><a href="${g.domain}/backend/options/general">常规选项</a></li>
            <li><a href="${g.domain}/backend/options/post">撰写/阅读</a></li>
            <li><a class="last" href="${g.domain}/backend/options/email">邮件评论</a></li>
        </ul>
    </li>

    <shiro:hasAnyRoles name="admin,editor">
        <li><a class="nav-header" href="#">
            <i class="left glyphicon glyphicon-send" aria-hidden="true"></i>
            文章
            <i class="right glyphicon glyphicon-chevron-up" aria-hidden="true"></i>
        </a>
            <ul class="sub-menu">
                <li><a href="${g.domain}/backend/posts/edit">写文章</a></li>
                <li><a href="${g.domain}/backend/posts">所有文章</a></li>
                <li><a class="last" href="${g.domain}/backend/categorys">文章分类</a></li>
            </ul>
        </li>


        <li>
            <a class="nav-header" href="#">
                <i class="left glyphicon glyphicon-film" aria-hidden="true"></i>
                多媒体
                <i class="right glyphicon glyphicon-chevron-up" aria-hidden="true"></i>
            </a>
            <ul class="sub-menu">
                <li><a href="${g.domain}/backend/wait">媒体库</a></li>
                <li><a class="last" href="${g.domain}/backend/wait">添加</a></li>
            </ul>
        </li>
    </shiro:hasAnyRoles>

    <shiro:hasRole name="admin">
        <li>
            <a class="nav-header" href="#">
                <i class="icon glyphicon glyphicon-file" aria-hidden="true"></i>
                页面
                <i class="right glyphicon glyphicon-chevron-up" aria-hidden="true"></i>
            </a>
            <ul class="sub-menu">
                <li><a href="${g.domain}/backend/wait">所有页面</a></li>
                <li><a class="last" href="${g.domain}/backend/wait">新建页面</a></li>
            </ul>
        </li>

        <li>
            <a class="nav-header" href="#">
                <i class="left glyphicon glyphicon-comment" aria-hidden="true"></i>
                评论
            </a>
        </li>

        <li>
            <a class="nav-header" href="#">
                <i class="left glyphicon glyphicon-link" aria-hidden="true"></i>
                链接
                <i class="right glyphicon glyphicon-chevron-up" aria-hidden="true"></i>
            </a>
            <ul class="sub-menu">
                <li><a href="${g.domain}/backend/wait">添加</a></li>
                <li><a class="last" href="${g.domain}/backend/wait">全部链接</a></li>
            </ul>
        </li>
    </shiro:hasRole>

    <shiro:authenticated>
        <li>
            <a class="nav-header" href="#">
                <i class="left glyphicon glyphicon-user" aria-hidden="true"></i>
                用户
                <i class="right glyphicon glyphicon-chevron-up" aria-hidden="true"></i>
            </a>
            <shiro:hasRole name="admin">
                <ul class="sub-menu">
                    <li><a href="${g.domain}/backend/users">所有用户</a></li>
                    <li><a href="${g.domain}/backend/users/edit">添加用户</a></li>
                    <li><a class="last" href="${g.domain}/backend/users/my">我的个人资料</a></li>
                </ul>
            </shiro:hasRole>
        </li>
    </shiro:authenticated>

    <shiro:hasRole name="admin">
        <li>
            <a class="nav-header" href="#">
                <i class="left glyphicon glyphicon-wrench" aria-hidden="true"></i>
                工具
                <i class="right glyphicon glyphicon-chevron-up" aria-hidden="true"></i>
            </a>
            <ul class="sub-menu">
                <li><a href="${g.domain}/backend/tool/ehcache">缓存监控</a></li>
                <li><a href="${g.domain}/backend/wait">导入</a></li>
                <li><a class="last" href="${g.domain}/backend/wait">导出</a></li>
            </ul>
        </li>
    </shiro:hasRole>
</ul>

<script type="text/javascript" language="JavaScript">
    $(".nav-header").each(function () {
        var _this = $(this);
        _this.click(function () {
            var subMenu = _this.siblings();
            if (subMenu.length == 0) {
                return;
            }
            if (subMenu.css("display") == "none") {
                subMenu.slideDown();
                _this.find(":last").removeClass("glyphicon-chevron-up");
                _this.find(":last").addClass("glyphicon-chevron-down");
            } else {
                subMenu.slideUp();
                _this.find(":last").removeClass("glyphicon-chevron-down");
                _this.find(":last").addClass("glyphicon-chevron-up");
            }
        });
    });
</script>
