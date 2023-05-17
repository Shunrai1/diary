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
	<title>My JSP 'userSave.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		function checkForm(){
			var nickName = $("#nickName").val();
			if(nickName==null || nickName==""){
				$("#error").html("昵称不能为空！");
				return false;
			}
			return true;
		}
	</script>
</head>
<body>
<div class="data_list">
	<div class="data_list_title">
		<img src="${pageContext.request.contextPath}/images/user_edit_icon.png"/>
		个人信息设置
	</div>

	<div class="container-fluid">
		<div class="row">
			<div class="col-md-4">
				<img src="<%=path%>/userImages/${currentUser.imageName}" width="200" height="240"/>
			</div>
			<div class="col-md-8">
				<form action="user?action=save" method="post" enctype="multipart/form-data" onsubmit="return checkForm()">
					<table width="100%">
						<tr>
							<td width="20%">更改头像：</td>
							<td><input type="file" id="imagePath" name="imagePath"/></td>
						</tr>
						<tr>
							<td>我的昵称：</td>
							<td><input type="text" id="nickName" name="nickName" value="${currentUser.nickName}"
									   style="margin-top:5px;height:30px;"/></td>
						</tr>
						<tr>
							<td valign="top">我的心情：</td>
							<td>
							<textarea id="mood" name="mood" rows="10" style="width: 100%">${currentUser.mood}
							</textarea>
							</td>
						</tr>
						<tr>
							<td><button class="btn btn-primary" type="submit">保存</button></td>
							<td>
								<button class="btn btn-primary" type="button" onclick="javascript:history.back()">返回</button>
								&nbsp;&nbsp;<span id="error" style="color: red; ">${error }</span>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</div>
</body>
</html>
