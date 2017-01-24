<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:include page="common/base.jsp"/>
</head>
<body  style="background: #eee">
<div class="container-fluid">

    <div id="login">
        <form class="form-group" id="loginForm" method="post" role="form" action="/backend/login">
            <h2>FBlog login</h2>
            <c:if test="${msg!=null}"><p class="message">${msg}<br></p></c:if>
            <div class="form-group">
                <div class="input-group">
                    <div class="input-group-addon"><i class="icon-user"></i></div>
                    <input type="text" class="form-control" autocomplete="off" name="username"
                           placeholder="用户名/邮箱">
                </div>
            </div>

            <div class="form-group">
                <div class="input-group">
                    <div class="input-group-addon"><i class="icon-key"></i></div>
                    <input class="form-control" type="password" autocomplete="off" name="password"
                           placeholder="密码">
                </div>
            </div>

            <div class="check-box">
                <label><input type="checkbox" name="remeber" style="margin-right: 10px"><i style="color: #555">记住我的登录信息</i> </label>
            </div>

            <div class="form-group" style="margin-top: 5px">
                <a style="width: 40%;text-align: left" href="${g.domain}/"><< 返回 </a>
                <button class="btn btn-margin btn-primary" type="submit" style="width: 60%;margin-left: 25px">登录</button>
            </div>

        </form>
    </div>
</div>
</body>
</html>
