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

    <!--Fonts-->
    <link href="<%=basePath %>assets/googlefonts/OpenSans.css" rel="stylesheet"/>

    <!--Beyond styles-->
    <link id="beyond-link" href="<%=basePath %>assets/css/beyond.min.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath %>assets/css/demo.min.css" rel="stylesheet"/>
    <link href="<%=basePath %>assets/css/typicons.min.css" rel="stylesheet"/>
    <link href="<%=basePath %>assets/css/animate.min.css" rel="stylesheet"/>
    <link id="skin-link" href="" rel="stylesheet" type="text/css"/>

    <!--Page Related styles-->
    <link href="<%=basePath %>assets/css/dataTables.bootstrap.css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>assets/css/bootstrap-table.css">
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
                    <li><a href="#">GM工具</a></li>
                    <li class="active">头像审核</li>
                </ul>
            </div>
            <!-- /Page Breadcrumb -->

            <!-- 真正内容 -->

            <!-- Page Body -->
            <div class="page-body">
                <!-- 查询 start-->
                <!-- 查询 over-->
                <!-- 表格区域 -->
                <div class="row">
                    <div class="col-xs-12 col-md-12">
                        <div class="widget">
                            <div class="widget-header ">
                                <span class="widget-caption">头像审核</span>
                                <div class="widget-buttons">
                                    <a href="#" data-toggle="maximize">
                                        <i class="fa fa-expand"></i>
                                    </a>
                                    <a href="#" data-toggle="collapse">
                                        <i class="fa fa-minus"></i>
                                    </a>
                                    <a href="#" data-toggle="dispose">
                                        <i class="fa fa-times"></i>
                                    </a>
                                </div>
                            </div>

                            <div class="widget-body" style="overflow-x: scroll;">
                                <br>
                                <table class="table table-striped table-hover table-bordered" id="tb_userimg">
                                    <thead> 
                                    <tr> 

                                        <th field="lognum" data-switchable="false">玩家ID</th>

                                        <th field="active3" data-visible="false">头像</th>

                                        <th field="active7" data-visible="false">操作</th>

                                    </tr>
                                     
                                    </thead>
                                     
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <!--表格内容>
            <!-- /真正内容 -->
            </div>
            <!-- /Page Content -->
        </div>
        <!-- /Page Container -->
        <!-- Main Container -->

    </div>
</div>


<!--Basic Scripts-->
<script src="<%=basePath %>assets/js/jquery-2.0.3.min.js"></script>
<script src="<%=basePath %>assets/js/bootstrap.min.js"></script>
<!--Beyond Scripts-->
<script src="<%=basePath %>assets/js/beyond.min.js"></script>
<!--Page Related Scripts-->
<script src="<%=basePath %>assets/js/datatable/jquery.dataTables.min.js"></script>
<script src="<%=basePath %>assets/js/datatable/ZeroClipboard.js"></script>
<script src="<%=basePath %>assets/js/datatable/dataTables.tableTools.min.js"></script>
<script src="<%=basePath %>assets/js/datatable/dataTables.bootstrap.min.js"></script>
<script src="<%=basePath %>assets/js/select2/select2.js"></script>
<script src="<%=basePath %>scripts/highcharts/highcharts.js"></script>
<script src="<%=basePath %>assets/js/bootbox/bootbox.js"></script>
<script type="text/javascript" src="<%=basePath %>assets/js/bootstrap-table.js"></script>

