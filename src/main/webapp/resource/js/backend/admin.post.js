fblog.register("fblog.post");

$(function () {
    $("#editor-nav a").click(function (e) {
        console.log("a 标签点击事件，进行响应");
        e.preventDefault();//阻止模态展示
        $(this).tab("show");
        fblog.post.editType = $(this).attr("href").substring(8);
    });
    // if (!document.getElementById("#ueditor")) return;
    fblog.post.editType = "ue";
    fblog.post.ueditor = UE.getEditor('ueditor', {
        /* 阻止div标签自动转换为p标签 */
        allowDivTransToP: false,
        autoHeightEnabled: true,
        autoFloatEnabled: true
    });
    fblog.post.ueditor.ready(function () {
        fblog.post.ueditor.execCommand('')
    });
    fblog.post.epiceditor = new EpicEditor({
        basePath: window.location.protocol + "//" + window.location.port + window.location.host + "/resource/epiceditor",
        userNativeFullScreen: false,
        clientSideStorage: false,
        bar: 'show',
        file: {
            defaultContent: $("#editor-txt-tt").val(),
            autoSave: false
        },
        autogrow: {
            minHeight: 400,
            maxHeight: 600
        }
    });
    fblog.post.epiceditor.load();
});

//文章插入
fblog.post.insert = function () {
    var title = $.trim($("#title").val());
    //需要先输入标题
    if (title == "") {
        $("#title").focus();
        return;
    }
    var getText = function () {
        var result;
        switch (fblog.post.editType) {
            case "ue":
                result = fblog.post.ueditor.getContent();
                break;
            case "txt":
                result = $("#editor-txt-tt").val();
                break;
            case "mk":
                result = fblog.post.epiceditor.getElement('previewer').body.innerHTML;
                break;
            default:
                result = "";
                break;
        }
        return result;
    }
    //编辑的文章上传
    var postid = $("#postid").val();
    var data = {
        title: title,
        content: getText(),
        tags: $("#tags").val(),
        categoryid: $("#category").val(),
        pstatus: $("input:radio[name=pstatus]:checked").val(),
        cstatus: $("input:radio[name=cstatus]:checked").val()
    };
    //文章的id有效
    if (postid.length > 0) {
        data.id = postid;
    }
    //使用ajax上传
    $.ajax({
        type: postid.length > 0 ? "PUT" : "POST",
        url: fblog.getDomain("posts"),
        data: data,
        dataType: "json",
        success: function (data) {
            if (data && data.success) {
                window.location.href = ".";
            } else {
                alert(data.msg);
            }
        }
    });
}
//删除文章
fblog.post.remove = function (postid) {
    var isDelete = confirm("是否确认删除？");
    if (isDelete) {
        $.ajax({
            type: "DELETE",
            url: fblog.getDomain("posts/" + postid),
            dataType: "json",
            success: function (data) {
                if (data && data.success) {
                    //删除成功，刷新页面
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        });
    }
}

//快速编辑
fblog.post.fastedit = function (postid) {
    if (fblog.post.fastIndex) {
        $("#editor-" + fblog.post.fastIndex).show();
    }
    fblog.post.fastIndex = postid;
}

fblog.post.fastedit = function (postid) {
    if (fblog.post.fastIndex) {
        $("#edit-" + fblog.post.fastIndex).show();
    }

    fblog.post.fastIndex = postid;
    var editRow = $("#edit-row");
    var editPost = $("#edit-" + postid);

    editRow.find("input[name='title']").val(editPost.find(".post-title").text());
    editRow.find("input[name='createTime']").val(editPost.find(".post-ctime").text());
    editRow.find(".er-author").text(editPost.find(".post-author").text());
    editRow.find("#tags").text(editPost.find(".post-tags").text());

    var category = editPost.find(".post-category").text();
    editRow.find("input[name='category']").each(function (item) {
        $(this).prop("checked", category == $(this).parent().text());
    })

    var pstatus = editPost.find(".post-ps").text();
    editRow.find("input[name='pstatus'][value='" + pstatus + "']").prop("checked", true);

    var cstatus = editPost.find(".post-cs").text();
    editRow.find("input[name='cstatus'][value='" + cstatus + "']").prop("checked", true);

    editPost.hide();
    editPost.after(editRow);
    editRow.show();
}

fblog.post.canclefast = function () {
    if (fblog.post.fastIndex) {
        $("#edit-" + zblog.post.fastIndex).show();
    }
    fblog.post.fastIndex = null;
    $("#edit-row").hide();
}

fblog.post.submitfast = function () {
    var editRow = $("#edit-row");

    var data = {
        id: fblog.post.fastIndex,
        title: editRow.find("input[name='title']").val(),
        tags: editRow.find("#tags").val(),
        categoryid: editRow.find("input[name='category']:checked").val(),
        pstatus: editRow.find("input:radio[name='pstatus']:checked").val(),
        cstatus: editRow.find("input:radio[name='cstatus']:checked").val()
    };

    $.ajax({
        type: "PUT",
        url: fblog.getDomain("posts/fast"),
        data: data,
        dataType: "json",
        success: function (data) {
            if (data && data.success) {
                window.location.reload();
            } else {
                alert(data.msg);
            }
        }
    });
}

fblog.post.edit = function (postid) {
    window.location.href = fblog.getDomain("posts/edit?pid=" + postid);
}


