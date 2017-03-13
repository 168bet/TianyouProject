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
<title>渠道号列表</title>
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
 	<div align="center"><br>
		<table width="800px">
			<tr><th>打包日期</th><th>打包操作</th></tr>
			<c:forEach items="${pkg_dir_list}" var="file" varStatus="vs">
		    	<tr height="50px">
		    		<td align="center">${vs.count}.&nbsp;<a href="${pageContext.request.contextPath }/main?op=pkg_list&dir_name=${file }">${file }</a></td>
			    	<td align="center">
			    		<input type="button" value="下载" class="white2" onclick="location='${pageContext.request.contextPath }/main?op=down_pkg_dir&pkg_dir=${file }'">
						<input type="button" value="删除" class="white2" onclick="location='${pageContext.request.contextPath }/main?op=del_pkg_dir&pkg_dir=${file }'">
			    	</td>
		    	</tr>
			</c:forEach>
		</table>
		
		<!-- 
		<div align="center"><br>
			<a href="${pageContext.request.contextPath }/package?op=last_page">上一页</a>&nbsp;&nbsp;&nbsp;
			<a href="${pageContext.request.contextPath }/package?op=next_page">下一页</a>
		</div>
		 -->
	</div>
</body>
</html>
