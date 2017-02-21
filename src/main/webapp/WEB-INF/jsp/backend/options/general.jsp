<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE Html>
<html>
<head>
    <jsp:include page="../common/base.jsp"/>
</head>
<body style="padding-top: 50px">
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
                <div class="panel-heading"><span class="icon glyphicon glyphicon-briefcase"></span>常规</div>
                <div class="panel-body">
                    <c:if test="${success}">
                        <div class="alert alert-success" style="padding: 10px 15px;">修改成功</div>
                    </c:if>
                    <form action="/backend/options/general" method="post" class="form-horizontal text-left" role="form">
                        <div class="form-group ${title!=null?'has-error':''}">
                            <label class="col-sm-3 col-md-3" for="title"><i>*</i>站点标题</label>
                            <div class="col-sm-6 col-md-7">
                                <input placeholder="站点标题" name="title" id="title" class="form-control" type="text"
                                       value="${form.title}">
                                <p class="help-block">${title}</p>
                            </div>
                        </div>

                        <div class="form-group ${subtitle!=null?'has-error':''}">
                            <label class="col-sm-3 col-md-3" for="subtitle"><i>*</i>副标题</label>
                            <div class="col-sm-6 col-md-7">
                                <input placeholder="副标题" name="subtitle" id="subtitle" class="form-control" type="text"
                                       value="${form.subtitle}">
                                <p class="help-block">${subtitle}</p>
                            </div>
                        </div>

                        <div class="form-group ${description!=null?'has-error':''}">
                            <label class="col-sm-3 col-md-3" for="description">站点描述</label>
                            <div class="col-sm-6 col-md-7">
                                <input placeholder="站点描述" name="description" id="description" class="form-control"
                                       type="text" value="${form.description}">
                                <p class="help-block">${description}</p>
                            </div>
                        </div>

                        <div class="form-group ${keywords!=null?'has-error':''}">
                            <label class="col-sm-3 col-md-3" for="keywords">站点keywords</label>
                            <div class="col-sm-6 col-md-7">
                                <input placeholder="本站关键字" name="keywords" id="keywords" class="form-control"
                                       type="text" value="${form.keywords}">
                                <p class="help-block">${keywords!=null?keywords:'填写本站关键字'}</p>
                            </div>
                        </div>

                        <div class="form-group ${weburl!=null?'has-error':''}">
                            <label class="col-sm-3 col-md-3" for="weburl"><i>*</i>站点地址(URL)</label>
                            <div class="col-sm-6 col-md-7">
                                <input placeholder="http://" name="weburl" id="weburl" class="form-control" type="text"
                                       value="${form.weburl}">
                                <p class="help-block">${weburl}</p>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 col-md-3" for="email">电子邮件</label>
                            <div class="col-sm-6 col-md-7">
                                <input placeholder="example@xx.com" name="email" id="email" class="form-control" type="text">
                                <p class="help-block">这个电子邮件地址仅为了管理方便而索要，例如新注册用户通知。</p>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 col-md-3">成员资格</label>
                            <div class="col-sm-6">
                                <div class="check-box">
                                    <label style="padding-left: 20px">
                                        <input type="checkbox" name="enableReg" readonly="readonly">任何人都可以注册
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 col-md-3">新用户默认角色</label>
                            <div class="col-sm-6 col-md-7">
                                <select class="form-control" name="defaultUserRole">
                                    <option>订阅者</option>
                                    <option>编辑</option>
                                    <option>作者</option>
                                    <option>投稿者</option>
                                    <option>管理员</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group" style="padding-top: 20px;">
                            <div class="col-sm-offset-3 col-md-2">
                                <button type="submit" class="btn btn-primary btn-block">保存修改</button>
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
