package com.shunrai.note.web;

import com.shunrai.note.dao.DiaryDao;
import com.shunrai.note.po.Diary;
import com.shunrai.note.po.User;
import com.shunrai.note.service.UserService;
import com.shunrai.note.vo.ResultInfo;
import org.apache.commons.io.FileUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserServlet", value = "/user")
@MultipartConfig //文件相关注解
public class UserServlet extends HttpServlet {
    private UserService userService=new UserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //接受用户行为
        String actionName= request.getParameter("actionName");
        //判断用户行为，调用对应方法
        if("login".equals(actionName)){
            //用户登录
            userLogin(request,response);
        }else if("logout".equals(actionName)){
            //用户退出
            userLogout(request,response);
        }else if("userCenter".equals(actionName)){
            //设置个人中心导航高亮
            request.setAttribute("menu_page","user");
            //进入个人中心
            userCenter(request,response);
        }else if("userHead".equals(actionName)){
            //加载头像
            userHead(request,response);
        }else if ("checkNick".equals(actionName)){
            //验证昵称的唯一性
            checkNick(request,response);
        }else if("updateUser".equals(actionName)){
                //修改用户信息
            updateUser(request, response);

        }
    }

    /**
     * 修改用户信息
     * @param request
     * @param response
     */
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用service层方法，传递request对象作为参数，返回resultInfo对象
        ResultInfo<User> resultInfo=userService.updateUser(request);
        //将resultInfo对象存到request作用域中
        request.setAttribute("resultInfo",resultInfo);
        //请求转发到个人中心页面
        request.getRequestDispatcher("user?actionName=userCenter").forward(request,response);

    }

    /**
     * 验证昵称的唯一性
     * @param request
     * @param response
     */
    private void checkNick(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nick=request.getParameter("nick");//获取参数（昵称）
        //得到用户ID
        User user = (User) request.getSession().getAttribute("user");
        //调用Service层方法，得到返回的结果
        Integer code =userService.checkNick(nick,user.getUser_id());
        //通过字符输入流将结果响应给前台的Ajax的回调函数
        response.getWriter().write(code+"");
        //关闭资源
        response.getWriter().close();
    }

    /**
     * 加载头像
     * @param request
     * @param response
     */
    private void userHead(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String head=request.getParameter("imageName");
        //得到图片路径
        String realPath=request.getServletContext().getRealPath("/WEB-INF/upload/");
        //得到file对象
        File file = new File(realPath+"/"+head);
        //通过截取，得到图片后缀
        String pic = head.substring(head.lastIndexOf(".")+1);
        //不同图片后缀，不同的响应类型
        if("PNG".equalsIgnoreCase(pic)){
            response.setContentType("image/png");
        }else if("JPG".equalsIgnoreCase(pic)){
            response.setContentType("image/jpg");
        }else if("GIF".equalsIgnoreCase("image/gif")){
            response.setContentType("image/gif");
        }
        //利用FileUtils的copyFile()方法，将图片拷贝给浏览器
        FileUtils.copyFile(file,response.getOutputStream());
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.doGet(request,response);
    }

    private void userLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName=request.getParameter("userName");
        String userPwd=request.getParameter("password");
        ResultInfo<User> resultInfo=userService.userLogin(userName,userPwd);
        //登录成功
        if(resultInfo.getCode()==1){
            request.getSession().setAttribute("user",resultInfo.getResult());
            //判断用户是否选择记住密码（rem的值为1）
            String rem = request.getParameter("rem");
            if("1".equals(rem)){
                //得到Cookie对象
                Cookie cookie=new Cookie("user",userName+"-"+userPwd);
                //设置失效时间
                cookie.setMaxAge(3*24*60*60);
                //响应给客户端
                response.addCookie(cookie);
            }
            else {
                //如果否，清空原有的cookie对象
                Cookie cookie = new Cookie("user",null);
                //删除cookie，设置maxage为0
                cookie.setMaxAge(0);
                //响应给客户端
                response.addCookie(cookie);
            }
            //重定向跳转到index页面
            response.sendRedirect("index");
        }else{

            request.setAttribute("resultInfo",resultInfo);
            request.getRequestDispatcher("login.jsp").forward(request,response);
        }
    }

    /**
     * 用户退出
     * @param request
     * @param response
     * @throws IOException
     */
    private void userLogout(HttpServletRequest request, HttpServletResponse response) throws IOException{
        request.getSession().invalidate();//删除session
        //删除cookie
        Cookie cookie = new Cookie("user",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        //重定向到登录页面
        response.sendRedirect("login.jsp");
    }

    /**
     * 进入个人中心
     * @param request
     * @param response
     */
    private void userCenter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("changePage","user/info.jsp");
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}
