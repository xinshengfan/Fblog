<%@ page pageEncoding="utf-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<meta name="keywords"
      content="${ptitle} <c:if test="${post!=null}"> ${post.category.name} <c:forEach var="tag" items="${post.tags}" begin="0">${tag},</c:forEach></c:if> ">
<meta name="description" content="${g.description} ${ptitle} <c:if test="${post!=null}">${post.excerpt}</c:if> ">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta http-equiv="X-UA-COMPATIBLE" content="IE=Edge,chrome=1">

<title>FBlog${ptitle!=null?' | ':''}${ptitle}</title>

<link rel="icon" href="${g.domain}/resource/img/myIcon.ico" type="image/x-icon"/>
<link rel="shortcut icon" href="${g.domain}/resource/img/myIcon.ico" type="image/x-icon"/>
<link rel="bookmark" href="${g.domain}/resource/img/myIcon.ico" type="image/x-icon"/>

<link rel="stylesheet" href="${g.domain}/resource/bootstrap-3.3.1/css/bootstrap-theme.min.css"/>
<link rel="stylesheet" href="${g.domain}/resource/bootstrap-3.3.1/css/bootstrap.min.css"/>
<link rel="stylesheet" href="${g.domain}/resource/Font-Awesome-3.2.1/css/font-awesome.min.css"/>
<link rel="stylesheet" href="${g.domain}/resource/css/client.css"/>

<script src="${g.domain}/resource/js/jquery-1.9.1.min.js"></script>
<script src="${g.domain}/resource/bootstrap-3.3.1/js/bootstrap.min.js"></script>
<script src="${g.domain}/resource/js/jquery.cookie.js"></script>
<script src="${g.domain}/resource/js/backend/base.admin.js"></script>
