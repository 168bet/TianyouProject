<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <base href="<%=basePath%>">

    <title>SLG</title>

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
    <link href="<%=basePath %>assets/css/dataTables.bootstrap.css"
          rel="stylesheet"/>

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
                        <a href="#">Home</a>
                    </li>
                    <li>
                        <a href="#">GM操作</a>
                    </li>
                    <li class="active">
                        发奖
                    </li>
                </ul>
            </div>
            <!-- /Page Breadcrumb -->

            <!-- 真正内容 -->

            <!-- Page Body -->
            <div class="page-body">
                <div class="tabbable">
                    <ul class="nav nav-tabs nav-justified" id="myTab5">
                        <li class="active">
                            <a data-toggle="tab" href="#home5" style="font-weight: bold;">全区发奖</a>
                        </li>
                        <li class="tab-red">
                            <a data-toggle="tab" href="#profile0" style="font-weight: bold;">指定发奖</a>
                        </li>
                    </ul>

                    <div class="tab-content">
                        <div id="home5" class="tab-pane in active">
                            <!-- 查询 start-->
                            <div class="widget radius-bordered">
                                <div class="widget-header bordered-bottom">
                                    <span class="widget-caption">全区发奖</span>
                                </div>
                                <form id="sform">
                                    <div class="widget-body">
                                        <div class="row">
                                            <div class="col-lg-4">
                                                <label><strong>区服</strong></label>
                                                <select name="server" class="el-select" style="width:100%;">
                                                    <option value="0">---所有---</option>
                                                    <option value="0">一区</option>
                                                </select>
                                            </div>
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
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div class="col-lg-4">
                                                <div>
                                                    <label><strong>邮件标题</strong></label>
                                                    <input name="title" type="text" class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div class="col-lg-4">
                                                <label><strong>邮件内容</strong></label>
                                    <textarea id="content" class="form-control" name="content" rows="5"
                                              style="width: 250%"></textarea>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div class="col-lg-4">
                                                <div style="padding-top:5px;">
                                                    <div class="col-lg-4">
                                                        <input id="addGiftBtn" type="button" class="btn btn-yellow"
                                                               value="选择奖励">
                                                    </div>
                                                    <input id="sendBtn" type="button" class="btn btn-blue" value="提交">
                                                    <input id="resetBtn" type="reset" class="btn btn-danger" value="重置">
                                                </div>
                                            </div>
                                        </div>
                                        <br>
                                        <table class="table table-striped table-hover table-bordered" width="100%"
                                               id="tb_gift">
                                            <thead>
                                            <tr role="row">
                                                <th>奖励ID</th>
                                                <th>奖励数量</th>
                                            </tr>
                                            </thead>
                                        </table>
                                    </div>
                                </form>
                            </div>
                            <!-- 查询 over-->
                        </div>
                        <!-- PC端 -->
                        <div id="profile0" class="tab-pane">
                            <!-- 查询 start-->
                            <div class="widget radius-bordered">
                                <div class="widget-header bordered-bottom">
                                    <span class="widget-caption">指定发奖</span>
                                </div>
                                <form id="bform">
                                    <div class="widget-body">
                                        <div class="row">
                                            <div class="col-lg-4">
                                                <div>
                                                    <label><strong>玩家昵称</strong></label>
                                                    <input name="nick" type="text" class="form-control">
                                                </div>
                                            </div>
                                            <br>
                                            <input id="kickBtn" type="button" class="btn btn-danger" value="踢人">
                                        </div>
                                        <br>
                                        <br>
                                        <div class="row">
                                            <div class="col-lg-4">
                                                <div>
                                                    <label><strong>邮件标题</strong></label>
                                                    <input name="title" type="text" class="form-control">
                                                </div>
                                            </div>
                                            <br>
                                            <div class="col-lg-4">
                                                <div>
                                                    <label><strong>指定名单</strong></label>
                                                    <input name="xlsfile" type="file" id="upfile" value="上传名单附件">
                                                </div>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div class="col-lg-4">
                                                <label><strong>邮件内容</strong></label>
                                    <textarea id="bcontent" class="form-control" name="content" rows="5"
                                              style="width: 250%"></textarea>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="row">
                                            <div class="col-lg-4">
                                                <div style="padding-top:5px;">
                                                    <div class="col-lg-4">
                                                        <input id="addGiftBtnB" type="button" class="btn btn-yellow"
                                                               value="选择奖励">
                                                    </div>
                                                    <input id="bsendBtn" type="button" class="btn btn-blue" value="提交">
                                                    <input id="bresetBtn" type="reset" class="btn btn-danger"
                                                           value="重置">
                                                </div>
                                            </div>
                                        </div>
                                        <br>
                                        <table class="table table-striped table-hover table-bordered" width="100%"
                                               id="tb_gift_B">
                                            <thead>
                                            <tr role="row">
                                                <th>奖励ID</th>
                                                <th>奖励数量</th>
                                            </tr>
                                            </thead>
                                        </table>
                                    </div>
                                </form>
                            </div>
                            <!-- 查询 over-->
                        </div>
                    </div>
                </div>
                <!--AddGift_A-->
                <div class="modal fade" id="addGiftModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                    &times;
                                </button>
                                <h4 class="modal-title" id="giftModalLabel">
                                    选择奖励
                                </h4>
                            </div>
                            <div class="modal-body">
                                <form class="form-horizontal" role="form" id="giftform">
                                    <div class="form-group">
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label no-padding-right">奖励ID</label>
                                            <div class="col-sm-9">
                                                <input id="goodsId" type="text" class="form-control" name="goodsId"
                                                       placeholder="奖励ID" style="width: 200px"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label no-padding-right">数量</label>
                                            <div class="col-sm-9">
                                                <input id="goodsNum" type="text" class="form-control" name="goodsNum"
                                                       placeholder="数量" style="width: 200px"/>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-danger" data-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-blue" id="gift_add_btn">确定</button>
                            </div>
                        </div><!-- /.modal-content -->
                    </div><!-- /.modal -->
                </div>
                <!--AddGift_A-->

                <!--AddGift_B-->
                <div class="modal fade" id="addGiftModalB" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                    &times;
                                </button>
                                <h4 class="modal-title" id="giftModalLabelB">
                                    选择奖励
                                </h4>
                            </div>
                            <div class="modal-body">
                                <form class="form-horizontal" role="form" id="giftformb">
                                    <div class="form-group">
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label no-padding-right">奖励ID</label>
                                            <div class="col-sm-9">
                                                <input id="goodsIdB" type="text" class="form-control" name="goodsId"
                                                       placeholder="奖励ID" style="width: 200px"/>
                                                <%--<select name="goodsId" class="el-select" style="width:50%;">
                                                    <c:forEach items="${goods}" var="node">
                                                        <option value="${node.goodsId}">${node.goodsName}</option>
                                                    </c:forEach>
                                                </select>--%>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label no-padding-right">数量</label>
                                            <div class="col-sm-9">
                                                <input id="goodsNumB" type="text" class="form-control" name="goodsNum"
                                                       placeholder="数量" style="width: 200px"/></div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-danger" data-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-blue" id="gift_add_btn_B">确定</button>
                            </div>
                        </div><!-- /.modal-content -->
                    </div><!-- /.modal -->
                </div>
                <!--AddGift_B-->
            </div>
            <!-- /真正内容 -->
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
<!--Page Related Scripts-->
<script src="<%=basePath %>assets/js/datatable/jquery.dataTables.min.js"></script>
<script src="<%=basePath %>assets/js/datatable/ZeroClipboard.js"></script>
<script src="<%=basePath %>assets/js/datatable/dataTables.tableTools.min.js"></script>
<script src="<%=basePath %>assets/js/datatable/dataTables.bootstrap.min.js"></script>
<script src="<%=basePath %>assets/js/bootstrap.min.js"></script>
<script src="<%=basePath %>scripts/fileUpload/jquery.uploadify-3.1.0.min.js?r=<%=new Random().nextInt() %>"></script>
<script src="<%=basePath %>assets/js/jquery.form.js"></script>
<!--Jquery Select2-->
<script src="<%=basePath %>assets/js/select2/select2.js"></script>
<script src="<%=basePath %>assets/js/bootbox/bootbox.js"></script>

