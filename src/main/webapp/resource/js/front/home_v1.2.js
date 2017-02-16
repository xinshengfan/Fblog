fblog.register("fblog.front");

$(function () {
    var emptyH = $(window).height() - 120;
    $('#empty').css('height', emptyH);
    $('#empty img').css('padding-top', emptyH / 3);
    //.collapse需要先激活
    $("#navbar").collapse({toggle: false});
    $(window).bind('hashchange', function () {
        // alert(window.location.href + "  hash:" + location.hash);
        if (location.hash.length == 0) {
            fblog.front.replaceContent("/home");
        }
    });
    loadHome();
});

loadHome = function () {
    var url = location.hash;
    url = url.length == 0 ? "/home" : url.substring(1);
    $.ajax({
        url: fblog.getWebDomain(url),
        type: 'GET',
        dataType: 'html',
        success: function (data) {
            $('#empty').hide();
            $('#blogApp').html(data);
        },
        complete: function () {
            $("#pload").remove();
        }
    });
}

fblog.front.search = function () {
    fblog.front.replaceContent("/home?word=" + $('#search_word').val());
};

fblog.front.replaceContent = function (url) {
    if (url != "/home") {
        location.hash = "#" + url;
    }
    $.ajax({
        url: fblog.getWebDomain(url),
        type: 'GET',
        dataType: 'html',
        beforeSend: function () {
            $("body").append('<div id="pload" style="position:fixed;top:70px;z-index:1200;' +
                'background:url(/resource/img/reload.svg) top center no-repeat;width:100%;height:50px;margin:auto auto;"></div>');
        },
        success: function (data) {
            $('#blogApp').html(data);
            $(document).attr("title", $("#post_title").html());
        },
        complete: function () {
            $("#pload").remove();
            //合上菜单栏
            $("#navbar").collapse('hide');
            //滚动到顶部
            $('body,html').animate({scrollTop: 0}, 800);
        }
    });
};
