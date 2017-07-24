<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<!DOCTYPE html>
<html lang="en" class="app js touch no-android chrome no-firefox no-iemobile no-ie no-ie8 no-ie10 no-ie11 no-ios"><head>
<meta charset="utf-8">
<meta http-equiv="Content-Language" content="zh-CN">
<meta name="author" content="emlog">
<meta name="robots" content="noindex, nofollow">
<title>管理中心 2- K3KConsole</title>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="<%=basePath%>/styles/bootstrap.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>/styles/animate.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>/styles/font-awesome.min.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>/styles/icon.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>/styles/font.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>/styles/app.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>/styles/style.css" type="text/css">
<script src="<%=basePath%>/scripts/js/jquery.min.js?v=5.3.1"></script>
<script src="<%=basePath%>/scripts/js/jquery-ui.min.js?v=5.3.1"></script>
<script src="<%=basePath%>/scripts/js/bootstrap.js"></script>
<script src="<%=basePath%>/scripts/js/app.js?v=5.3.1"></script>
<script src="<%=basePath%>/scripts/js/slimscroll/jquery.slimscroll.min.js"></script>
<script src="<%=basePath%>/scripts/js/app.plugin.js?v=5.3.1"></script>
<script type="text/javascript" src="<%=basePath%>/scripts/js/common.js?v=5.3.1"></script>
<!--[if lt IE 9]>
<script src="<%=basePath%>/scripts/js/ie/html5shiv.js"></script>
<script src="<%=basePath%>/scripts/js/ie/respond.min.js"></script>
<script src="<%=basePath%>/scripts/js/ie/excanvas.js"></script>
<![endif]-->
</head>
<body class="">
<section class="vbox">
<header class="bg-light header header-md navbar navbar-fixed-top-xs box-shadow">
	<div class="navbar-header aside-md dk">
		<a class="btn btn-link visible-xs" data-toggle="class:nav-off-screen" data-target="#nav"><i class="fa fa-bars"></i></a>
		<a class="navbar-brand" href="../" target="_blank" title="在新窗口浏站点"><span class="hidden-nav-xs">K3K Console</span></a>
		<a class="btn btn-link visible-xs" data-toggle="dropdown" data-target=".user"><i class="fa fa-cog"></i></a>
	</div>
	
	<ul class="nav navbar-nav navbar-right m-n hidden-xs nav-user user">
		<!-- <li class="hidden-xs"><a href="./configure.php"><i class="fa fa-cog" style="font-size:18px;"></i></a></li>  -->

   		<li class="dropdown">
			<a href="#" class="dropdown-toggle" data-toggle="dropdown">
			<span class="thumb-sm avatar pull-right m-t-n-sm m-b-n-sm m-l-sm"><img src="<%=basePath%>/images/avatar.png"></span>康强 <b class="caret"></b></a>
			<ul class="dropdown-menu animated fadeInRight">
				<li><span class="arrow top"></span><a href="?action=logout"><i class="fa fa-power-off fa-fw"></i> Logout</a></li>
			</ul>
		</li>
	</ul>
</header>
