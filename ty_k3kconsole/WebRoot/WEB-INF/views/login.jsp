<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>K3K-Console</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="shortcut icon" href="<%=basePath %>assets/img/favicon.png" type="image/x-icon"/>
<link rel="stylesheet" href="<%=basePath %>styles/login.css" type="text/css"/>
<script type="text/javascript" src="<%=basePath %>assets/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="<%=basePath %>scripts/login.js"/>
<script type="text/javascript">
	$(document).ready(function(){
		$("#login").addClass("current"); 
	});

	function BindEnter(e) {
		var keyCode = null;
        if(e.which)
            keyCode = e.which;
        else if(e.keyCode) 
            keyCode = e.keyCode;
		if (keyCode == 13) {
		     event.cancelBubble = true;
		     event.returnValue = false;
		     $("button[type='button']").click(); 
	    }
	}
</script>
</head>
<body>
<div id="home">
    <form id="login" class="current1" method="post" action="index" autocomplete="on">           
        <h3>用户登录</h3>
        <img class="avator" src="<%=basePath %>images/avatar.png" width="96" height="96"/>
        <label>登录名<input type="text" name="userName" style="width:160px;"/><span>登录名为空</span></label>
        <label>密码<input id="password" type="password" name="password"  style="width:160px;"/><span>密码为空</span></label>
       <%-- <div>
        	<input type="checkbox" name="rememberMe" style="float:left;margin-top:8px;margin-right:3px;"/>
        	<p style="width:45px;float:left;height:30px;line-height:30px;color:#000;font-size:12px;">记住我</p>
        </div>--%>
        <button type="button" onkeypress="bindEnter(event)">登录</button>   
    </form> 
</div>
</body>
</html>
