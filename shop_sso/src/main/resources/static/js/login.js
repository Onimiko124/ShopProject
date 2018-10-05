$(function () {
    // 判断是否已经登录，如果已经登录则显示用户的信息
    $.ajax({
        url: "http://localhost:8084/sso/islogin",
        dataType:"jsonp"
    });


})

function callback(data) {
    // alert(data)
    if(data!=null){
        $("#pid").html(data.name + "您好，欢迎来到<b><a href=\"/\">ShopCZ商城</a></b>[<a href=\"http://localhost:8084/sso/logout\">注销</a>])");
    } else {
        $("#pid").html("[<a href=\"javascript:login();\">登录</a>][<a href=\"\">注册</a>]");
    }
}

// 登录的函数
function login() {
    // 获取到当前的请求路径
    var href = location.href;
    // 对请求路径进行encode编码(去除中文乱码)
    href = encodeURI(href);
    // 对&进行替换
    href = href.replace("&","*");

    // 通过login函数跳转到登录页面，保留当前的请求路径
    location.href = "http://localhost:8084/sso/tologin?returnUrl=" + href;
}