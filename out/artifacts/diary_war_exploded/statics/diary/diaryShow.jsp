<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'diaryShow.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  <script type="text/javascript">
	function diaryDelete(diaryId){
		if(confirm("您确定要删除这个日志吗？")){
			window.location="diary?action=delete&diaryId="+diaryId;
		}
	}
</script>
  <body>
   <div class="data_list">
	<div class="data_list_title">
	<img src="${pageContext.request.contextPath}/images/diary_show_icon.png"/>
	日记信息</div>
	<div>
		<div class="diary_title"><h3>${diary.title }</h3></div>
		<div class="diary_info">
			发布时间：『 <fmt:formatDate value="${diary.releaseDate }" type="date" pattern="yyyy-MM-dd HH:mm:ss"/>』
			&nbsp;&nbsp;日志类别：${diary.typeName}
		</div>
		<div class="diary_content">
			${diary.content }
		</div>
		<div class="diary_action">
			<button class="btn btn-primary" type="button" onclick="javascript:window.location='diary?action=preSave&diaryId=${diary.diaryId}'">修改日志</button>
			<button class="btn btn-primary" type="button" onclick="javascript:history.back()">返回</button>
			<button class="btn btn-danger" type="button" onclick="diaryDelete(${diary.diaryId})">删除日志</button>
		</div>
	</div>
</div>
  </body>
</html>
