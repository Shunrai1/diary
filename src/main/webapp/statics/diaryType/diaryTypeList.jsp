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
    
    <title>My JSP 'diaryTypeList.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  <body>
    <div class="data_list">
		<div class="data_list_title">
		<img src="${pageContext.request.contextPath}/images/list_icon.png"/>
		日记类别列表
		<span class="diaryType_add">
			<button class="btn btn-mini btn-success" type="button"
			onclick="javascript:window.location='diaryType?action=preSave'">添加日志类别</button>
		</span>
		</div>
		<div>
			<table class="table table-hover table-striped"><!-- striped条纹渐变色 -->
			  <tr>
			  	<th>编号</th>
			  	<th>类别名称</th>
			  	<th>操作</th>
			  </tr>
	  <c:forEach var="diaryType" items="${diaryTypeList}">
	  	<tr>
	  		<td>${diaryType.diaryTypeId}</td>
	  		<td>${diaryType.typeName}</td>
	  		<td>
	  			<button class="btn btn-mini btn-info" type="button" 
	  			onclick="javascript:window.location='diaryType?action=preSave&diaryTypeId=${diaryType.diaryTypeId}'">
	  			修改</button>&nbsp;
	  			<button class="btn btn-mini btn-danger" type="button"
	  			onclick="diaryTypeDelete(${diaryType.diaryTypeId})">删除</button>
	  		</td>
	  	</tr>
	  </c:forEach>
			</table>
		</div>
		<div align="center"><font color="red">${error}</font></div>
	</div>
  </body>
  <script type="text/javascript">
		function diaryTypeDelete(diaryTypeId){
			if(confirm("您确定要删除这个日志类别吗？")){
				window.location="diaryType?action=delete&diaryTypeId="+diaryTypeId;
			}
		}
 </script>
</html>










