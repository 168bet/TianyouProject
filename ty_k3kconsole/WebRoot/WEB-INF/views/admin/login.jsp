<%--
  Created by IntelliJ IDEA.
  User: jef
  Date: 2016/10/27
  Time: 下午9:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<!--Head-->
<head>
    <base href="<%=basePath%>">

    <title>Login Page</title>

    <meta name="description" content="login page" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="shortcut icon" href="assets/img/favicon.png" type="image/x-icon">

    <!--Basic Styles-->
    <link href="<%=basePath %>assets/css/bootstrap.min.css" rel="stylesheet" />
    <link id="bootstrap-rtl-link" href="" rel="stylesheet" />
    <link href="<%=basePath %>assets/css/font-awesome.min.css" rel="stylesheet" />

    <!--Fonts-->
    <link href="<%=basePath %>assets/googlefonts/OpenSans.css" rel="stylesheet"/>

    <!--Beyond styles-->
    <link id="beyond-link" href="<%=basePath %>assets/css/beyond.min.css" rel="stylesheet" />
    <link href="<%=basePath %>assets/css/demo.min.css" rel="stylesheet" />
    <link href="<%=basePath %>assets/css/animate.min.css" rel="stylesheet" />
    <link id="skin-link" href="" rel="stylesheet" type="text/css" />

    <!--Skin Script: Place this script in head to load scripts for skins and rtl support-->
    <script src="<%=basePath %>assets/js/skins.min.js"></script>
</head>
<!--Head Ends-->
<!--Body-->
<body>
<div class="login-container animated fadeInDown">
    <div class="loginbox bg-white">
        <div class="loginbox-title">登录</div>
        <div class="loginbox-textbox">
            <input type="text" class="form-control" placeholder="账号" />
        </div>
        <div class="loginbox-textbox">
            <input type="text" class="form-control" placeholder="密码" />
        </div>
        <div class="loginbox-submit">
            <input type="button" class="btn btn-primary btn-block" value="登录">
        </div>
    </div>

    </div>
</div>

<!--Basic Scripts-->
<script src="<%=basePath %>assets/js/jquery-2.0.3.min.js"></script>
<script src="<%=basePath %>assets/js/bootstrap.min.js"></script>

<!--Beyond Scripts-->
<script src="<%=basePath %>assets/js/beyond.js"></script>

<!--Google Analytics::Demo Only-->
<script>
    (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r; i[r] = i[r] || function () {
                    (i[r].q = i[r].q || []).push(arguments)
                }, i[r].l = 1 * new Date(); a = s.createElement(o),
                m = s.getElementsByTagName(o)[0]; a.async = 1; a.src = g; m.parentNode.insertBefore(a, m)
    })(window, document, 'script', 'http://www.google-analytics.com/analytics.js', 'ga');

    ga('create', 'UA-52103994-1', 'auto');
    ga('send', 'pageview');

</script>
</body>
<!--Body Ends-->
</html>

