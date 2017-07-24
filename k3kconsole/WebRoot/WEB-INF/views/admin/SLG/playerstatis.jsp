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
                    <li><a href="#">SLG</a></li>
                    <li class="active">领主统计</li>
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
                                <div class="col-lg-6 col-sm-6 col-xs-12">
                                    <div class="input-group">
                                        <input name="Date" class="form-control date-picker" placeholder="选择时间"
                                               id="date-picker" type="text" data-date-format="yyyy-mm-dd" >
                                        <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    </div>
                                </div>
                                <div class="col-lg-8"></div>
                            </div>
                            <div class="row">
                                <div class="col-lg-4">
                                    <label><strong>领主信息</strong></label>
                                    <select name="type" class="el-select" style="width:100%;">
                                        <option value="1">领主等级</option>
                                        <option value="2">领主战力</option>
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
                                <div class="col-lg-4"></div>
                            </div>
                        </div>
                    </form>
                </div>
                <!-- 查询 over-->

                <!-- 图标区域 -->
                <div class="row">
                    <div class="col-xs-12 col-md-12">
                        <div class="widget">
                            <div class="widget-header ">
                                <span class="widget-caption">图表</span>
                                <div class="widget-buttons">
                                    <a href="#" data-toggle="maximize"> <i class="fa fa-expand"></i></a>
                                    <a href="#" data-toggle="collapse"> <i class="fa fa-minus"></i></a>
                                    <a href="#" data-toggle="dispose"> <i class="fa fa-times"></i></a>
                                </div>
                            </div>
                            <div class="widget-body">
                                <div id="reg-chart" class="chart">
                                    暂无数据...
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 表格区域 -->
                <div class="row">
                    <div class="col-xs-12 col-md-12">
                        <div class="widget">
                            <div class="widget-header ">
                                <span class="widget-caption">领主统计</span>
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

                            <div class="widget-body">
                                <br>
                                <table class="table table-striped table-hover table-bordered" id="tb_userlv">
                                    <thead>
                                    <tr>
                                        <th>领主战力</th>
                                        <th>人数</th>
                                    </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <!--图表内容>
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
<script src="<%=basePath %>assets/js/datetime/bootstrap-datepicker.js"></script>
<script src="<%=basePath %>assets/js/select2/select2.js"></script>
<script src="<%=basePath %>scripts/highcharts/highcharts.js"></script>

<script>
    $(document).ready(function () {
        //注册统计左侧菜单样式
        $(".sidebar-menu .menu-li").removeClass("active");
        $(".sidebar-menu .submenu li").removeClass("active");
        $(".sidebar-menu .menu-li:eq(7)").addClass("active").addClass("open");
        $(".sidebar-menu .menu-li:eq(7) .submenu li:eq(8)").addClass("active");

        $('.date-picker').datepicker();

        $(".el-select").select2({
            placeholder: "----请选择----",
            language: "zh-CN",
            allowClear: true
        });

        var oTable = $('#tb_userlv').dataTable({
            "bServerSide": true,
            "bLengthChange": false,
            "iDisplayLength": 24,
            "bProcessing": true,
            "sProcessing": "数据有点多，努力加载中...",
            "sPaginationType": "bootstrap",
            "sDom": "flt<'row DTTTFooter'<'col-sm-6'i><'col-sm-6'p>>",
            "bPaginate": true,
            "bSort": false,
            "bFilter": false,
            "aoColumns": [
                {"mData": "userlv"},
                {"mData": "usernum"},
            ],
            "sAjaxSource": "slglevel/queryUser",
            "fnServerData": retrieveData
        });

        $('#searchBtn').click(function () {
            reload_dt();
        });
        $('#resetBtn').click(function () {
            reload_dt();
        });
    });


    function draw_charts(data) {
        var chart_data = data.aaData;
        var usernum = chart_data.map(function (o) {
            return o.usernum
        });

        var series = [{name: '领主人数', data: usernum}];
        var categories = chart_data.map(function (e) {
            return e.userlv
        });

        var chart_options = {
            chart: {
                type: 'line',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                renderTo: "reg-chart"
            },
            credits: {enabled: false},
            exporting: {
                url: 'http://chart.wenjuan.com',
                enabled: true
            },
            lang: {
                printChart: "打印",
                downloadJPEG: "另存为JPEG",
                downloadPDF: "另存为PDF",
                downloadPNG: "另存为PNG",
                downloadSVG: "另存为SVG"
            },
            title: {
                text: '领主统计',
                x: -20 //center
            },
            xAxis: {

                categories: categories,
                labels: {
                    step: 2,
                    staggerLines: 1
                }
            },
            yAxis: {
                min:0,
                title: {
                    text: '人数'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                valueSuffix: ''
            },
            legend: {
                layout: 'horizontal',
                align: 'center',
                verticalAlign: 'bottom',
                borderWidth: 0
            },
            series: series,
        };
        new Highcharts.Chart(chart_options);
    }

    function retrieveData(sSource, aoData, fnCallback) {
        var sObj = serializeForm($('#sform'), aoData);
        $.ajax({
            type: "POST",
            url: sSource,
            data: aoData,
            dataType: "json",
            success: function (data) {
                fnCallback(data);
                draw_charts(data);
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
        $('#tb_userlv').DataTable().ajax.reload();
    }

</script>

</body>
</html>
