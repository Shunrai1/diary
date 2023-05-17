<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isELIgnored="false" %>
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
<%--	  高德定位--%>
	  <script language="javascript" src="http://webapi.amap.com/maps?v=1.3&amp;key=e8496e8ac4b0f01100b98da5bde96597"></script>
	  <script src="../shujubaobiao/address.js"></script>
	  <style>

		  #ip:active{

			  background:olive;

		  }

		  #ip:focus{

			  background:olive;

		  }

	  </style>
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
  <div id='container' style="display: none;" ></div>
  <script type="text/javascript">
	  window._AMapSecurityConfig = {
		  securityJsCode:'22514f86bb5b0f1bb026a4fe6aae5030',
	  }
  </script>
  <script type="text/javascript" src="https://webapi.amap.com/maps?v=2.0&key=c0e143eace6684b4dfbe65f0934ada1f&plugin=AMap.Geocoder"></script>
  <script type="text/javascript">
	  	  var lng;
	  	  var lat;
		  var lnglat;//经纬度
		  var address;//地址
		  var geocoder = new AMap.Geocoder({
			  city: "010", //城市设为北京，默认：“全国”
			  radius: 500 //范围，默认：500
		  });
		  var map = new AMap.Map('container', {
			  resizeEnable: true,

		  });
		  AMap.plugin('AMap.Geolocation', function () {
			  var geolocation = new AMap.Geolocation({
				  enableHighAccuracy: true,//是否使用高精度定位，默认:true
				  timeout: 10000,          //超过10秒后停止定位，默认：5s
				  buttonPosition: 'RB',    //定位按钮的停靠位置
				  buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
				  zoomToAccuracy: true,   //定位成功后是否自动调整地图视野到定位点

			  });
			  map.addControl(geolocation);
			  geolocation.getCurrentPosition(function (status, result) {
				  if (status == 'complete') {
					  lnglat = [result.position.lng, result.position.lat];
					  lng = result.position.lng;
					  lat = result.position.lat;
					  //写入经纬度
					  if(lng.equals("")||lng==null){//浏览器不支持http定位
						  document.getElementById("Lng").value= 1.0;
						  document.getElementById("Lat").value = 1.0;
					  }else{
						  document.getElementById("Lng").value= lng;
						  document.getElementById("Lat").value = lat;
					  }
					  // 根据经纬度获取详细信息
					  geocoder.getAddress(lnglat, function (status, result1) {
						  if (status === 'complete' && result1.regeocode) {
							  address = result1.regeocode.formattedAddress;
							  console.log(result1);
							  //写入地址
							  if(address.equals("")||address==null){//浏览器不支持http定位
								  document.getElementById("Address").value = "http定位失败";
							  }
							  else {
								  document.getElementById("Address").value = address;
							  }
						  } else {
							  console.log('根据经纬度查询地址失败')
						  }
					  });
				  } else {
					  console.error(result);
				  }
			  });
		  });



  </script>

<div class="data_list">
		<div class="data_list_title">
			<c:choose>
				<c:when test="${diary.note_id != null}">
					<img src="/diary_war_exploded/statics/images/diary_type_edit_icon.png"/>
					修改日志
				</c:when>
				<c:otherwise>
					<img src="/diary_war_exploded/statics/images/diary_add_icon.png"/>
					写日志
				</c:otherwise>
			</c:choose>
		</div>
	<form action="Diary?actionName=save" method="post" onsubmit="return checkForm()">
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
							<option value="${diaryTypeCount.type_id}" ${diaryTypeCount.type_id==diary.type_id ? 'selected' : ''}>
							${diaryTypeCount.type_name}
							</option>
						</c:forEach>
					</select>
			</div>

			<div class="diary_btn">
				<input type="hidden" id="diaryId" name="diaryId" value="${diary.note_id}">
				<input type="hidden" id="Lng" name="lng" value="1.0">
				<input  type="hidden" id="Lat" name="lat" value="1.0">
				<input type="hidden" id="Address" name="address" value="供不支持http定位使用">
				<input type="submit" class="btn btn-primary" value="保存"/>
				<button class="btn btn-primary" type="button" onclick="javascript:history.back()">返回</button>
				<span id="error" style="color: red; ">${error}</span>
			</div>
		</div>
	</form>
</div>
  </body>
</html>













