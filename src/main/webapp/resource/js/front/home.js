fblog.register("fblog.front");

$(function () {
    $('#empty').css('height',$(window).height()-180);
    $('#empty img').css('padding-top',20);
    $.ajax({
        url: fblog.domain("/home"),
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
});

fblog.front.search = function () {
    fblog.front.replaceContent("/home?word=" + $('#search_word').val());
};

fblog.front.replaceContent = function (url) {
    $.ajax({
        url: fblog.domain(url),
        type: 'GET',
        dataType: 'html',
        beforeSend: function () {
            $("body").append('<div id="pload" style="position:fixed;top:70px;z-index:1200;' +
                'background:url(/resource/img/reload.svg) top center no-repeat;width:100%;height:50px;margin:auto auto;"></div>');
        },
        success: function (data) {
            $('#blogApp').html(data);
        },
        complete: function () {
            $("#pload").remove();
        }
    });
};