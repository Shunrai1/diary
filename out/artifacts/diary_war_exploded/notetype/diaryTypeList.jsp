<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="com.shunrai.note.po.DiaryType" %>
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

	<link rel="stylesheet" type="text/css" href="styles.css">


  </head>
  <body>
    <div class="data_list">
		<div class="data_list_title">
		<img src="/diary_war_exploded/statics/images/list_icon.png"/>
		日记类别列表
		<span class="diaryType_add">
			<button class="btn btn-mini btn-success" type="button"
			onclick="javascript:window.location='DiaryType?actionName=preSave'">添加日志类别</button>
		</span>
		</div>
		<div>
			<table class="table table-hover table-striped"><!-- striped条纹渐变色 -->
			  <tr align="center" >
<%--			  	<th>编号</th>--%>
			  	<th>类别名称</th>
			  	<th>操作</th>
			  </tr>
<%--	  <c:forEach var="diaryType" items="${diaryTypeList}">--%>
		  <%
			  ArrayList<DiaryType> list = (ArrayList<DiaryType>) request.getAttribute("diaryTypeList");
			  for (DiaryType diaryType : list) {
		  %>
	  	<tr>
<%--	  		<td>${diaryType.type_id}</td>--%>
<%--	  		<td>${diaryType.type_name}</td>--%>
<%--	            <td><%=diaryType.getType_id()%></td>--%>
	            <td><%=diaryType.getType_name()%></td>
	  		<td>
	  			<button class="btn btn-mini btn-info" type="button" 
	  			onclick="javascript:window.location='DiaryType?actionName=preSave&diaryTypeId=<%=diaryType.getType_id()%>'">
	  			修改</button>&nbsp;
	  			<button class="btn btn-mini btn-danger" type="button"
	  			onclick="diaryTypeDelete(<%=diaryType.getType_id()%>)">删除</button>
	  		</td>
	  	</tr>
		  <%
			  }
		  %>
<%--	  </c:forEach>--%>
			</table>
		</div>
		<div align="center"><span style="color: red; ">${error}</span></div>
	</div>
  </body>
  <script type="text/javascript">
		function diaryTypeDelete(diaryTypeId){
			if(confirm("您确定要删除这个日志类别吗？")){
				window.location="DiaryType?actionName=delete&diaryTypeId="+diaryTypeId;
			}
		}
 </script>
</html>










