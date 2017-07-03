<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>403 - 没有权限</title>
</head>

<body>
<script language=javascript> 
$.cookie("tab_cookie", null, { path: '/' });

if (top.location !== self.location) {
top.location=self.location;
}
</script> 
<div>
	<div><h1>没有权限访问该页面.</h1></div>
	<div><a href="<c:url value="/index"/>">返回首页</a></div>
</div>
</body>
</html>