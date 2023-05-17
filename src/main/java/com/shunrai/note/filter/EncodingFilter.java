package com.shunrai.note.filter;

import cn.hutool.core.util.StrUtil;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 请求乱码解决
 * 乱码情况：
 *    服务器默认的解析编码为ISO-8859-1,不支持中文
 * 解决方案：
 *    POST请求：
 *      无论是什么版本的服务器，都会出现乱码，需要通过request.setCharacterEncoding("UTF-8)处理
 *    GET请求：
 *      Tomcat8及以上版本，不会乱码
 *      Tomcat7及以下版本，需要单独处理
 *         new String(request.getParamater("xxx").getBytes("ISO-8859-1"),"UTF-8")
 */
@WebFilter(filterName = "Filter" ,urlPatterns = "/*")//过滤所有资源
public class EncodingFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request1=(HttpServletRequest) request;
        HttpServletResponse response1=(HttpServletResponse) response;
        //处理POST请求
         request1.setCharacterEncoding("UTF-8");
         //得到请求类型(POST|GET)
        String method = request1.getMethod();
         //如果是GET请求，则判断服务器版本
        if("GET".equalsIgnoreCase(method)){
            //得到服务器版本
            String serverInfo = request1.getServletContext().getServerInfo();//Apache Tomcat /7.0.79
            //通过截取字符串，得到具体版本号
            String version = serverInfo.substring(serverInfo.lastIndexOf("/")+1,serverInfo.indexOf("."));
            //判断服务器版本是否是7及以下
            if(version != null && Integer.parseInt(version)<8){
                //7及以下
                MyWapper myWapper = new MyWapper(request1);
                //放行资源
                chain.doFilter(myWapper,response1);
            }
        }
        chain.doFilter(request1, response1);
    }

    /**
     * 定义内部类（类的本质是request对象）
     * 继承HttpServletRequestWrapper包装类
     * 重写getParameter()方法
     */
    class MyWapper extends HttpServletRequestWrapper{

        private HttpServletRequest request;

        public MyWapper (HttpServletRequest request){
            super(request);
            this.request=request;
        }
        //重写，处理乱码问题
        @Override
        public String getParameter(String name) {
            String value = request.getParameter(name);
            if(StrUtil.isBlank(value)){
                return value;
            }
            try {
                value = new String(value.getBytes("ISO-8859-1"),"UTF-8");
            }catch (Exception e){
                e.printStackTrace();
            }
            return value;
        }
    }

    public void destroy() {
    }


}
