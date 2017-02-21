<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="main-container">
    <span id="post_title" hidden="hidden">Xinping${ptitle!=null?' | ':''}${ptitle}</span>
    <section class="blog-box-container">
        <div class="blog-timeline-box">
            <div class="blog-timeline">
                <a href="#">
                    <div class="blog-category"><img class="img-responsive" src="${post.category.imgPath}"></div>
                </a>
                <div class="blog-time"><fmt:formatDate value="${post.createTime}" pattern="MM月dd日"/></div>
            </div>
        </div>

        <div class="blog-concise-box">
            <div class="blog-concise">
                <div class="blog-title"><a onclick="fblog.front.replaceContent('/post/${post.id}')">${post.title}</a></div>
                <div class="ellipsis-text blog-content mui-ellipsis-3">
                    ${post.content}
                </div>

                <c:if test="${prev!=null || next!=null}">
                    <ul class="post_pre list-unstyled">
                        <c:if test="${prev!=null}">
                            <li><a onclick="fblog.front.replaceContent('/post/${prev.id}')"><img
                                    src="/resource/img/up1.svg"><span>${prev.title}</span></a>
                            </li>
                        </c:if>
                        <c:if test="${next!=null}">
                            <li><a onclick="fblog.front.replaceContent('/post/${next.id}')">
                                <img
                                        src="/resource/img/down1.svg"><span>${next.title}</span></a>
                            </li>
                        </c:if>
                    </ul>
                </c:if>

                <div class="blog-stat">
                    <a href="#"><img class="icon-category"
                                     src="/resource/img/read.svg"><span>${post.rcount}</span></a>
                </div>
            </div>
        </div>
    </section>
</div>