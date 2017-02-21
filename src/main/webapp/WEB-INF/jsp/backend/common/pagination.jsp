<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="page" uri="/WEB-INF/tld/pagination.tld" %>
<ul class="pagination">
    <page:page model="${page}" pageUrl="" showPage="8">
        <page:prev>
            <li><a href="${pageUrl}"><span aria-hidden="true"><<</span></a></li>
        </page:prev>
        <page:pager>
            <c:choose>
                <c:when test="${pageNumber==page.pageIndex}">
                    <li><a href="javascript:void(0);">${pageNumber}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${pageUrl}">${pageNumber}</a></li>
                </c:otherwise>
            </c:choose>
        </page:pager>
        <page:next>
            <li><a href="${pageUrl}">>></a></li>
        </page:next>
    </page:page>
</ul>
