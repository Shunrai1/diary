<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>个人日志中心</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <!-- 引入bootstrap核心样式 -->
    <link rel="stylesheet" href="statics/bootstrap/css/bootstrap.css">
    <script src="statics/bootstrap/js/jquery-2.1.4.min.js"></script>
    <script src="statics/bootstrap/js/bootstrap.js"></script>
    <script src="statics/js/ckeditor/ckeditor.js"></script>
    <link rel="stylesheet" href="statics/style/diary.css" rel="stylesheet">
</head>

<style type="text/css">
    body {
        padding-top: 60px;
        padding-bottom: 40px;
    }
    .container-content{
        /* border:1px solid red; */
    }
</style>
<body>
<!-- navbar:导航栏;  navbar-inverse:逆变化导航边黑色;navbar-fixed-top:导航固定到最上方;-->
<nav class="navbar navbar-inverse navbar-fixed-top">
    <!-- container:内部容器 ;容器里面要放东西的-->
    <div class="container">
        <!-- 头标题 文字大一号-->
        <div class="navbar-header">
            <a class="navbar-brand" href="#">我的日记本</a>
        </div>
        <div>
            <!-- 导航内容 -->
            <ul class="nav navbar-nav">
                <li <c:if test="${menu_page=='index'}">class="active"</c:if>><a href="index?all=true"><i class="glyphicon glyphicon-home"></i>&nbsp;主页</a></li>
                <li <c:if test="${menu_page=='note'}">class="active"</c:if>><a href="Diary?actionName=preSave"><i class="glyphicon glyphicon-pencil"></i>&nbsp;写日志</a></li>
                <li <c:if test="${menu_page=='type'}">class="active"</c:if>><a href="DiaryType?actionName=list"><i class="glyphicon glyphicon-book"></i>&nbsp;日志分类管理</a></li>
                <li <c:if test="${menu_page=='user'}">class="active"</c:if>><a href="user?actionName=userCenter"><i class="glyphicon glyphicon-user"></i>&nbsp;个人中心</a></li>
                <li <c:if test="${menu_page=='report'}">class="active"</c:if>><a href="Report?actionName=report"><i class="glyphicon glyphicon-user"></i>&nbsp;数据报表</a></li>
            </ul>
            <form name="myForm" class="navbar-form pull-right" method="post" action="index?all=true">
                <input class="span2" id="s_title" name="s_title"  type="text"  placeholder="日志标题搜索" value="${s_title}">
                <button type="submit" class="btn" onkeydown="if(event.keyCode==13) myForm.submit()">
                    <i class="glyphicon glyphicon-search"></i>&nbsp;搜索日志</button>
            </form>
        </div>
    </div>
</nav>

<!-- 主内容 -->
<div class="container container-content">
    <div class="row">
        <!-- 12栅格系统  流式的按照百分比设计的-->
        <%--动态包含页面--%>
        <div class="col-md-9">
<%--            通过后台设置动态显示的页面，通过包含加载进来--%>
<%--               如果获取到后台设置的值，则显示；如果为，则为默认--%>
               <c:if test="${empty changePage}">
                   <jsp:include page="note/list.jsp"></jsp:include>
               </c:if>
               <c:if test="${!empty changePage}">
                   <jsp:include page="${changePage}"></jsp:include>
               </c:if>

        </div>
        <div class="col-md-3">
            <div class="data_list">
                <div class="data_list_title">
                    <img src="statics/images/user_icon.png"/>
                    个人中心
                    <a href="user?actionName=logout">退出</a>
                </div>
                <div class="user_image">
                    <img src="user?actionName=userHead&imageName=${user.head}" width="200" height="240"/>
                </div>
                <div class="nickName">${user.nick}</div>
                <div class="userSign">(${user.mood})</div>
            </div>
            <div class="data_list">
                <div class="data_list_title">
                    <img src="statics/images/byType_icon.png"/>
                    日志类别
                </div>
                <div class="datas">
                    <ul>
                        <c:forEach var="diaryTypeCount" items="${diaryTypeCountList}">
                            <li>
								<span>
									<a href="index?s_typeId=${diaryTypeCount.type_id}">${diaryTypeCount.type_name}(${diaryTypeCount.diaryCount })</a>
								</span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <div class="data_list">
                <div class="data_list_title">
                    <img src="statics/images/byDate_icon.png"/>
                    日志日期
                </div>
                <div class="datas">
                    <ul>
                        <c:forEach var="diaryCount" items="${diaryCountList}">
                            <li><span><a href="index?s_releaseDateStr=${diaryCount.pub_time}">${diaryCount.pub_time}(${diaryCount.diaryCount})</a></span></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>


</div>
</body>
</html>
