<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-fixed-top" role="navigation" style="background-color: white">
    <div class="container header-title">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <div class="navbar-header logo">
                <a href="#"><img class="img-responsive" src="/resource/img/me.jpg"></a>
            </div>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="menus nav navbar-nav ">
                <li><a class="navbar-brand" href="${g.domain}" style="margin-left: 20px">FBlog</a></li>
                <li><a href="#" onclick="fblog.front.replaceContent('/category/android')"><img class="icon-category"
                                                                src="/resource/img/android.svg"><span>Android</span></a>
                </li>
                <li><a href="#" onclick="fblog.front.replaceContent('/category/ios')"><img class="icon-category"
                                                            src="/resource/img/ios.svg"><span>iOS</span></a>
                </li>
                <li><a href="#" onclick="fblog.front.replaceContent('/category/web')"><img class="icon-category" src="/resource/img/web.svg"></i>
                    <span>Web</span></a>
                </li>
                <li><a href="#" onclick="fblog.front.replaceContent('/category/note')"><img class="icon-category" src="/resource/img/note.svg"></i>
                    <span>笔记</span></a>
                </li>
            </ul>
            <form class="navbar-form navbar-right search_area" onsubmit="return false;">
                <div class="search_input">
                    <input id="search_word" type="text" autocomplete="off" name="word" placeholder="搜索一下" value="${search}"/>
                </div>
                <div class="search_button">
                    <input type="submit" value="" onclick="fblog.front.search();"/>
                </div>
            </form>
        </div>
    </div>
</nav>
