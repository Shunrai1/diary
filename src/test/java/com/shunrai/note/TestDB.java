package com.shunrai.note;

import cn.hutool.core.util.StrUtil;
import com.shunrai.note.dao.BaseDao;
import com.shunrai.note.dao.UserDao;
import com.shunrai.note.po.User;
import com.shunrai.note.util.DBUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class TestDB {
    //使用日志工厂类，记录日志
    private Logger logger =  LoggerFactory.getLogger(TestDB.class);
    /**
     * 单元测试方法
     * 1. 方法的返回值，建议使用void,一般没有返回值
     * 2. 参数列表，建议空参，一般是没有参数
     * 3， 方法上需要设置@Test注解
     * 4. 每个方法都能独立远行
     *
     *判定结果：
     *     绿色：成功
     *     红色：失败
     */
    @Test
    public void testDB(){
        System.out.println(DBUtil.getConnection());
        //使用日志
        logger.info("获取数据库连接："+DBUtil.getConnection());
        logger.info("获取数据库连接；{}",DBUtil.getConnection());
    }
    @Test
    public void testQueryUserName(){
        UserDao userDao = new UserDao();
        User user = userDao.queryUserByName("admin");
        System.out.println(user.getUpwd());
    }
    @Test
    public void testQueryUserByName(){
        UserDao userDao=new UserDao();
        User user = userDao.queryUserByName("admin");
        System.out.println(user.getUpwd());
    }
    @Test
    public void testAdd(){
        String sql = "insert into tb_user (user_id,uname,upwd,nick,head,mood) values (?,?,?,?,?,?)";
        List<Object> params = new ArrayList<>();
        params.add(3);
        params.add("lisi");
        params.add("2323");
        params.add("lisi");
        params.add("404.jpg");
        params.add("Hello");
        int row = BaseDao.executeUpdate(sql,params);
        System.out.println(row);
    }
    @Test
    public void up(){
        String sql = "select * from tb_user where nick = ? and user_id != ?";
        List<Object> params = new ArrayList<>();
        params.add("lisi1e");
        params.add(1);
        int row=  BaseDao.executeUpdate(sql,params /**User.class**/);
        if(row == 0){
            System.out.println("nick可用jjj");
        }else {
            System.out.println("nick不可用！！！");
        }

    }

}
