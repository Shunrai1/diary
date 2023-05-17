package com.shunrai.note.web;

import com.shunrai.note.dao.DiaryDao;
import com.shunrai.note.dao.DiaryTypeDao;
import com.shunrai.note.po.Diary;
import com.shunrai.note.po.PageBean;
import com.shunrai.note.po.User;
import com.shunrai.note.util.DBUtil;
import com.shunrai.note.util.StringUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "IndexServlet", value = "/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //设置个人中心导航高亮
        request.setAttribute("menu_page","index");
        //设置首页动态包含页面
//        request.setAttribute("changePage","note/list.jsp");
//        request.getRequestDispatcher("index.jsp").forward(request,response);
        //s_开头的都是搜索功能，按相应的类型进行搜索
        HttpSession session=request.getSession();
        String s_typeId = request.getParameter("s_typeId");
        String s_releaseDateStr = request.getParameter("s_releaseDateStr");
        //当前页码数
        String page = request.getParameter("page");
        String all = request.getParameter("all");
        String s_title = request.getParameter("s_title");
        Diary diary = new Diary();

        if("true".equals(all)){
            session.removeAttribute("s_releaseDateStr");
            session.removeAttribute("s_typeId");
            if(StringUtil.isNotEmpty(s_title)){
                diary.setTitle(s_title);
            }
            session.setAttribute("s_title", s_title);
        }else{
            //这个时候就需要开始判断了！
            //new 一个diary专门封装查询条件
            if(StringUtil.isNotEmpty(s_typeId)){//这个类别id不唯空的话
                diary.setType_id(Integer.parseInt(s_typeId));
                //同时还要放到session作用域中，因为当我们点击了一个类别的时候，如果有下一页啥的是吧！
                session.setAttribute("s_typeId",s_typeId);
                session.removeAttribute("s_releaseDateStr");
                session.removeAttribute("s_title");
            }
            if(StringUtil.isNotEmpty(s_releaseDateStr)){//这个releaseDate不唯空的话也要设置进去
                s_releaseDateStr = new String(s_releaseDateStr.getBytes("ISO-8859-1"),"UTF-8");
                diary.setReleaseDateStr(s_releaseDateStr);
                session.setAttribute("s_releaseDateStr",s_releaseDateStr);
                session.removeAttribute("s_typeId");
                session.removeAttribute("s_title");
            }
            if(StringUtil.isEmpty(s_typeId)){
                //当他们类别都是空的到session中看看是否有数据
                Object obj = session.getAttribute("s_typeId");
                if(obj!=null)
                    diary.setType_id(Integer.parseInt((String)obj));
            }
            if(StringUtil.isEmpty(s_releaseDateStr)){
                //当他们类别都是空的到session中看看是否有数据
                Object obj = session.getAttribute("s_releaseDateStr");
                if(obj!=null)
                    diary.setReleaseDateStr((String)obj);
            }
            //模糊查询时候下一页的处理
            if(StringUtil.isEmpty(s_title)){
                Object obj=session.getAttribute("s_title");
                if(obj!=null)
                    diary.setTitle((String)obj);
            }
        }
        //判断是否为空，因为我们第一次请求page就是空的
        if(StringUtil.isEmpty(page))
            page="1";

        Connection conn = null;
        //每页显示的记录数
        int pageSize = 10;
        //分页所需要的参数，我们创建PageBean实体类
        PageBean pageBean = new PageBean(Integer.parseInt(page),pageSize);
        //获取当前登陆用户的id
        User user=(User) request.getSession().getAttribute("user");
        int user_id = user.getUser_id();
        try {
            conn = DBUtil.getConnection();
            //总记录数
            int total = DiaryDao.diaryCount(conn,diary,user_id);
            List<Diary> diaryList = DiaryDao.diaryList(conn,pageBean,diary,user_id);//这样就获取一页的数据
            //获取pagecode
            String pageCode = this.generatePagation(total, Integer.parseInt(page), pageSize);
            request.setAttribute("pageCode", pageCode);
            request.setAttribute("diaryList", diaryList);//每页展示数据和搜索功能
            session.setAttribute("diaryTypeCountList", DiaryTypeDao.diaryTypeCountList(conn,user_id));//每个类型下的日志数
            session.setAttribute("diaryCountList", DiaryDao.diaryCountList(conn,user_id));//按照年月日查询日志数
            request.setAttribute("changePage", "note/list.jsp");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtil.close(null,null,conn);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    /**
     * 获取分页code
     * @param totalNum  总记录数
     * @param currentPage 当前页码数
     * @param pageSize 每页显示的大小
     */
    private String generatePagation(int totalNum,int currentPage,int pageSize){
        //总页数                         100     %      10 = 0 总页数就是 totalNum / pageSize (100/10 =10 页);
        //                                                 取余不是0的话     如              (10   / 4) + 1 = 3 页
        int totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        //int pageCount = (int)Math.ceil(totalNum * 1.0 / pageSize);//向上取整
        StringBuffer pageCode=new StringBuffer();
//		pageCode.append("<li><a href='main?page=1'>首页</a></li>");
        //当前页是第一页的时候
        if(currentPage==1){
            pageCode.append("<li class='disabled'><a>首页</a></li>");
            //不显示上一页
            pageCode.append("<li class='disabled'><a>上一页</a></li>");
        }else{
            pageCode.append("<li><a href='index?page=1'>首页</a></li>");
            //显示上一页
            pageCode.append("<li><a href='index?page=" + (currentPage - 1) + "'>上一页</a></li>");
        }

        for(int i = currentPage - 2;i<= currentPage + 2; i++){
            if(i<1 || i > totalPage)
                continue;
            if(i==currentPage){
                //active显示当前页
                pageCode.append("<li class='active'><a href='#'>"+i+"</a></li>");
            }else{
                pageCode.append("<li><a href='index?page="+i+"'>"+i+"</a></li>");
            }
        }
        //当前页如果是最后一页
        if(currentPage==totalPage){
            //不显示下一页
            pageCode.append("<li class='disabled'><a>下一页</a></li>");
            pageCode.append("<li class='disabled'><a>尾页</a></li>");
        }else{
            //否则显示下一页
            pageCode.append("<li><a href='index?page="+(currentPage+1)+"'>下一页</a></li>");
            pageCode.append("<li><a href='index?page=" + totalPage + "'>尾页</a></li>");
        }
        return pageCode.toString();
    }
}
