<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav class="navbar navbar-inverse navbar-fixed-top"role="navigation">
    <div class="container-fluid" style="padding-right: 15px">
        <div class="navbar-header">
            <a class="navbar-brand" target="_blank" href="${g.domain}">FBlog</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a class="dropdown-toggle" data-toggle="dropdown" href="#">新建<span class="caret"/></a>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="${g.domain}/backend/posts/edit" >文章</a></li>
                    <li><a href="${g.domain}/backend/wait">链接</a> </li>
                    <li><a href="${g.domain}/backend/users/edit">用户</a> </li>
                </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">Dashboard</a> </li>
                <li><a class="dropdown-toggle" data-toggle="dropdown" href="#">你好,User<span class="caret"/></a>
                <ul class="dropdown-menu">
                    <li><a href="${g.domain}/backend/options/general">设置</a> </li>
                    <li><a href="${g.domain}/backend/users/my">编辑我的个人资料</a> </li>
                    <li><a href="${g.domain}/backend/logout">退出</a> </li>
                </ul>
                </li>
            </ul>
            <form action="#" class="navbar-form navbar-right">
                <input type="text"autocomplete="off" placeholder="Search..." class="form-control">
            </form>
        </div>
    </div>
</nav>