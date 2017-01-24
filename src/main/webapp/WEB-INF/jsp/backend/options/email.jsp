<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE Html>
<html>
<head>
    <jsp:include page="../common/base.jsp"/>
</head>
<body style="margin-top: 50px;">
<jsp:include page="../common/navbar.jsp"/>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2" id="sidebar" style="padding: 0">
            <jsp:include page="../common/sidebar.jsp"/>
        </div>
        <div class="col-sm-9 col-md-10">
            <ol class="breadcrumb header">
                <li><span class="icon glyphicon glyphicon-home"></span>主菜单</li>
                <li>系统设置</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-heading"><span class="icon glyphicon glyphicon-briefcase"></span>邮件服务器</div>
                <div class="panel-body">
                    <c:if test="${success}">
                        <div class="alert alert-success" style="padding: 10px 15px">修改成功</div>
                    </c:if>
                    <form action="" method="post" role="form" class="form-horizontal text-left">
                        <div class="form-group ${host!=null?'has-error':''}">
                            <label class="col-sm-3" for="host"><i>*</i>主机Host</label>
                            <div class="col-sm-6">
                                <input class="form-control" placeholder="host" id="host" type="text" name="host"
                                       value="${form.host}">
                                <p class="help-block">${host}</p>
                            </div>
                        </div>

                        <div class="form-group ${port!=null?'has-error':''}">
                            <label class="col-sm-3" for="port"><i>*</i>端口port</label>
                            <div class="col-sm-6">
                                <input class="form-control" placeholder="port" id="port" type="text" name="port"
                                       value="${form.port}">
                                <p class="help-block">${port}</p>
                            </div>
                        </div>

                        <div class="form-group ${username!=null?'has-error':''}">
                            <label class="col-sm-3" for="username"><i>*</i>用户名</label>
                            <div class="col-sm-6">
                                <input class="form-control" placeholder="username" id="username" type="text"
                                       name="username"
                                       value="${form.username}">
                                <p class="help-block">${username}</p>
                            </div>
                        </div>

                        <div class="form-group ${password!=null?'has-error':''}">
                            <label class="col-sm-3" for="password"><i>*</i>密码</label>
                            <div class="col-sm-6">
                                <input class="form-control" placeholder="password" id="password" type="password"
                                       name="password"
                                       value="${form.password}">
                                <p class="help-block">${password}</p>
                            </div>
                        </div>

                        <div class="form-group" style="padding-top: 20px">
                            <div class="col-sm-offset-3 col-sm-2">
                                <button class="btn btn-primary btn-block" type="submit">保存更改</button>
                            </div>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
