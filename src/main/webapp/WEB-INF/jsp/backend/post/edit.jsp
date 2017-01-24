<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="z" uri="/WEB-INF/tld/function.tld" %>
<!DOCTYPE Html>
<html>
<head>
    <jsp:include page="../common/base.jsp"/>
    <script type="text/javascript" src="${g.domain}/resource/ueditor-1.4.3/ueditor.config.js"></script>
    <script type="text/javascript" src="${g.domain}/resource/ueditor-1.4.3/ueditor.all.js"></script>
    <script type="text/javascript" src="${g.domain}/resource/epiceditor/js/epiceditor.min.js"></script>
</head>
<body style="margin-top: 50px">
<jsp:include page="../common/navbar.jsp"/>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2" style="padding: 0">
            <jsp:include page="../common/sidebar.jsp"/>
        </div>
        <div class="col-sm-9 col-md-10">
            <ol class="breadcrumb header">
                <li><span class="icon glyphicon glyphicon-home"></span>主菜单</li>
                <li>文章</li>
                <li class="active">编辑文章</li>
            </ol>

            <div class="row">
                <div class="col-sm-9 col-md-9">
                    <div class="panel panel-default">
                        <div class="panel-heading"><span class="icon glyphicon glyphicon-edit"></span>标题/内容 </div>
                        <div class="panel-body">
                            <input type="hidden" id="postid" value="${post.id}">
                            <input type="text" id="title" class="form-control input-md" placeholder="输入标题" value="${post.title}"><br/>
                            <ul class="nav nav-tabs nav-justified" id="editor-nav">
                                <li ><a href="#editor-mk">MarkDown</a> </li>
                                <li class="active"><a href="#editor-ue">UEditor</a> </li>
                                <li><a href="#editor-txt">纯文本</a> </li>
                            </ul>
                            <%-- 利用tab-content和tab-pane来控制标签与对应的面板切换 --%>
                            <div class="tab-content">
                                <div class="tab-pane" id="editor-mk"><div id="epiceditor"></div> </div>
                                <div class="tab-pane active" id="editor-ue">
                                    <script id="ueditor" style="width:100%;height:350px" type="text/plain">${post.content}</script>
                                </div>
                                <div class="tab-pane" id="editor-txt">
                                    <textarea id="editor-txt-tt" style="width: 100%;height: 400px">${post.content}</textarea>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer text-success">注:此三种编辑模式相互独立,最终以当前选中标签页内容提交</div>
                    </div>
                </div>

                <div class="col-sm-3 col-md-3">
                    <div class="panel panel-default">
                        <div class="panel-heading">发布</div>
                        <div class="panel-body">
                            <div class="form-group">
                                <label for="category">分类</label>
                                <select class="form-control" id="category">
                                    <c:forEach items="${categorys}" var="category" begin="1">
                                        <option value="${category.id}" ${post.category.id==category.id?'selected':''}>
                                            ├─<c:if test="${category.level==3}">└─</c:if>${category.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="open">公开度</label><br>
                                <label class="radio-inline">
                                    <input type="radio" name="pstatus"value="publish ${post.pstatus=='publish'?'checked':''}" id="open">公开
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="pstatus"value="secret ${post.pstatus=='secret'?'checked':''}">隐藏
                                </label>
                            </div>

                            <div class="form-group">
                                <label for="tags">标签</label><br>
                                <input type="text" class="form-control" name="tag" id="tags" placeholder="标签..."  value="${z:join(post.tags,',')}">
                                <span class="help-block">多个标签用英文逗号隔开</span>
                            </div>

                            <div class="form-group">
                                <label for="comment">是否允许评论</label><br>
                                <label class="radio-inline">
                                    <input type="radio" name="cstatus" value="open ${post.cstatus=='open'?'checked':''}" id="comment">是
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="cstatus" value="close ${post.cstatus=='close'?'checked':''}" >否
                                </label>
                            </div>

                            <div class="panel-footer">
                                <button type="button" class="btn btn-primary btn-block" onclick="fblog.post.insert()">发布</button>
                            </div>

                        </div>
                    </div>
                </div>

            </div>

        </div>
    </div>
</div>
<script type="text/javascript" src="${g.domain}/resource/js/backend/admin.post.js"></script>
</body>
</html>
