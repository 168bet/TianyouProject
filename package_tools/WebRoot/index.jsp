<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jstl/core_rt" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ include file="logo.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>天游打包工具</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
  	<div>
  	<form action="${pageContext.request.contextPath }/main?op=upload_package" method="post" enctype="multipart/form-data">
		<div><br><br>
			上传游戏主包:<input type="file" name="main">
			<input type="submit" value="上传" class="white1">
		</div>
	</form><hr>
	
	<!-- 
	<form action="${pageContext.request.contextPath }/main?op=upload_keystore" method="post" enctype="multipart/form-data">
		<div><br>
			<table>
				<tr><td>上传游戏签名:</td><td><input type="file" name="sign"></td></tr>
				<tr><td>签名密码：</td><td><input type="text" name="pass"></td></tr>
				<tr><td>签名别名：</td><td><input type="text" name="alias"></td></tr>
				<tr><th colspan=3><input type="submit" value="上传" class="white1"></th></tr>
			</table>
		</div>
	</form>
	-->
	
  	<form action="${pageContext.request.contextPath }/main?op=packing" method="post">
		<br>选择主包：<br>
		<table>
			<c:forEach items="${apklist}" var="file">
				<tr>
					<td class="td1"><input type="radio" name="apk" value="${file.key }" id="${file.key }"></td>
					<td class="td1" width="290px"><label for="${file.key }">${file.value }</label></td>
					<th class="td1"><input type="button" value="删除" class="white1" onclick="location='${pageContext.request.contextPath }/main?op=delete&uuid=${file.key }'"></th>
				</tr>
			</c:forEach>
		</table><hr>
		
		<br>选择签名：<br>
		<table>
			<c:forEach items="${keystonelist}" var="file">
				<tr>
					<td class="td1"><input type="radio" name="keystore" value="${file.key }" id="${file.key }"></td>
					<td class="td1" width="290px"><label for="${file.key }">${file.value }</label></td>
					<th class="td1"><input type="button" value="删除" class="white1" onclick="location='${pageContext.request.contextPath }/main?op=delete&uuid=${file.key }'"></th>
				</tr>
			</c:forEach>
		</table><hr>
		 
		<br>已选渠道：		
		<a href="${pageContext.request.contextPath }/channel?op=channel_list">选择渠道</a>&nbsp;
		<a href="${pageContext.request.contextPath }/main?op=clean_channel">清空已选</a>&nbsp;
		<a href="${pageContext.request.contextPath }/channel?op=custom_channel">自选渠道</a><br>
		<table>
			<tr>
				<c:forEach items="${select_channel }" var="file" varStatus="vs">
					<td width="300">
						${vs.count }. ${file }
					</td>
					<c:if test="${vs.count % 5 == 0}">
						<tr>
					</c:if>
				</c:forEach>
			</tr>
		</table><hr>
  		<br>包名：<input type="text" name="pack_name"><br><hr>
  		<br><input type="submit" value="开始打包"><br><br>
  		<label>${flag }</label>
  	</form>
  	</div>
  </body>
</html>
