package com.shunrai.note.filter;

import com.shunrai.note.po.User;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 非法访问拦截
 *   拦截的资源：
 *     所有资源 /*
 *   需要被放行的资源：
 *     1，指定页面放行（login.jsp register.jsp等）
 *     2. 静态资源放行（js、css、images等）
 *     3. 指定行为放行（用户无需登陆既可执行的操作等）
 *     4. 登陆状态放行（判断session是否存在user对象）
 *
 *  免登录（自动登录）
 *     通过Cookie和Session对象实现
 *     当用户处于未登录状态，且去请求需要登录才能访问的资源时，调用自动登录功能
 *     目的：
 *       让用户处于登录状态
 *      实现：
 *        从Cookie对象中获取用户的姓名与密码，自动执行登录操作
 */
@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request1, ServletResponse response1, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request =(HttpServletRequest)request1;
        HttpServletResponse response=(HttpServletResponse)response1;
        //得到访问路径
        String path = request.getRequestURI();
       // 1，指定页面放行（login.jsp register.jsp等）
        if(path.contains("/login.jsp")){
            chain.doFilter(request,response);
            return;
        }
      //  2.静态资源放行（js、css、images等）
        if(path.contains("/static")){
            chain.doFilter(request,response);
            return;
        }
        //3.指定行为放行（用户无需登陆既可执行的操作等）
        if(path.contains("/user")){
            //得到用户行为
            String actionName = request.getParameter("actionName");
            if("login".equals(actionName)){
                chain.doFilter(request,response);
                return;
            }
        }
        //  4. 登陆状态放行（判断session是否存在user对象）
        // 获取Session作用域中user对象
        User user = (User) request.getSession().getAttribute("user");
        if(user!=null){
            chain.doFilter(request,response);
            return;
        }

        /**
         * 免登录（自动登录）
         */
        Cookie[] cookies =request.getCookies();
        if(cookies!=null && cookies.length>0){
            for (Cookie cookie:cookies){
                if("user".equals(cookie.getName())){
                    String value = cookie.getValue();
                    String[] val = value.split("-");
                    String userName = val[0];
                    String userPwd = val[1];
                    String url="user?actionName=login&rem=1&userName="+userName+"&password="+userPwd;
                    request.getRequestDispatcher(url).forward(request,response);
                    return;
                }
            }
        }
        //拦截请求，重定向跳转到登录界面
        response.sendRedirect("login.jsp");
    }
}