<script>
    $(document).ready(function () {
        //注册统计左侧菜单样式
        $(".sidebar-menu .menu-li").removeClass("active");
        $(".sidebar-menu .submenu li").removeClass("active");
        $(".sidebar-menu .menu-li:eq(7)").addClass("active").addClass("open");
        $(".sidebar-menu .menu-li:eq(7) .submenu li:eq(1)").addClass("active");


        $(".el-select").select2({
            placeholder: "----请选择----",
            language: "zh-CN",
            allowClear: true
        });

        var oTable = $('#tb_userimg').dataTable({
            "bServerSide": true,
            "sinfo": false,
            "bLengthChange": false,
            "iDisplayLength": 100,
            "bProcessing": true,
            "sProcessing": "数据有点多，努力加载中...",
            "sPaginationType": "bootstrap",
            "sDom": "flt<'row DTTTFooter'<'col-sm-6'i><'col-sm-6'p>>",
            "bPaginate": true,
            "bSort": false,
            "bFilter": false,
            "aoColumns": [
                {"mData": 'uid'},
                {
                    "mData": "action", "mRender": function (data, type, full) {
                    return '<a href="#" class="btn btn-info btn-xs photo"> 查看图片 </a>';
                }
                },
                {
                    "mData": "action", "mRender": function (data, type, full) {
                    return '<a href="#" class="btn btn-blue btn-xs pass"> 通过 </a>&nbsp;&nbsp <a href="#" class="btn btn-danger btn-xs reject"> 拒绝 </a>';
                }
                }
            ],
            "sAjaxSource": "slgimg/query",
            "fnServerData": retrieveData
        });

        $('#searchBtn').click(function () {
            reload_dt();
        });
        $('#resetBtn').click(function () {
            reload_dt();
        });

        //照片
        $('#tb_userimg').on("click", 'a.photo', function (e) {
            e.preventDefault();
            var nRow = $(this).parents('tr')[0];
            var aData = oTable.fnGetData(nRow);
            window.open(aData.headimg);
        });

        $('#tb_userimg').on('click', 'a.pass', function (e) {
            e.preventDefault();
            var nRow = $(this).parents('tr')[0];
            var aData = oTable.fnGetData(nRow);
            bootbox.confirm({
                message: "请确认是否通过 用户头像:" + aData.uid,
                buttons: {
                    cancel: {
                        label: '取消',
                        className: 'btn btn-danger'
                    },
                    confirm: {
                        label: '确认',
                        className: 'btn btn-blue'
                    }
                },
                callback: function (result) {
                    if (result == true) {
                        $.ajax({
                            url: "slgimg/done?type=passPicVer&userid=" + aData.uid,
                            type: "POST",
                            dataType: "json",
                            success: function (data) {
                                if (data.result == 0) {
                                    bootbox.alert("操作成功");
                                    reload_dt();
                                } else {
                                    bootbox.alert("操作失败");
                                }
                            }
                        });
                    }
                }
            });
        });

        $('#tb_userimg').on('click', 'a.reject', function (e) {
            e.preventDefault();
            var nRow = $(this).parents('tr')[0];
            var aData = oTable.fnGetData(nRow);
            bootbox.confirm({
                message: "请确认是否拒绝 用户头像:" + aData.uid ,
                buttons: {
                    cancel: {
                        label: '取消',
                        className: 'btn btn-danger'
                    },
                    confirm: {
                        label: '确认',
                        className: 'btn btn-blue'
                    }
                },
                callback: function (result) {
                    if (result == true) {
                        $.ajax({
                            url: "slgimg/done?type=rejectPicVer&userid=" + aData.uid,
                            type: "POST",
                            dataType: "json",
                            success: function (data) {
                                if (data.result == 0) {
                                    bootbox.alert("操作成功");
                                    reload_dt();
                                } else {
                                    bootbox.alert("操作失败");
                                }
                            }
                        });
                    }
                }
            });
        });

    });


    function retrieveData(sSource, aoData, fnCallback) {
        var sObj = serializeForm($('#sform'), aoData);
        console.log(aoData);
        console.log(sObj);
        $.ajax({
            type: "POST",
            url: sSource,
            data: aoData,
            dataType: "json",
            success: function (data) {
                fnCallback(data);
            }
        });
    }

    function serializeForm(form, obj) {
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
        $('#tb_userimg').DataTable().ajax.reload();
    }

</script>

</body>
</html>
