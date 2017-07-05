<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <base href="<%=basePath%>">

    <title>K3K后台管理系统</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
-->
    <!--Basic Styles-->
    <link href="<%=basePath %>assets/css/bootstrap.min.css" rel="stylesheet"/>
    <link id="bootstrap-rtl-link" href="" rel="stylesheet"/>
    <link href="<%=basePath %>assets/css/font-awesome.min.css" rel="stylesheet"/>
    <link href="<%=basePath %>assets/css/weather-icons.min.css" rel="stylesheet"/>

    <!--Fonts-->

    <!--Beyond styles-->
    <link id="beyond-link" href="<%=basePath %>assets/css/beyond.min.css"
          rel="stylesheet" type="text/css"/>
    <link href="<%=basePath %>assets/css/demo.min.css" rel="stylesheet"/>
    <link href="<%=basePath %>assets/css/typicons.min.css" rel="stylesheet"/>
    <link href="<%=basePath %>assets/css/animate.min.css" rel="stylesheet"/>
    <link id="skin-link" href="" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath %>scripts/fileUpload/style/uploadify.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath %>scripts/fileUpload/style/flat-ui.css" type="text/css"/>
    <!--Page Related styles-->
    <link href="<%=basePath %>assets/css/dataTables.bootstrap.css" rel="stylesheet"/>

    <!--Skin Script: Place this script in head to load scripts for skins and rtl support-->
    <script src="<%=basePath %>assets/js/skins.min.js"></script>
</head>

<body>
<jsp:include page="../loading.jsp" flush="true"></jsp:include>

<jsp:include page="../navbar.jsp" flush="true"></jsp:include>

<!-- Main Container -->
<div class="main-container container-fluid">
    <!-- Page Container -->
    <div class="page-container">
        <!-- Page Sidebar -->
        <div class="page-sidebar" id="sidebar">
            <!-- Sidebar Menu -->
            <%@ include file="../left_side.jsp" %>
            <!-- /Sidebar Menu -->
        </div>
        <!-- /Page Sidebar -->
        <!-- Page Content -->
        <div class="page-content">
            <!-- Page Breadcrumb -->
            <div class="page-breadcrumbs">
                <ul class="breadcrumb">
                    <li>
                        <i class="fa fa-home"></i>
                        <a href="#">首页</a>
                    </li>
                    <li><a href="#">GM操作</a></li>
                    <li class="active">发送公告</li>
                </ul>
            </div>
            <!-- /Page Breadcrumb -->

            <!-- 真正内容 -->

            <!-- Page Body -->
            <div class="page-body">
                <!-- 查询 start-->
                <div class="widget radius-bordered">
                    <div class="widget-header bordered-bottom">
                        <span class="widget-caption">公告信息输入区</span>
                    </div>
                    <form id="sform">
                        <div class="widget-body">
                            <div class="row">
                                <div class="col-lg-4">
                                    <label><strong>公告内容</strong></label>
                                    <textarea id="msg" class="form-control" name="msg" rows="5"
                                              style="width: 250%"></textarea>
                                </div>
                            </div>
                            <br>
                            <div class="row">
                                <div class="col-lg-4">
                                    <div style="padding-top:5px;">
                                        <input id="sendBtn" type="button" class="btn btn-blue" value="提交">
                                        <input id="resetBtn" type="reset" class="btn btn-danger" value="重置">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <!-- 查询 over-->
                <!-- /真正内容 -->
            </div>
            <!-- /Page Content -->
        </div>
        <!-- /Page Container -->
        <!-- Main Container -->

    </div>
</div>


<!--Beyond Scripts-->
<script src="<%=basePath %>assets/js/beyond.min.js"></script>
<!--Page Related Scripts-->
<script src="<%=basePath %>assets/js/datatable/jquery.dataTables.min.js"></script>
<script src="<%=basePath %>assets/js/datatable/ZeroClipboard.js"></script>
<script src="<%=basePath %>scripts/fileUpload/jquery.uploadify-3.1.0.min.js?r=<%=new Random().nextInt() %>"></script>
<!--Jquery Select2-->
<script src="<%=basePath %>assets/js/select2/select2.js"></script>
<script src="<%=basePath %>assets/js/jquery.form.js"></script>
<script src="<%=basePath %>assets/js/bootstrap.min.js"></script>
<script src="<%=basePath %>assets/js/bootbox/bootbox.js"></script>

<script>

    $(document).ready(function () {
        //修改左侧菜单样式
        $(".sidebar-menu .menu-li").removeClass("active");
        $(".sidebar-menu .submenu li").removeClass("active");
        $(".sidebar-menu .menu-li:eq(8)").addClass("active").addClass("open");
        $(".sidebar-menu .menu-li:eq(8) .submenu li:eq(0)").addClass("active");

        $(".el-select").select2({
            placeholder: "----请选择----",
            language: "zh-CN",
            allowClear: true
        });

    });
    $('#sendBtn').click(function(){
        $('#sform').ajaxSubmit({
            type:'POST',
            url:'${base}/slgmail/sendNotice',
            dataType: "json",
            success:function(data){
                bootbox.alert(data.message);
                if(data.result != 4){
                    reload_dt();
                }
            },
            error:function(XmlHttpRequest,textStatus,errorThrown){
                console.log(XmlHttpRequest);
                console.log(textStatus);
                console.log(errorThrown);
            }
        });
    });

    $('#resetBtn').click(function () {
        reload_dt()
    });

    function serializeForm(form, obj) {
        if (obj == null) {
            obj = {};
        }
        $.each(form.serializeArray(), function (index) {
            if (obj[this['name']]) {
                obj[this['name']] = obj[this['name']] + ',' + this['value'];
            } else {
                obj[this['name']] = this['value'];
            }
        });
        return obj;
    }

    function reload_dt() {
        $('#sform').reset;
    }
</script>

</body>
</html>
