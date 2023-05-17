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
    
    <title>My JSP 'diarySave.jsp' starting page</title>
    
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
		var title = $("#title").val();
		//获取CKEDITOR数据
		var content = CKEDITOR.instances.content.getData();
		var typeId = $("#typeId").val();
		if( title==null || title==""){
			$("#error").html("标题不能为空");
			return false;
		}
		if( content==null || content==""){
			$("#error").html("内容不能为空");
			return false;
		}
		if(typeId==null||typeId==""){
			$("#error").html("请选择日志类别");
			return false;
		}
		return true;
	}
</script>
  <body>
<div class="data_list">
		<div class="data_list_title">
			<c:choose>
				<c:when test="${diary.diaryId != null}">
					<img src="${pageContext.request.contextPath}/images/diary_type_edit_icon.png"/>
					修改日志
				</c:when>
				<c:otherwise>
					<img src="${pageContext.request.contextPath}/images/diary_add_icon.png"/>
					写日志
				</c:otherwise>
			</c:choose>
		</div>
	<form action="diary?action=save" method="post" onsubmit="return checkForm()">
		<div>
			<div class="diary_title">
			<input type="text" id="title"  name="title" value="${diary.title}" class="input-xlarge"  
			style="margin-top:5px;height:30px;"  placeholder="日志标题"/>
			</div>
			<div>
				<textarea class="ckeditor" id="content" name="content">${diary.content}</textarea>
			</div>
			<div class="diary_type" >
					<select id="typeId" name="typeId">
						<option value="">请选择日志类别</option>
						<c:forEach var="diaryTypeCount" items="${diaryTypeCountList}">
							<option value="${diaryTypeCount.diaryTypeId}" ${diaryTypeCount.diaryTypeId==diary.typeId ? 'selected' : ''}>
							${diaryTypeCount.typeName}
							</option>
						</c:forEach>
					</select>
			</div>
			<div class="diary_btn">
				<input type="hidden" id="diaryId" name="diaryId" value="${diary.diaryId}">
				<input type="submit" class="btn btn-primary" value="保存"/>
				<button class="btn btn-primary" type="button" onclick="javascript:history.back()">返回</button>
				<font id="error" color="red">${error}</font>  
			</div>
		</div>
	</form>
</div>
  </body>
</html>













