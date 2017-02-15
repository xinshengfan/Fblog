/**
 * Created by fansion on 16/01/2017.
 * 公用的js方法，在每个jsp文件中都都加载
 */
//初始化一次
if (typeof App == "undefined") {
    var fblog = {};
}
//定义命名空间方法，规则如fblog.register('fblog.post')
fblog.register = function () {
    var result = {}, temp;
    for (var i = 0; i < arguments.length; i++) {
        temp = arguments[i].split(".")
        result = window[temp[0]] = window[temp[0]] || {}
        for (var j = 0; j < temp.slice(1).length; j++) {
            result = (result[temp[j + 1]] = result[temp[j + 1]] || {})
        }
    }
    return result;
}

fblog.getDomain = function (path) {
    return window.location.protocol + "//" + window.location.host + "/backend/" + path;
}

fblog.getWebDomain = function (path) {
    return window.location.protocol + "//" + window.location.host + path;
}