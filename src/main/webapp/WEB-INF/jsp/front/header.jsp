<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header class="blog-header">
    <div class="container-fluid clearfix">
        <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
            <div class="header-title">
                <div class="navbar-header logo">
                    <a href="#"><img id="logo-icon" src="/resource/img/me.jpg"></a>
                </div>
                <div class="navbar-collapse collapse">
                    <a class="navbar-brand" target="_blank" href="${g.domain}">FBlog</a>
                    <ul class="nav navbar-nav menus">
                        <li><a href="${g.domain}/category/android"><img class="icon-category"
                                             src="/resource/img/android.svg"><span>Android</span></a></li>
                        <li><a href="${g.domain}/category/ios"><img class="icon-category" src="/resource/img/ios.svg"><span>iOS</span></a>
                        </li>
                        <li><a href="${g.domain}/category/web"><img class="icon-category" src="/resource/img/web.svg"></i><span>Web</span></a>
                        </li>
                        <li><a href="${g.domain}/category/note"><img class="icon-category" src="/resource/img/note.svg"></i><span>笔记</span></a>
                        </li>
                    </ul>
                    <div id="search_area">
                        <form method="get" action="" onsubmit="alert('正在开发，敬请期待！');">
                            <div class="search_input">
                                <input type="text" autocomplete="off" name="word" placeholder="搜索一下"/>
                            </div>
                            <div class="search_button">
                                <input type="submit" value="Search"/>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </nav>
    </div>
</header>