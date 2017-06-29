<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<ul class="nav sidebar-menu">
    <!--Dashboard-->
    <li class="active menu-li">
        <a href="<%=basePath %>index">
            <i class="menu-icon glyphicon glyphicon-home"></i>
            <span class="menu-text"> 首页 </span>
        </a>
    </li>

    <!--SLG管理-->
    <li class="menu-li">
        <a href="#" class="menu-dropdown">
            <i class="menu-icon fa fa-bar-chart-o"></i>
            <span class="menu-text" style="font-weight:bold;"> SLG统计 </span>
            <i class="menu-expand"></i>
        </a>
        <ul class="submenu">
            <li><a href="<%=basePath %>slgaccount"><span class="menu-text">账号统计</span></a></li>
            <li><a href="<%=basePath %>slguserpreserve"><span class="menu-text">用户留存</span></a></li>
            <li><a href="<%=basePath %>slglose/list"><span class="menu-text">用户流失</span></a></li>
            <li><a href="<%=basePath %>slgonline/hour"><span class="menu-text">分时在线(按小时)</span></a></li>
            <li><a href="<%=basePath %>slgonline/day"><span class="menu-text">分时在线(按天)</span></a></li>
            <li><a href="<%=basePath %>slgpay"><span class="menu-text">充值统计</span></a></li>
            <li><a href="<%=basePath %>slgpay/list"><span class="menu-text">充值排行</span></a></li>
            <li><a href="<%=basePath %>slgrank/list"><span class="menu-text">领主排行</span></a></li>
            <li><a href="<%=basePath %>slglevel/slguserlv"><span class="menu-text">领主统计</span></a></li>
            <li><a href="<%=basePath %>slglevel/slgbdlv"><span class="menu-text">建筑统计</span></a></li>

        </ul>
    </li>
    <!--SLG GM管理-->
    <li class="menu-li">
        <a href="#" class="menu-dropdown">
            <i class="menu-icon fa fa-bar-chart-o"></i>
            <span class="menu-text" style="font-weight:bold;"> GM操作 </span>
            <i class="menu-expand"></i>
        </a>
        <ul class="submenu">
            <li><a href="<%=basePath %>slgmail/add"><span class="menu-text">新增邮件</span></a></li>
            <li><a href="<%=basePath %>slgmail/notice"><span class="menu-text">添加公告</span></a></li>
            <li><a href="<%=basePath %>slgift/list"><span class="menu-text">发奖</span></a></li>
            <li><a href="<%=basePath %>slguser/list"><span class="menu-text">封禁账号</span></a></li>
            <li><a href="<%=basePath %>slgimg/list"><span class="menu-text">头像审核</span></a></li>

            <%--<li><a href="<%=basePath %>slgmail/list"><span class="menu-text">已发邮件</span></a></li>
            <li><a href="<%=basePath %>slguser/list"><span class="menu-text">查询信息</span></a></li>--%>
        </ul>
    </li>

    
</ul>
