<%@page import="java.security.KeyStore.Entry"%>
<%@page import="java.util.Set"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="logo.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>打包进度</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
<link rel="stylesheet" type="text/css" href="css/button.css">
<link rel="stylesheet" type="text/css" href="css/font.css">
<style type="text/css">
	th, td {
		border-bottom:1px solid #FF7F50;
	}
</style>
</head>
<body class="font3">

	<table style="border:0px; " cellpadding="10" align="center">
			<tr><th>已完成包列表：</th>
			<c:forEach items="${pkg_list}" var="file" varStatus="vs">
		    	<tr>
		    		<td>&nbsp;&nbsp;&nbsp;${vs.count}.&nbsp;${file }</td>
		    	</tr>
			</c:forEach>
	</table><br>
		
	<center>
		<input type="button" value="刷新" onclick="location='${pageContext.request.contextPath }/main?op=pkg_list&dir_name=${current_pkg_dir }'">&nbsp;&nbsp;&nbsp;
		<input type="button" value="返回" onclick="location='${pageContext.request.contextPath }/main?op=pkg_dir_list'">&nbsp;&nbsp;&nbsp;
	</center>
	
</body>
</html>