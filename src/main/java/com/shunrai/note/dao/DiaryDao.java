package com.shunrai.note.dao;
import com.shunrai.note.po.Diary;
import com.shunrai.note.po.DiaryType;
import com.shunrai.note.po.PageBean;

import com.shunrai.note.util.DateUtil;
import com.shunrai.note.util.StringUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DiaryDao {
//	public static boolean existDiaryWithTypeId(Connection conn, String diaryTypeId) {
//		return false;
//	}

	//删除前判断日志类别下是否有日志
	public static boolean existDiaryWithTypeId(Connection conn, String typeId)throws Exception{
		String sql = "select * from tb_note where type_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		int typeId1 = Integer.parseInt(typeId);
		pstmt.setInt(1, typeId1);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
			return true;
		}else{
			return false;
		}
	}

	//根据ID显示日志详细信息
    public static Diary diaryShow(Connection conn, String diaryId) throws Exception {
		String sql = "select t1.*, t2.type_name from tb_note t1,tb_note_type t2 where note_id = ? and t1.type_id = t2.type_id";
		int diaryId1 = Integer.parseInt(diaryId);
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, diaryId1);
		ResultSet rs = pstmt.executeQuery();
		Diary diary = new Diary();
		if(rs.next()){
			diary.setNote_id(rs.getInt("note_id"));
			diary.setTitle(rs.getString("title"));
			diary.setContent(rs.getString("content"));
			diary.setType_id(rs.getInt("type_id"));
			diary.setTypeName(rs.getString("type_name"));
			//TypeName第二张表里的 ，一会儿需要显示的，所以要添加这个属性
			//diary.setPub_time(DateUtil.formatString(rs.getString("pub_time"),"yyyy-MM-dd HH:mm:ss"));
			diary.setPub_time(rs.getDate("pub_time"));
			diary.setLon(rs.getFloat("lon"));
			diary.setLat(rs.getFloat("lat"));
		}
		return diary;
    }
	//修改日志
	public static int diaryUpdate(Connection conn, Diary diary) throws SQLException {
		String sql = "update tb_note set title = ?, content = ?, type_id = ?, pub_time = now(), lon = ?, lat = ?, address = ? where note_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, diary.getTitle());
		pstmt.setString(2, diary.getContent());
		pstmt.setInt(3, diary.getType_id());
		pstmt.setFloat(4, diary.getLon());
		pstmt.setFloat(5,diary.getLat());
		pstmt.setString(6,diary.getAddress());
		pstmt.setInt(7,diary.getNote_id());
		return pstmt.executeUpdate();
	}

	//写日志
	public static int diaryAdd(Connection con, Diary diary)throws Exception{
		String sql="insert into tb_note(title,content,type_id,pub_time,lon,lat,address) values (?,?,?,now(),?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, diary.getTitle());
		pstmt.setString(2, diary.getContent());
		pstmt.setInt(3, diary.getType_id());
		pstmt.setFloat(4,diary.getLon());
		pstmt.setFloat(5,diary.getLat());
		pstmt.setString(6,diary.getAddress());
		return pstmt.executeUpdate();
	}

		//删除日志
	public static int diaryDelete(Connection con, String diaryId)throws Exception{
		String sql="delete from tb_note where note_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		int diaryId1 = Integer.parseInt(diaryId);
		pstmt.setInt(1, diaryId1);
		return pstmt.executeUpdate();
	}
//
	public static List<Diary> diaryList(Connection con, PageBean pageBean, Diary s_diary, int user_id)throws Exception{
		List<Diary> diaryList = new ArrayList<Diary>();
		StringBuffer sb = new StringBuffer("select * from tb_note t1,tb_note_type t2 where t1.type_id = t2.type_id and user_id = "+user_id+"");//查询类型type相同的，日志表和类型表的联合
		genSql(sb, s_diary);
		//根据日志的发布日期进行降序,这就会根据最新发布的来显示内容
		sb.append(" order by t1.pub_time desc");
		if(pageBean!=null){
			//添加分页查询
			sb.append(" limit " +pageBean.getPageSize() + " OFFSET " +pageBean.getStart());//前端每页展示的数据个数
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			Diary diary = new Diary();
			diary.setNote_id(rs.getInt("note_id"));
			diary.setTitle(rs.getString("title"));
			diary.setContent(rs.getString("content"));
			//日期是需要转换的，所以先创建DateUtil工具类
//			diary.setPub_time(DateUtil.formatString(rs.getString("pub_time"), "yyyy-MM-dd"));旧的要类型转换
			diary.setPub_time(rs.getDate("pub_time"));
			diaryList.add(diary);
		}
		return diaryList;
	}

//
	//查询日志总记录数
	public static int diaryCount(Connection conn, Diary s_diary, int user_id ) throws Exception{
		StringBuffer sb = new StringBuffer("select count(*) as total from tb_note t1,tb_note_type t2 where t1.type_id = t2.type_id and user_id = "+user_id+"");
		genSql(sb, s_diary);
		PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}
		return 0;
	}

	//按照年月日查询日志数
	public static List<Diary> diaryCountList(Connection con, int user_id)throws Exception{
		List<Diary> diaryCountList=new ArrayList<Diary>();
		String sql="SELECT to_date(cast(pub_time as TEXT),'YYYY-MM-DD') as releaseDateStr ,"
				+ "COUNT(*) as diaryCount "
				+ "FROM tb_note t1, tb_note_type t2 WHERE t2.user_id = "+user_id+" and t1.type_id = t2.type_id GROUP BY releaseDateStr ORDER BY releaseDateStr DESC ";
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			Diary diary = new Diary();
//			diary.setReleaseDateStr(rs.getString("releaseDateStr")); 旧的返回的为字符串
			diary.setPub_time(rs.getDate("releasedatestr")); //新的返回的为Date类型
			diary.setDiaryCount(rs.getInt("diaryCount"));
			diaryCountList.add(diary);
		}
		return diaryCountList;
	}

	//封装查询条件
	private static StringBuffer genSql(StringBuffer sb, Diary s_diary){
		if(StringUtil.isNotEmpty(s_diary.getTitle())){
			//标题模糊查询
			sb.append(" and t1.title like '%"+s_diary.getTitle()+"%'");
		}
		if(s_diary.getType_id()!=-1){
			//按照类型的id去查询
			sb.append(" and t1.type_id = " +s_diary.getType_id());
		}
		if(StringUtil.isNotEmpty(s_diary.getReleaseDateStr())){
			//按照日期去查询
			sb.append(" and to_date(cast(pub_time as TEXT),'YYYY-MM-DD') = '"+s_diary.getReleaseDateStr()+"'");
		}
		return sb;
	}
}
