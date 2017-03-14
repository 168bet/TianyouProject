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
<script src="jquery-1.8.3.js"></script>
<script type="text/javascript">
	function _reverseChecked(){
		var channel = document.getElementsByName("channel");
		for(var i=0;i<channel.length;i++){
			channel[i].checked = !channel[i].checked;
		}
	}
</script>
<title>渠道号列表</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
<link rel="stylesheet" type="text/css" href="css/button.css">
<link rel="stylesheet" type="text/css" href="css/font.css">
<style type="text/css">
	td,th {border:solid #999; border-width:0px 1px 1px 0px;}
	table{border:solid #999; border-width:1px 0px 0px 1px;}
</style>
</head>
<body>
	<form action="${pageContext.request.contextPath }/channel?op=confirm" method="post">
	<div align="center"><br>
	<table>
		<tr><th>主渠道：</th><th>子渠道：<th width="100px"><input type="text" size="5"><a href="">搜索</a></th></tr>
		<c:forEach items="${channel_list}" var="file" varStatus="vs">
			<tr>
				<td align="center"><input type="checkbox" onclick="_reverseChecked()">${file.channelName }</td>
				<td colspan="2">
					<c:forEach items="${file.childChannels }" var="file2" varStatus="vs">
						<input type="checkbox" name="channel" value="${file2.channelName }:${file2.channelId }" id="${file2.channelName }">
						<label for="${file2.channelName }">${file2.channelName } : ${file2.channelId }</label>&nbsp;&nbsp;&nbsp;
						<c:if test="${vs.count == 5 }"><br></c:if>
					</c:forEach>
				</td>
			</tr>
	    </c:forEach>
	</table><br>
	<input type="submit" value="确认选择" class="btn"/>
	</div>
	</form>
</body>
</html>
