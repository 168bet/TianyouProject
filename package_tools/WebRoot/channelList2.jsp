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
	$(function(){
		$('.edit').click(function(){
			var vo =  $(this).prev('input').val();
			var channelid = $(this).prev('input').prev('input').val();
			$.post('${pageContext.request.contextPath }/channel?op=updateChannelID',{vo:vo,channelid:channelid},function(msg){
				window.location.reload();
			});
		});
	})
	
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
	th, td {
		border:1px solid #FF7F50;
	}
</style>
</head>
 <body class="font3">
 	<br><br>
 	<form action="${pageContext.request.contextPath }/channel?op=confirm" method="post">
		<table align="center" width="1000px">
			<tr>
				<th colspan=3>
				<form action="${pageContext.request.contextPath }/channel?op=searchChannel&uuid=${file.channelId }" method="post" >
					<h2>渠道号列表：</h2>要查询的渠道名:<input type="text" name="search_channelname">
					<input type="submit" value="搜索" onclick="location'${pageContext.request.contextPath}/channel?searchChannel'">
				</form><br><br>
				</th>
			</tr>
			<%int i = 0; %>
	        <c:forEach items="${channel_list}" var="file">
	            <tr height="45px">
		           <td>&nbsp;<input type="checkbox" name="channel" value="${file.channelName }" id="${file.channelName }"/>&nbsp;<%i++; %><%=i %>.&nbsp;<label for="${file.channelName }">${file.channelName } —— ${file.channelId }</label></td>
			       <td align="center">
			       		<input type='hidden' class='channelid' value="${file.channelName }">
			       		渠道号：<input type="text" name="channelid">
				       <input type="button" value="修改" class='edit'>
			       </td>
		    	</tr>
	        </c:forEach>
	        <tr>
				<th height="45px"><input type="button" id="othercheck" value="全选 / 反选" class="btn" onclick="_reverseChecked()"></th>
				<th colspan=2><input type="submit" value="确认选择" class="btn"/></th>
			</tr>
		</table><br>
	</form>
		
	<div align="center">
		<a href="${pageContext.request.contextPath }/channel?op=last_page">上一页</a>&nbsp;&nbsp;&nbsp;
		<a href="${pageContext.request.contextPath }/channel?op=next_page">下一页</a>
	</div>

</body>
</html>
