<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="page" uri="/WEB-INF/tld/pagination.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="main-container">
    <span id="post_title" hidden="hidden">FBlog${ptitle!=null?' | ':''}${ptitle}</span>
    <c:if test="${page.content.size()==0}">
        <section class="blog-box-container">
            <div class="blog-concise-box">
                <div class="blog-concise">
                    <div class="ellipsis-text blog-content mui-ellipsis-3">
                        <em style="color: gray">暂时没有内容</em>
                    </div>
                </div>
            </div>
        </section>
    </c:if>
    <c:forEach items="${page.content}" var="post">
        <section class="blog-box-container">
            <div class="blog-timeline-box">
                <div class="blog-timeline">
                    <a href="#">
                        <div class="blog-category"><img class="icon-category" src="${post.category.imgPath}"></div>
                    </a>
                    <div class="blog-time"><fmt:formatDate value="${post.createTime}" pattern="MM月dd日"/></div>
                </div>
            </div>

            <div class="blog-concise-box">
                <div class="blog-concise">
                    <div class="blog-title"><a
                            onclick="fblog.front.replaceContent('/post/${post.id}')">${post.title}</a></div>
                    <div class="ellipsis-text blog-content mui-ellipsis-3">
                            ${post.excerpt}
                    </div>
                    <div class="blog-stat">
                        <a href="#"><img class="icon-category"
                                         src="/resource/img/read.svg"><span>${post.rcount}</span></a>
                    </div>
                </div>
            </div>
        </section>
    </c:forEach>
    <div class="blog-box-pager">
        <div class="pager clearfix">
            <ul class="paging pagination">
                <page:page model="${page}" pageUrl="${request.requestURL}" showPage="9" boundary="2">
                    <page:prev>
                        <li><a onclick="fblog.front.replaceContent('/home${pageUrl}')"><<</a></li>
                    </page:prev>
                    <page:pager>
                        <c:choose>
                            <c:when test="${dot}">
                                <li><span class="dots">…</span></li>
                            </c:when>
                            <c:when test="${pageNumber==page.pageIndex}">
                                <li class="active"><a href="javascript:void(0);">${pageNumber}</a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a onclick="fblog.front.replaceContent('/home${pageUrl}')">${pageNumber}</a></li>
                            </c:otherwise>
                        </c:choose>
                    </page:pager>
                    <page:next>
                        <li><a onclick="fblog.front.replaceContent('/home${pageUrl}')">>></a></li>
                    </page:next>
                </page:page>
            </ul>
        </div>
    </div>
</div>
