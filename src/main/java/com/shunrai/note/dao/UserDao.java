package com.shunrai.note.dao;

import com.shunrai.note.po.User;
import com.shunrai.note.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    /**
     * 1.定义sql语句
     * 2.设置参数集合
     * 3.调用BaseDao的查询语句
     * @param userName
     * @return
     */
    public User queryUserByName(String userName){

        String sql= "select * from tb_user where uname = ?";
        List<Object> params = new ArrayList<>();
        params.add(userName);
        User user =(User) BaseDao.queryRow(sql,params,User.class);

        return user;
    }

    public User queryUserByName02(String userName){
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //获取数据库连接
        try{
            connection = DBUtil.getConnection();
            String sql = "select * from tb_user where uname = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,userName);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                user=new User();
                user.setUser_id(resultSet.getInt("user_id"));
                user.setUname(userName);
                user.setHead(resultSet.getString("head"));
                user.setMood(resultSet.getString("mood"));
                user.setNick(resultSet.getString("nick"));
                user.setUpwd(resultSet.getString("upwd"));

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtil.close(resultSet,preparedStatement,connection);
        }

        return user;
    }

    /**
     * 通过昵称和用户id查询用户对象
     * @param nick
     * @param user_id
     * @return
     */
    public User queryUserByNickAndUserId(String nick, int user_id) {
        //定义sql语句
        String sql = "select * from tb_user where nick = ? and user_id != ?";
        //设置参数集合
        List<Object> params = new ArrayList<>();
        params.add(nick);
        params.add(user_id);
        //调用BaseDao的查询方法
        User user=  (User) BaseDao.queryRow(sql,params ,User.class);
        return user;
    }

    /**
     * 通过用户id修改用户信息
     * @param user
     * @return
     */
    public int updateUser(User user) {
        String sql = "update tb_user set nick = ?, mood = ?, head = ? where user_id = ?";
        List<Object> params = new ArrayList<>();
        params.add(user.getNick());
        params.add(user.getMood());
        String mood = user.getMood();
        params.add(user.getHead());
        params.add(user.getUser_id());
        int row = BaseDao.executeUpdate(sql, params);
        return row;
    }
}
