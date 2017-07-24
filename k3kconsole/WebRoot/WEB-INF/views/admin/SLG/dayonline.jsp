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
                    <li><a href="#">SLG统计</a></li>
                    <li class="active">分时在线(按天)</li>
                </ul>
            </div>
            <!-- /Page Breadcrumb -->

            <!-- 真正内容 -->

            <!-- Page Body -->
            <div class="page-body">
                <!-- 查询 start-->
                <div class="widget radius-bordered">
                    <div class="widget-header bordered-bottom">
                        <span class="widget-caption">查询条件输入区</span>
                    </div>
                    <form id="sform">
                        <div class="widget-body">
                            <div class="row">
                                <div class="col-lg-4">
                                    <label><strong>时间区域</strong></label>
                                    <div class="input-group">
                                        <span class="input-group-addon">
                                        <i class="fa fa-calendar"></i></span>
                                        <input name="DateRange" type="text" class="form-control" id="date-range"/>
                                    </div>
                                </div>
                                <div class="col-lg-4">
                                    <label><strong>渠道</strong></label>
                                    <select name="enter" class="el-select" style="width:100%;">
                                        <option value="0">---所有---</option>
                                        <option value="1">天游</option>
                                        <option value="2">闲趣</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-4">
                                    <label><strong>游戏入口</strong></label>
                                    <select name="union" class="el-select" style="width:100%;">
                                        <option value="0">---所有---</option>
                                        <option value="1">列王传奇</option>
                                        <option value="10">即刻出兵</option>
                                        <option value="2">大秦帝国战争</option>
                                        <option value="4">传奇指挥官</option>
                                        <option value="5">远征部落</option>
                                        <option value="6">军团帝国</option>
                                        <option value="7">帝国传奇战争</option>
                                        <option value="8">WOK列王之战</option>
                                        <option value="9">皇室传奇之争</option>
                                        <option value="12">皇室英雄</option>
                                        <option value="13">皇室传奇</option>
                                        <option value="14">帝国王者</option>
                                        <option value="15">帝国荣耀</option>
                                        <option value="16">文明起源</option>
                                    </select>
                                </div>
                                <div class="col-lg-4">
                                    <label><strong>平台</strong></label>
                                    <select name="plat" class="el-select" style="width:100%;">
                                        <option value="0">---所有---</option>
                                        <option value="1">IOS</option>
                                        <option value="2">Android</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-4">
                                    <div style="padding-top:5px;">
                                        <input id="searchBtn" type="button" class="btn btn-blue" value="搜索">
                                        <input id="resetBtn" type="reset" class="btn btn-danger" value="重置">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <!-- 查询 over-->

                <!-- 表格区域 -->
                <div class="row">
                    <div class="col-xs-12 col-md-12">
                        <div class="widget">
                            <div class="widget-header ">
                                <span class="widget-caption">分时在线(按天)</span>
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
                                <table class="table table-striped table-hover table-bordered" id="tb_account">
                                    <thead> 
                                    <tr> 
                                        <th field="day" data-switchable="false">日期</th>

                                        <th field="lognum" data-switchable="false">最高在线人数</th>

                                        <th field="lognum" data-switchable="false">最低在线人数</th>

                                        <th field="lognum" data-switchable="false">平均在线人数</th>

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
<script src="<%=basePath %>assets/js/datetime/moment.js"></script>
<script src="<%=basePath %>assets/js/datetime/daterangepicker.js"></script>
<script src="<%=basePath %>assets/js/select2/select2.js"></script>
<script src="<%=basePath %>scripts/highcharts/highcharts.js"></script>
<script type="text/javascript" src="<%=basePath %>assets/js/bootstrap-table.js"></script>

<script>
    $(document).ready(function () {
        //注册统计左侧菜单样式
        $(".sidebar-menu .menu-li").removeClass("active");
        $(".sidebar-menu .submenu li").removeClass("active");
        $(".sidebar-menu .menu-li:eq(7)").addClass("active").addClass("open");
        $(".sidebar-menu .menu-li:eq(7) .submenu li:eq(4)").addClass("active");

        $('#date-range').daterangepicker({
            'format': 'YYYY-MM-DD',
            'separator': '  &  '
        });


        $(".el-select").select2({
            placeholder: "----请选择----",
            language: "zh-CN",
            allowClear: true
        });

        var oTable = $('#tb_account').dataTable({
            "bServerSide": true,
            "sinfo": false,
            "bLengthChange": false,
            "iDisplayLength": 10,
            "bProcessing": true,
            "sProcessing": "数据有点多，努力加载中...",
            "sPaginationType": "bootstrap",
            "sDom": "flt<'row DTTTFooter'<'col-sm-6'i><'col-sm-6'p>>",
            "bPaginate": true,
            "bSort": false,
            "bFilter": false,
            "aoColumns": [
                {"mData": 'date'}, {"mData": 'max'}, {"mData": 'min'}, {"mData": 'avg'}
            ],
            "sAjaxSource": "slgonline/queryday",
            "fnServerData": retrieveData
        });

        $('#searchBtn').click(function () {
            reload_dt();
        });
        $('#resetBtn').click(function () {
            reload_dt();
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
        $('#tb_account').DataTable().ajax.reload();
    }

</script>

</body>
</html>
