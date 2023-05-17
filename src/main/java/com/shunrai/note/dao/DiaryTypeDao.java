package com.shunrai.note.dao;
import com.shunrai.note.po.DiaryType;
import com.shunrai.note.po.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DiaryTypeDao {
	public static List<DiaryType> diaryTypeCountList(Connection conn,int user_id)throws Exception{
		List<DiaryType> diaryTypeCountList = new ArrayList<DiaryType>();
		String sql = "select t2.type_id,t2.type_name,count(note_id) as diarycount "
				+ "from tb_note as t1 "
				+ "right join tb_note_type as t2 "
				+ "on t1.type_id = t2.type_id "
				+"where user_id = "+user_id+""
				+ " group by t2.type_id";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			DiaryType diaryType = new DiaryType();
			diaryType.setType_id(rs.getInt("type_id"));
			diaryType.setType_name(rs.getString("type_name"));
			diaryType.setDiaryCount(rs.getInt("diarycount"));
			diaryTypeCountList.add(diaryType);
		}
		return diaryTypeCountList;
	}

	//日志类别查询
	public static List<DiaryType> diaryTypeList(Connection conn,int user_id)throws Exception{
		List<DiaryType> diaryTypeList = new ArrayList<DiaryType>();
		String sql="select * from tb_note_type where user_id = ? order by type_id";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, user_id );
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			DiaryType diaryType = new DiaryType();
			diaryType.setType_id(rs.getInt("type_id"));
			diaryType.setType_name(rs.getString("type_name"));
			diaryType.setUser_id(rs.getInt("user_id"));
			diaryTypeList.add(diaryType);
		}
		return diaryTypeList;
	}
	
	//日志类别的添加
	public static int diaryTypeAdd(Connection conn, DiaryType diaryType,int user_id)throws Exception{
		String sql = "insert into tb_note_type(type_name,user_id) values (?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		//自增约束性，来实现
//		String sql1 = "select * from tb_note_type";
//		List<Object> params = new ArrayList<>();
//		List<DiaryType> list = BaseDao.queryRows(sql1,params,DiaryType.class);
//		int row = list.size();
//		pstmt.setInt(1, (row+1) );
		pstmt.setString(1, diaryType.getType_name());
		pstmt.setInt(2, user_id );
		return pstmt.executeUpdate();
	}
	
	//日志类别的修改
	public static int diaryTypeUpdate(Connection conn, DiaryType diaryType)throws Exception{
		String sql = "update tb_note_type set type_name = ? where type_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, diaryType.getType_name());
		pstmt.setInt(2, diaryType.getType_id());
		return pstmt.executeUpdate();
	}
	//日志类别修改前，显示旧数据
	public static DiaryType diaryTypeShow(Connection conn,String diaryTypeId)throws Exception{
		String sql="SELECT * from tb_note_type where type_id = ?";
		List<Object> params = new ArrayList<>();
		int diaryTypeId1 = Integer.parseInt(diaryTypeId);
		params.add(diaryTypeId1);
		DiaryType diaryType = (DiaryType) BaseDao.queryRow(sql,params,DiaryType.class);
		return diaryType;
	}

	//删除日志类别 当日志类别下有日志是不能删除的
	public static int diaryTypeDelete(Connection conn, String diaryTypeId)throws Exception{
		String sql = "delete from tb_note_type where type_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		int diaryTypeId1 = Integer.parseInt(diaryTypeId);
		pstmt.setInt(1, diaryTypeId1);
		return pstmt.executeUpdate();
	}

	public static DiaryType queryDiaryTypeByTypeNameAndUserId(String typeName, int user_id) {
		//定义sql语句
		String sql = "select * from tb_note_type where type_name = ? and user_id = ?";
		//设置参数集合
		List<Object> params = new ArrayList<>();
		params.add(typeName);
		params.add(user_id);
		//调用BaseDao的查询方法
		DiaryType diaryType=  (DiaryType) BaseDao.queryRow(sql,params ,DiaryType.class);
		return diaryType;
	}
}
