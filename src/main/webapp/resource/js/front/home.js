fblog.register("fblog.home");

$(function (category) {
    $(".blog-category img").setAttribute("src",fblog.home.getImg(category));
});

fblog.home.getImg = function (category) {
    switch (category) {
        case "android":
            return "/resource/img/android.svg";
        case "ios":
            return "/resource/img/ios.svg";
        case "web":
            return "/resource/img/web.svg";
        case "note":
            return "/resource/img/note.svg";
    }
}

