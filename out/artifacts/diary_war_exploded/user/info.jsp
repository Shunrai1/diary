<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
    <script type="text/javascript" src="https://code.jquery.com/jquery-1.11.0.js"></script>
</head>
<body>
<div class="data_list">
    <div class="data_list_title">
        <img src="/diary_war_exploded/statics/images/user_edit_icon.png"/>
        个人信息设置
    </div>

    <div class="container-fluid">
        <div class="row">
            <div class="col-md-4">
                <img src="user?actionName=userHead&imageName=${user.head}" width="200" height="280"/>
            </div>
            <div class="col-md-8">
                <%-- 表单类型：method="post" enctype="multipart/form-data"--%>
                <form action="user" method="post" enctype="multipart/form-data" >
                     <%--设置隐藏存放用户行为的actionName--%>
                    <input type="hidden" name="actionName" value="updateUser">
                    <table width="100%">
                        <tr>
                            <td width="20%">更改头像：</td>
                            <td><input type="file" id="imagePath" name="img"/></td>
                        </tr>
                        <tr>
                            <td>我的昵称：</td>
                            <td><input type="text" id="nickName" name="nick" value="${user.nick}"
                                       style="margin-top:5px;height:30px;"/></td>
                        </tr>
                        <tr>
                            <td valign="top">我的心情：</td>
                            <td>
							<textarea id="mood" name="mood" rows="10" style="width: 100%">${user.mood}</textarea>
                            </td>
                        </tr>
                        <tr>
                            &nbsp;<td><button class="btn btn-primary" type="submit" id="btn" onclick="return updateUser()">保存</button></td>
                            <td>
                                <button class="btn btn-primary" type="button" onclick="javascript:history.back()">返回</button>
                                <span id="msg" style="color: red; font-size: 12px ">${resultInfo.msg}</span>
<%--                                <div id="div1">nihao</div>--%>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
</body>

<script type="text/javascript" >
    $("#nickName").blur(function(){
        $("#nickName").css("background-color","#FFFFFF");//失去焦点，改变颜色
        //获取昵称文本框的值
        var nickName= $("#nickName").val();
        // alert(nickName);
        //判断值是否为空
        if(nickName==null||nickName===""){
            $("#msg").html("用户昵称不能为空!")
            $("#btn").prop("disabled",true)//按钮禁用
            return;
        }
        //不为空，判断昵称是否做了修改
        var nick='${user.nick}';
        //没有修改
        if(nickName==nick){
            return;
        }
        //昵称做了修改
        //发送Ajax请求后台，验证昵称是否可用
        $.ajax({
            type:"post",
            url:"user",
            data:{
                actionName:"checkNick",
                nick:nickName
            },
            success:function (code){
                //如果可用，清空提示信息，按钮可用
                if(code==0){
                    $("#msg").html("")
                    $("#btn").prop("disabled",false);
                }else {//如果不可用
                    $("#msg").html("该昵称已经存在，请重新输入！")
                    $("#btn").prop("disabled",true);
                }
            },
            error:function (){
                alert("Ajax回调失败")
            }
        })

    }).focus(function (){
        $("#nickName").css("background-color","#FFFFCC");//获得焦点，改变颜色
        $("#msg").html("")
        $("#btn").prop("disabled",false);
    });

    /**
     * 表单提交校验
     *    满足条件，返回TRUE，表示提交表单
     *    不满足条件，返回FALSE，表示不提交表单
     * @returns {boolean}
     */
    function updateUser(){
        var nickName = $("#nickName").val();
        if(nickName==null || nickName==""){
            $("#msg").html("昵称不能为空！");
            return false;
        }
        if(resultInfo.code== 1 ){
            alert("保存成功");
        }
        alert("保存成功");
        return true;
    }
</script>
</html>
