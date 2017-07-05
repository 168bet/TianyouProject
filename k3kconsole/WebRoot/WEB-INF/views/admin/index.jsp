<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <base href="<%=basePath%>">
    
    <title>K3KConsole</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<!--Basic Styles-->
    <link href="<%=basePath %>assets/css/bootstrap.min.css" rel="stylesheet" />
    <link id="bootstrap-rtl-link" href="" rel="stylesheet" />
    <link href="<%=basePath %>assets/css/font-awesome.min.css" rel="stylesheet" />
    <link href="<%=basePath %>assets/css/weather-icons.min.css" rel="stylesheet" />
    
     <!--Fonts-->
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,400,600,700,300" rel="stylesheet" type="text/css">

    <!--Beyond styles-->
    <link id="beyond-link" href="<%=basePath %>assets/css/beyond.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath %>assets/css/demo.min.css" rel="stylesheet" />
    <link href="<%=basePath %>assets/css/typicons.min.css" rel="stylesheet" />
    <link href="<%=basePath %>assets/css/animate.min.css" rel="stylesheet" />
    <link id="skin-link" href="" rel="stylesheet" type="text/css" />


    <!--Skin Script: Place this script in head to load scripts for skins and rtl support-->
    <script src="<%=basePath %>assets/js/skins.min.js"></script>
    
  </head>
  
  <body>
    <jsp:include page="loading.jsp" flush="true"></jsp:include>    
    <jsp:include page="navbar.jsp" flush="true"></jsp:include>
    
    <!-- Main Container -->
    <div class="main-container container-fluid">
        <!-- Page Container -->
        <div class="page-container">
            <!-- Page Sidebar -->
            <div class="page-sidebar" id="sidebar">
                <!-- Sidebar Menu -->
      <%@include file="left_side.jsp" %>
      <!-- /Sidebar Menu -->
            </div>
            <!-- /Page Sidebar -->
            <!-- Page Content -->
            <div class="page-content">
                <!-- Page Breadcrumb -->
                <div class="page-breadcrumbs">
                    <ul class="breadcrumb">
                        <li class=active>
                            <i class="fa fa-home"></i>
                            <a href="#">Home</a>
                        </li>
                    </ul>
                </div>
                
                <div class="page-body">
	                <div class="alert alert-info">
						欢迎来到全新改版的 <strong>K3K后台管理系统</strong>. 请愉快的使用吧~！
						<a href="#" data-dismiss="alert" class="close">×</a>
					</div>
					
                                </div>
            </div>
            <!-- /Page Content -->
        </div>
        <!-- /Page Container -->
        <!-- Main Container -->

    </div>
    
    
    
    
    <!--Basic Scripts-->
    <script src="<%=basePath %>assets/js/jquery-2.0.3.min.js"></script>
    <script src="<%=basePath %>assets/js/bootstrap.min.js"></script>

    <!--Beyond Scripts-->
    <script src="<%=basePath %>assets/js/beyond.min.js"></script>
    <script>
    /**
    var oTable = $('#tb_login_log').dataTable({
		"aLengthMenu": [
            [5, 15, 20, 25],
            [5, 15, 20, 25]
        ],
        "iDisplayLength": 5,
        "sPaginationType": "bootstrap",
        "sDom": "Tflt<'row DTTTFooter'<'col-sm-6'i><'col-sm-6'p>>",
        "oTableTools": {
        	"aButtons": [],
            },
            "language": {
                "search": "",
                "sLengthMenu": "_MENU_",
                "oPaginate": {
                    "sPrevious": "上一页",
                    "sNext": "下一页"
                }
            },
            "aoColumns": [
              { "bSortable": false },
              { "bSortable": false },
              { "bSortable": false }
            ]
    });
    */
    $("#sidebar-collapse").click();  //TODO:很搓比的写法。将菜单初始时候为收进去状态.
    </script>
  </body>
</html>