<script>

    $(document).ready(function () {
        //修改左侧菜单样式
        $(".sidebar-menu .menu-li").removeClass("active");
        $(".sidebar-menu .submenu li").removeClass("active");
        $(".sidebar-menu .menu-li:eq(8)").addClass("active").addClass("open");
        $(".sidebar-menu .menu-li:eq(8) .submenu li:eq(1)").addClass("active");

    });

    $(".el-select").select2({
        placeholder: "----请选择----",
        language: "zh-CN",
        allowClear: true
    });

    var globalret = 0;
    var giftAry = new Array();
    var giftAryB = new Array();
    var i = 0;
    var j = 0;

    $('#uploadBtn').click(function () {
        $('#uploadModal').modal('show');
    });

    $('#addGiftBtn').click(function () {
        $('#addGiftModal').modal('show');
    });
    $('#addGiftBtnB').click(function () {
        $('#addGiftModalB').modal('show');
    });


    $('#gift_add_btn').click(function () {
        var gift = {goodsId: "", goodsNum: ""};
        var giftinfo = serializeForm($('#giftform'), null);
        bootbox.confirm({
            message: "请确认是否添加奖励ID:" + giftinfo.goodsId + " 数量:" + giftinfo.goodsNum,
            buttons: {
                confirm: {
                    label: '确认',
                    className: 'btn-blue'
                },
                cancel: {
                    label: '取消',
                    className: 'btn-danger'
                }
            },
            callback: function (result) {
                if (result) {
                    if (giftinfo.goodsNum > 0) {
                        gift.goodsId = giftinfo.goodsId;
                        gift.goodsNum = giftinfo.goodsNum;
                        giftAry.push(gift);
                        i = i + 1;
                        $('#tb_gift').append('<tr><td>' + giftinfo.goodsId + '</td><td>' + giftinfo.goodsNum + '</td><tr>');
                        $('#giftform').reset;
                        $('#addGiftModal').modal('hide');
                    } else {
                        bootbox.alert('奖励数量不能为零');
                    }
                }
            }
        });
    });

    $('#gift_add_btn_B').click(function () {
        var gift = {goodsId: "", goodsNum: ""};
        var giftinfo = serializeForm($('#giftformb'), null);
        bootbox.confirm({
            message: "请确认是否添加奖励ID:" + giftinfo.goodsId + " 数量:" + giftinfo.goodsNum,
            buttons: {
                confirm: {
                    label: '确认',
                    className: 'btn-blue'
                },
                cancel: {
                    label: '取消',
                    className: 'btn-danger'
                }
            },
            callback: function (result) {
                if (result) {
                    if (giftinfo.goodsNum > 0) {
                        gift.goodsId = giftinfo.goodsId;
                        gift.goodsNum = giftinfo.goodsNum;
                        giftAryB.push(gift);
                        j = j + 1;
                        $('#tb_gift_B').append('<tr><td>' + giftinfo.goodsId + '</td><td>' + giftinfo.goodsNum + '</td><tr>');
                        $('#giftformb').reset;
                        $('#addGiftModalB').modal('hide');
                    } else {
                        bootbox.alert('奖励数量不能为零');
                    }
                }
            }
        });
    });

    $('#sendBtn').click(function () {
        var ginfo = JSON.stringify(giftAry);
        $('#sform').ajaxSubmit({
            type: 'POST',
            url: '${base}/slgift/send?type=1&gift=' + ginfo,
            dataType: "json",
            success: function (data) {
                bootbox.alert(data.message);
                console.log(data.info);
                if (data.result != 4) {
                    reload_dt();
                }
            },
            error: function (XmlHttpRequest, textStatus, errorThrown) {
                console.log(XmlHttpRequest);
                console.log(textStatus);
                console.log(errorThrown);
            }
        });
    });

    $('#kickBtn').click(function () {
        $('#bform').ajaxSubmit({
            type: 'POST',
            url: '${base}/slgift/kick',
            dataType: "json",
            success: function (data) {
                console.info(data.msgcode);
                bootbox.alert(data.message);
                if (data.result != 4) {
                    reload_dt();
                }
            },
            error: function (XmlHttpRequest, textStatus, errorThrown) {
                console.log(XmlHttpRequest);
                console.log(textStatus);
                console.log(errorThrown);
            }
        });
    });

    $('#bsendBtn').click(function () {
        var ginfo = JSON.stringify(giftAryB);
        $('#bform').ajaxSubmit({
            type: 'POST',
            url: '${base}/slgift/send?type=2&gift=' + ginfo,
            dataType: "json",
            success: function (data) {
                bootbox.alert(data.message);
                if (data.result != 4) {
                    reload_dt();
                }
            },
            error: function (XmlHttpRequest, textStatus, errorThrown) {
                console.log(XmlHttpRequest);
                console.log(textStatus);
                console.log(errorThrown);
            }
        });
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

    $('#resetBtn').click(function () {
        reload_dt()
    });
    $('#bresetBtn').click(function () {
        reload_dt_B()
    });

    function reload_dt() {
        i = 0;
        giftAry.splice(0, giftAry.length);
        $("#tb_gift tbody").html("");
        $('#sform').reset;
    }
    function reload_dt_B() {
        j = 0;
        giftAryB.splice(0, giftAryB.length);
        $("#tb_gift_B tbody").html("");
        $('#bform').reset;
    }

</script>

</body>
</html>
