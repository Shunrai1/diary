package com.shunrai.note.dao;

import com.shunrai.note.util.DBUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 基础的JDBC类
 *     更新操作（添加、修改、删除）
 *     查询操作
 *        1.查询一个字段（只会返回一条记录且只有一个字段；常用场景：查询总数量）
 *        2，查询集合
 *        3，查询某个对象
 */
public class BaseDao {
    /**
     * 更像操作
     *
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, List<Object>params){
        int row = 0;//受影响的行数
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = DBUtil.getConnection();
            preparedStatement= connection.prepareStatement(sql);
            //如果有参数,下标从1开始
            if(params!=null&&params.size()>0){
                for(int i=0;i<params.size();i++) {
                    preparedStatement.setObject(i+1,params.get(i));
                }
            }
            row = preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtil.close(null,preparedStatement,connection);
        }
        return row;
    }

    /**
     * 查询一个字段（只会返回一条记录且只有一个字段；常用场景：查询总数量）
     * @param sql
     * @param params
     * @return
     */
    public static Object findSingleValue(String sql,List<Object>params){
        Object object = null;
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try{
            connection = DBUtil.getConnection();
            preparedStatement= connection.prepareStatement(sql);
            //如果有参数,下标从1开始
            if(params!=null&&params.size()>0){
                for(int i=0;i<params.size();i++) {
                    preparedStatement.setObject(i+1,params.get(i));
                }
            }
            //执行查询，返回结果集
            resultSet = preparedStatement.executeQuery();
            //判断并分析结果集
            if(resultSet.next()){
                object=resultSet.getObject(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtil.close(resultSet,preparedStatement,connection);
        }
        return object;
    }

    /**
     * 查询集合
     * 要求JavaBean中的字段与数据库中表的字段对应
     */
    public static List queryRows(String sql,List<Object> params,Class cls){
        List list = new ArrayList();
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;

        try{
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            //如果有参数,下标从1开始
            if(params!=null && params.size()>0){
                for(int i=0;i<params.size();i++) {
                    preparedStatement.setObject(i+1,params.get(i));
                }
            }
            //执行查询，返回结果集
            resultSet = preparedStatement.executeQuery();
            //得到结果集的元数据对象
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            //得到查询的字段数量
            int fieldNum = resultSetMetaData.getColumnCount();
            //判断并分析结果集
            while (resultSet.next()){
                Object object = cls.newInstance();
                for(int i=1;i<= fieldNum; i++){
                    //getColumnName()：获取列名或别名
                    //getColumnName(): 获取列名
                    String columnName = resultSetMetaData.getColumnName(i);
                    //通过反射，使用列名得到对应的field对象
                    Field field = cls.getDeclaredField(columnName);
                    //拼接set方法，得到字符串
                    String setMethod = "set" + columnName.substring(0,1).toUpperCase()+columnName.substring(1);
                    //通过反射，将set方法字符串反射成类中对应的set方法
                    Method method = cls.getDeclaredMethod(setMethod,field.getType());
                    //得到查询的每一个字段对应的值
                    Object value =resultSet.getObject(columnName);
                    //通过invoke方法调用set方法
                    method.invoke(object,value);
                }
                //将Javabean设置到集合中
                list.add(object);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtil.close(resultSet,preparedStatement,connection);
        }
        return list;
    }

    /**
     * 查询某个对象
     * @param sql
     * @param params
     * @param cls
     * @return
     */
    public static Object queryRow(String sql,List<Object>params,Class cls){
        List list = queryRows(sql,params,cls);
        Object object = null;
        //如果集合不为空，则获取查询的第一条数据
        if(list !=null && list.size()>0){
            object = list.get(0);
        }
        return object;
    }
}
