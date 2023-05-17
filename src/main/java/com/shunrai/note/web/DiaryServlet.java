package com.shunrai.note.web;

import com.shunrai.note.dao.DiaryDao;
import com.shunrai.note.dao.DiaryTypeDao;
import com.shunrai.note.po.Diary;
import com.shunrai.note.po.User;
import com.shunrai.note.util.DBUtil;
import com.shunrai.note.util.StringUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "DiaryServlet", value = "/Diary")
public class DiaryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //action用来判断操作
        String actionName = request.getParameter("actionName");
        if("show".equals(actionName)){
            diaryShow(request, response);
        }else if("preSave".equals(actionName)){
            //设置日志管理导航高亮
            request.setAttribute("menu_page","note");
            diaryPreSave(request,response);
        }else if("save".equals(actionName)){
            diarySave(request,response);
        }else if("delete".equals(actionName)){
            diaryDelete(request,response);
        }
    }
    //删除日志
    private void diaryDelete(HttpServletRequest request, HttpServletResponse response) {
        String diaryId = request.getParameter("diaryId");
        Connection con = null;
        try{
            con = DBUtil.getConnection();
            DiaryDao.diaryDelete(con, diaryId);
            request.getRequestDispatcher("index?all=true").forward(request, response);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                DBUtil.close(null,null,con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //保存日志
    private void diarySave(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String typeId = request.getParameter("typeId");
        String diaryId = request.getParameter("diaryId");
        float lon = Float.parseFloat(request.getParameter("lng"));
        float lat = Float.parseFloat(request.getParameter("lat"));
        String address = request.getParameter("address");
        Diary diary=new Diary(title,content,Integer.parseInt(typeId),lon,lat,address);//去model加个构造器
        if(StringUtil.isNotEmpty(diaryId)){
            diary.setNote_id(Integer.parseInt(diaryId));
        }
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            int saveNums;
            if(StringUtil.isNotEmpty(diaryId)){
                saveNums=DiaryDao.diaryUpdate(conn, diary);
            }else{
                saveNums=DiaryDao.diaryAdd(conn, diary);
            }
            if(saveNums > 0){
                request.getRequestDispatcher("index?all=true").forward(request, response);
            }else{
                request.setAttribute("diary", diary);
                request.setAttribute("error", "保存失败");
                request.setAttribute("changePage", "note/diarySave.jsp");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                DBUtil.close(null,null,conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //写日志
    private void diaryPreSave(HttpServletRequest request, HttpServletResponse response) {
        String diaryId = request.getParameter("diaryId");
        Connection conn = null;
        try{
            if(StringUtil.isNotEmpty(diaryId)){
                conn = DBUtil.getConnection();
                Diary diary = DiaryDao.diaryShow(conn, diaryId);
                request.setAttribute("diary", diary);
            }
            //获取当前登陆用户的id
            User user=(User) request.getSession().getAttribute("user");
            int user_id = user.getUser_id();
            //实现自动更新写日志的类型列表
            conn = DBUtil.getConnection();
            request.getSession().setAttribute("diaryTypeCountList", DiaryTypeDao.diaryTypeCountList(conn,user_id));//每个类型下的日志数
            request.setAttribute("changePage", "note/diarySave.jsp");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                DBUtil.close(null,null,conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //显示日志
    private void diaryShow(HttpServletRequest request, HttpServletResponse response) {
        String diaryId = request.getParameter("diaryId");
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            Diary diary = DiaryDao.diaryShow(conn, diaryId);
            request.setAttribute("diary", diary);
            request.setAttribute("changePage", "note/diaryShow.jsp");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DBUtil.close(null,null,conn);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       this.doGet(request,response);
    }
}
