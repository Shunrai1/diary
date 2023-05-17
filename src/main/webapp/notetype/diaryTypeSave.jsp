<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isELIgnored="false" %>
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
    <title>My JSP 'diaryTypeSave.jsp' starting page</title>
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
		function checkForm(){
			var typeName =  $("#typeName").val();
			if(typeName==null || typeName==""){
				$("#error").html("类别名称不能为空！");
				return false;
			}
			return true;
		}
	</script>
  <body>
   <div class="data_list">
	<div class="data_list_title">
	<c:choose>
		<c:when test="${diaryType.type_id !=null}">
			<img src="/diary_war_exploded/statics/images/diary_type_edit_icon.png"/>
			修改日志类别
		</c:when>
		<c:otherwise>
			<img src="/diary_war_exploded/statics/images/diaryType_add_icon.png"/>
			添加日志类别
		</c:otherwise>
	</c:choose>
	</div>
	<form action="DiaryType?actionName=save" method="post" onsubmit="return checkForm()">
		<div class="diaryType_form">
		<!-- 修改需要的隐藏域 -->
			<input type="hidden" id="diaryTypeId" name="diaryTypeId" value="${diaryType.type_id}"/>
			<table align="center">
				<tr>
					<td>类别名称：</td>
					<td>
					<input type="text" id="typeName"  name="typeName" value="${diaryType.type_name}"
					style="margin-top:5px;height:30px;" />
					</td>
				</tr>
				<tr>
					<td><input type="submit" class="btn btn-primary" value="保存"/></td>
					<td>
					<button class="btn btn-primary" type="button" 
					onclick="javascript:history.back()">返回</button>&nbsp;&nbsp;
					<span id="error" style="color: red; ">${error}</span>
					</td>
				</tr>
			</table>
		</div>
	</form>
</div>
  </body>
</html>
