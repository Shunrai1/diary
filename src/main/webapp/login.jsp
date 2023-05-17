<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="">
    
    <title>个人日记本登录</title>
	<link href="statics/bootstrap/css/bootstrap.css" rel="stylesheet">
	<script src="statics/bootstrap/js/jquery-2.1.4.min.js"></script>
	<script src="statics/bootstrap/js/bootstrap.js"></script>
	
<style type="text/css">
	  body {
        padding-top: 200px;
        padding-bottom: 40px;
        background-image: url('statics/images/bg.gif');
      }
      .myForm-heading{
      	text-align: center;
      }
      .myForm{
        max-width: 400px;
        padding: 19px 29px 0px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        border-radius: 7px;
        box-shadow: 16px -12px 10px rgba(221,131,116,0.5);
      }
      .myForm .myForm-heading,
      .myForm .checkbox {
        margin-bottom: 10px;
      }
      .myForm input[type="text"],
      .myForm input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }
	  .myBtn{
		left:100px;
		position:relative;
	  }
	</style>
</head>
  
<body>
	<div class="container">
     <form action="user" method="post" id="loginForm" name="myForm" class="myForm" >
        <h2 class="myForm-heading">日志本</h2>
          <%--actionName来在一个servlet中区分--%>
		  <input type="hidden" name="actionName" value="login">

         <input id="userName" name="userName" value="${resultInfo.result.uname}"  type="text" class="form-control" placeholder="用户名">
         <input id="password" name="password" value="${resultInfo.result.upwd}"   type="password" class="form-control" placeholder="密码" >
		 <input id="remember" name="rem" type="checkbox" value="1"/>&nbsp;<label>记住我</label>&nbsp;&nbsp;&nbsp;&nbsp;
		 <span id="msg" style="color: red;font-size: 12px;">${resultInfo.msg}</span>
		 <div class="myBtn">
			 <button class="btn btn-success" type="submit" onclick="checkLogin()">登 录</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			 <button class="btn btn-info" type="button" onclick="reset()">重 置</button>
		 </div>
		  <div>
			<p align="center" style="padding-top:15px;">版权所有  shunrai<br>
			 <a href="https://www.baidu.com/" target="_blank">https://www.baidu.com/</a>
			 </p>
		</div>
	 </form>
     </div> 
</body>
<script type="text/javascript">
	var userName = $("#userName").val();
	var password = $("#password").val();
	
	//重置
	function reset(){
		userName.val("");
		password.val("");
	}
   //验证
	function isEmpty(str) {
		if(str==null||str.trim()==""){
			return true;
		}
		return false;
	}


	function checkLogin(){
		var userName = $("#userName").val();
		var password = $("#password").val();
		if(isEmpty(userName)){
			//如果为空，则提示用户
			$("#msg").html("用户名称不能为空")
             return;
		}
		if(isEmpty(password)){
			//如果为空，则提示用户
			$("#msg").html("用户密码不能为空")
            return;
		}
	}
</script>
</html>
