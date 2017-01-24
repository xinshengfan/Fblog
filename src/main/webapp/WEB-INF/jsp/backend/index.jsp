<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE Html>
<html>
<head>
    <jsp:include page="common/base.jsp" flush="false"/>
</head>
<body style="margin-top: 50px">
<jsp:include page="common/navbar.jsp" flush="false"/>
<div class="container-fluid">
    <div class="row">
        <%-- 左边的菜单栏，小屏占3/12 --%>
        <div class="col-sm-3 col-md-2" id="sidebar" style="padding: 0;">
            <jsp:include page="common/sidebar.jsp" flush="false"/>
        </div>
        <%-- 右侧内容，小屏9/12 --%>
        <div class="col-sm-9 col-md-10">
            <h3 class="page-header header">主菜单</h3>
            <%-- 先是四个大的图标，用于显示数据状态 --%>
            <div class="row">
                <div class="col-sm-3 col-md-3">
                    <div class="databox">
                        <div class="sybomol sybomol_terques"><i class="glyphicon glyphicon-user"></i></div>
                        <div class="value">3<p>用户</p></div>
                    </div>
                </div>
                <div class="col-sm-3 col-md-3">
                    <div class="databox">
                        <div class="sybomol symbomol_red "><i class="glyphicon glyphicon-pencil"></i></div>
                        <div class="value">3<p>文章</p></div>
                    </div>
                </div>
                <div class="col-sm-3 col-md-3">
                    <div class="databox">
                        <div class="sybomol sybomol_yellow"><i class="glyphicon glyphicon-comment"></i></div>
                        <div class="value">13<p>评论</p></div>
                    </div>
                </div>
                <div class="col-sm-3 col-md-3">
                    <div class="databox">
                        <div class="sybomol sybomol_blue"><i class="glyphicon glyphicon-download-alt"></i></div>
                        <div class="value">12<p>附件</p></div>
                    </div>
                </div>
            </div>
            <%-- 再起一行， 放置系统信息及最近发表和留言--%>
            <div class="row" style="padding-top: 15px">
                <div class="col-sm-6 col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading"><span class="icon glyphicon glyphicon-certificate"></span>系统信息</div>
                        <div class="panel-body">
                            <ul class="list-unstyled ul-group">
                                <li>操作系统：${osInfo.osName}&nbsp;${osInfo.osVersion}</li>
                                <li>服务器：${osInfo.serverInfo}</li>
                                <li>Java环境：${osInfo.javaVersion}</li>
                                <li>系统内存：${osInfo.totalMemory}</li>
                            </ul>
                        </div>
                    </div>
                </div>
                <%-- 最近发表 --%>
                <div class="col-sm-6 col-md-6" style="float: right">
                    <div class="panel panel-default">
                        <div class="panel-heading"><span class="icon glyphicon glyphicon-filter"></span>最近发表</div>
                        <div class="list-group">
                            <c:forEach items="${posts}" var="post">
                                <a class="list-group-item" href="../posts/$${post.id}" target="_blank"><span
                                        class="badge">${post.rcount}</span>
                                    <h4 class="list-group-item-heading">${post.title}</h4>
                                    <p><fmt:formatDate value="${post.createTime}" pattern="YYYY-MM-dd"/></p>
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <%-- 留言板 --%>
                <div class="col-sm-6 col-md-6" style="float: right">
                    <div class="panel-default">
                        <div class="panel-heading"><span class="icon glyphicon glyphicon-comment"></span> 近期留言</div>
                        <ul class="list-group">
                            <li class="list-group-item">
                                <span class="badge">2017-01-12</span>很好，好不错的样子！
                            </li>
                            <li class="list-group-item">
                                <span class="badge">2017-01-12</span>好好来，好好干！
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
