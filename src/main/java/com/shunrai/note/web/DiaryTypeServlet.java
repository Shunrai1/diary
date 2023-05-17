package com.shunrai.note.web;

import com.shunrai.note.dao.DiaryDao;
import com.shunrai.note.dao.DiaryTypeDao;
import com.shunrai.note.po.DiaryType;
import com.shunrai.note.po.User;
import com.shunrai.note.util.DBUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "DiaryTypeServlet", value = "/DiaryType")
public class DiaryTypeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String actionName=request.getParameter("actionName");
        if("list".equals(actionName)){
            //设置日志管理导航高亮
            request.setAttribute("menu_page","type");
            diaryTypeList(request,response);
        }else if("preSave".equals(actionName)){
            diaryTypePreSave(request,response);
        }else if("save".equals(actionName)){
            diaryTypeSave(request,response);
        }else if("delete".equals(actionName)){
            diaryTypeDelete(request,response);
        }
    }

    //日志类别删除业务
    private void diaryTypeDelete(HttpServletRequest request, HttpServletResponse response) {
        String diaryTypeId = request.getParameter("diaryTypeId");
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            if(DiaryDao.existDiaryWithTypeId(conn, diaryTypeId)){
                request.setAttribute("error", "日志类别下有日志，不能删除该类别！");
            }else{
                DiaryTypeDao.diaryTypeDelete(conn, diaryTypeId);
            }
            request.getRequestDispatcher("DiaryType?actionName=list").forward(request, response);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                DBUtil.close(null,null,conn);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    //日志类别添加前业务
    private void diaryTypePreSave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String diaryTypeId = request.getParameter("diaryTypeId");
        if(diaryTypeId!=null&& !diaryTypeId.equals("")){
            Connection conn = null;
            try{
                conn = DBUtil.getConnection();
                DiaryType diaryType = DiaryTypeDao.diaryTypeShow(conn,diaryTypeId);
                request.setAttribute("diaryType", diaryType);
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
        request.setAttribute("changePage", "notetype/diaryTypeSave.jsp");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    //添加日志类别
    private void diaryTypeSave(HttpServletRequest request, HttpServletResponse response) {
        User user=(User) request.getSession().getAttribute("user");
        int user_id = user.getUser_id();
        String diaryTypeId = request.getParameter("diaryTypeId");
        String typeName = request.getParameter("typeName");
        DiaryType diaryType = new DiaryType(typeName);//这需要去创建对应的构造器

        if(diaryTypeId!=null&& !diaryTypeId.equals("")){
            diaryType.setType_id(Integer.parseInt(diaryTypeId));
        }
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            int saveNum = 0;
            //判断用户下类别是否唯一
            DiaryType diaryType1 = DiaryTypeDao.queryDiaryTypeByTypeNameAndUserId(typeName,user_id);
            //判断该用户类别是否存在
            if(diaryType1==null){ //该用户类别不存在,能添加该类别
                if(diaryTypeId!=null&& !diaryTypeId.equals("")){
                    //diaryTypeId不唯空就更新日志类别
                    saveNum = DiaryTypeDao.diaryTypeUpdate(conn, diaryType);
                }else{
                    //diaryTypeId唯空就是添加日志类别
                    saveNum = DiaryTypeDao.diaryTypeAdd(conn, diaryType,user_id);
                }
                if(saveNum>0){
                    //saveNum大于0说明执行成功了，我们进行跳转显示数据
                    request.getRequestDispatcher("DiaryType?actionName=list").forward(request, response);
                }else{
                    request.setAttribute("diaryType", diaryType);
                    request.setAttribute("error", "保存失败！");
                    request.setAttribute("changePage", "notetype/diaryTypeSave.jsp");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }
            }else {//该用户类别存在,不能添加该类别
                if(diaryTypeId!=null&& !diaryTypeId.equals("")){
                    request.setAttribute("diaryType", diaryType);//修改界面
                }
                request.setAttribute("error", "该类别已经存在，不能添加！");
                request.setAttribute("changePage", "notetype/diaryTypeSave.jsp");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }



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

    //显示日志类别的list业务
    private void diaryTypeList(HttpServletRequest request, HttpServletResponse response) {
        Connection conn = null;
        try{
            conn = DBUtil.getConnection();
            //只展示登陆用户的添加的类别
            User user=(User) request.getSession().getAttribute("user");
            int user_id = user.getUser_id();
            List<DiaryType> diaryTypeList = DiaryTypeDao.diaryTypeList(conn,user_id);
            request.setAttribute("diaryTypeList", diaryTypeList);
            request.setAttribute("changePage", "notetype/diaryTypeList.jsp");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                DBUtil.close(null,null,conn);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       this.doGet(request,response);
    }
}
