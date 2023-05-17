package com.shunrai.note.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.db.Session;
import com.shunrai.note.dao.UserDao;
import com.shunrai.note.po.User;
import com.shunrai.note.vo.ResultInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class UserService {
    private UserDao userDao=new UserDao();

    public ResultInfo<User>userLogin(String userName, String userPwd){
        ResultInfo<User> resultInfo=new ResultInfo<>();
        //数据回显：当登录实现时，将登录信息返回给页面显示
        User u = new User();
        u.setUname(userName);
        u.setUpwd(userPwd);
        //设置到resultInfo对象中
        resultInfo.setResult(u);
        //判断参数是否为空
        if(StrUtil.isBlank(userName)||StrUtil.isBlank(userPwd)){
            resultInfo.setCode(0);
            resultInfo.setMsg("用户姓名或密码不能为空！");
            return resultInfo;
        }
        //如果不为空，通过用户名查询用户对象
        User user = userDao.queryUserByName(userName);
        //3，判断用户对象是否为空
        if(user==null){
            //
            resultInfo.setCode(0);
            resultInfo.setMsg("该用户不存在！");
            return resultInfo;
        }
        //4.如果用户对象不为空，将数据库中查询到的用户对象的密码与前端的作比较（将密码加密）
        //将前台的密码按照md5Hes加密
        //userPwd = DigestUtil.md5Hex(userPwd);
        if(!userPwd.equals(user.getUpwd())){
            resultInfo.setCode(0);
            resultInfo.setMsg("用户密码不正确！");
            return resultInfo;
        }
        resultInfo.setCode(1);
        resultInfo.setResult(user);
        return resultInfo;
    }

    /**
     * 验证昵称的唯一性
     * @param nick
     * @param user_id
     * @return
     */
    public Integer checkNick(String nick, int user_id) {
        //判断的昵称是否为空。返回1，表示不可用；返回0表示可用。
        if(StrUtil.isBlank(nick)){
            return 1;
        }
        //调用DAO层，通过用户id和昵称查询用户对象，受影响行数，如为0则不存在昵称相同的用户,输入昵称可用。
        User user = userDao.queryUserByNickAndUserId(nick,user_id);
        //判断用户是否存在
        if(user==null){ //用户不存在
            return 0;
        }
        return 1;
    }

    /**
     * 修改用户信息
     * @param request
     * @return
     */
    public ResultInfo<User> updateUser(HttpServletRequest request) {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        String nick = request.getParameter("nick");
        String mood = request.getParameter("mood");
        //必填参数非空校验
        if(StrUtil.isBlank(nick)){
            resultInfo.setCode(0);
            resultInfo.setMsg("用户昵称不能为空！");
            return resultInfo;
        }
        HttpSession session=request.getSession();
        User user =(User) request.getSession().getAttribute("user");
        //设置修改的昵称和心情
        user.setNick(nick);
        user.setMood(mood);
        try {
            //获取Part对象
            Part part=request.getPart("img");
            //通过Part对象获取上传文件的文件名（从头部信息中获取上传的文件名）
            String header = part.getHeader("Content-Disposition");
            //获取具体的请求头对应的值
            String str = header.substring(header.lastIndexOf("=")+2);
            //获取上传的文件名
            String fileName = str.substring(0,str.length()-1);
            //判断文件名是否为空
            if(!StrUtil.isBlank(fileName)){
                user.setHead(fileName);
                String filePath = request.getServletContext().getRealPath("/WEB-INF/upload/");
                part.write(filePath+"/"+fileName);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //调用Dao层的更新方法，返回受影响的行数
        int row = userDao.updateUser(user);
        //判断受影响行数
        if(row>0){
            resultInfo.setCode(1);
            //更新session中用户对象
            request.getSession().setAttribute("user",user);
        }else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }
        return resultInfo;
    }
}
