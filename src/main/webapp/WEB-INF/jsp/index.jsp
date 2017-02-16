<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE Html>
<html>
<head>
    <jsp:include page="front/frontBase.jsp"/>
</head>
<body style="margin-top: 70px;background-color: #eee">
<jsp:include page="./front/header.jsp"/>
<div id="blogApp" class="container-fluid">
    <%-- main --%>
    <div id="empty">
        <img src="${g.domain}/resource/img/loading.svg">
    </div>
</div>
<jsp:include page="front/bottom.jsp"/>
<jsp:include page="front/footer.jsp"/>

<script src="${g.domain}/resource/js/front/home_v1.2.js" type="text/javascript"></script>
</body>
</html>
