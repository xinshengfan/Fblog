<%--
  Created by IntelliJ IDEA.
  User: fansion
  Date: 26/12/2016
  Time: 10:45 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" isErrorPage="true" %>
<html>
<head>
    <script src="/resource/js/jquery-1.9.1.min.js" type="text/javascript"></script>
</head>
<body>
<div style="margin: 0 auto;text-align: center;max-width: 800px">
    <h1 style="color: red">页面发生错误：</h1>
    <p class="message"><h2>${msg}</h2></p>
    <p><a href="${g.domain}/"><< 返回主页 </a></p>
</div>
</body>
</html>
