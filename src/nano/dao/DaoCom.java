package nano.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import nano.ListData;
import nano.Record;
import nano.util.DateUtil;
import nano.util.SqlMaker;

import org.apache.log4j.Logger;

/*****************************************************************
 * @description : Common Database Access Object class
 * $Id: DaoCom.java,v 1.17 2006/08/28 02:23:58 mijuri1004 Exp $
 * $Revision: 1.17 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 12. 20	정수현		최초작성
 * 
 *****************************************************************/

public class DaoCom {
	private static Logger logger = Logger.getLogger(DaoCom.class.getName());

	
	public static Connection getConnection(){
		Connection conn = null;
		try{
            //Class.forName("org.gjt.mm.mysql.Driver");
        	Class.forName("com.mysql.jdbc.Driver");
        }catch(ClassNotFoundException classnotfoundexception){
            System.out.println((new StringBuilder()).append("DriverClass Load error  : ").append(classnotfoundexception.toString()).toString());
        }
        try{
        	//conn = DriverManager.getConnection( "jdbc:mysql://113.130.69.106/biztkdb", "biztkuser", "mybiztk#1"); 
        	conn = DriverManager.getConnection( "jdbc:mysql://113.130.69.106/biztkdb2", "biztkdb2user", "mybiztkdb2#1");
        	//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kbgk_foreign", "kbgk", "DB_nanodms");
        	
        	
        	//stmt = conn.createStatement();
        }catch(SQLException sqlexception) {
        	System.out.println("sqlexception DB접속 오류: "+sqlexception);
        }
		if (conn == null){
			System.out.println("DB 접속오류입니다.");
			// TODO 오류처리
		}
		return conn;
	}

	
	
	public static void close(Connection conn) {
		try { if (conn != null) conn.close(); } catch (Exception e) {}
	}
	

	
	/**
	 * insert , update , delete 쿼리 실행할때 쓰인다.
	 * 
	 * @param conn
	 * @param sql
	 * @throws Exception
	 */
	public static void executeUpdate(Connection conn, String sql) throws Exception
	{
		ResultSet rs = null;
		PreparedStatement pstmt = null;
			
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("executeQueryAll select error :" + e.getMessage());
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}
			try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
		}
	}

	
	


	/**
	 * count 등 결과가 하나의 row 만 리턴되는 쿼리 실행할때 쓰인다.
	 * 
	 * @param conn
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static Record executeSingleQuery(Connection conn, String sql) throws Exception
	{
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Record rec = new Record();
		
		try{
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, 
					ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();

			ListData list = new ListData();
			list = getAllResultSet(rs, list);
			if (list.size() >0){
				rec = list.get(0);
			}
			return rec;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("executeQueryAll select error :" + e.getMessage());
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}
			try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
		}
	}

	
	/**
	 * query 결과를 모두 ListData 에 담는다.
	 * 
	 * @param conn
	 * @param list
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	
	public static ListData executeQueryAll(Connection conn, ListData list, String sql) throws Exception
	{
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, 
					ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();

			list = getAllResultSet(rs, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("executeQueryAll select error :" + e.getMessage());
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}
			try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
		}
	}

	
	public static ListData getAllResultSet(ResultSet resultSet, ListData list) throws Exception{
		
		ResultSetMetaData 	rsmd = resultSet.getMetaData();
		String[] 	columnName = new String[rsmd.getColumnCount()];
		String 		columnValue = "";

		
		for (int i = 1; i < rsmd.getColumnCount()+1; i++) {
			columnName[i-1] = rsmd.getColumnName(i).toLowerCase();   // toUpperCase
			//System.out.println("Column " + rsmd.getColumnName(j));
		}
		
		while(resultSet.next()){
			Record rec = new Record();
			rec.put("ROWNO", new Integer(list.getTotalCount() - resultSet.getRow() +1).toString());
			
			for (int j = 1; j < rsmd.getColumnCount() +1; j++) {
				int ftype = rsmd.getColumnType(j);


				if (ftype == Types.DATE){
					Date date = resultSet.getTimestamp(j);
					columnValue = DateUtil.getDateFormat( date, "yyyy-MM-dd HH:mm:ss");
					if ((columnValue.length() > 10) && (columnValue.substring(11).equals("00:00:00"))){
						columnValue = columnValue.substring(0,10);
					}
				} else {
					columnValue = resultSet.getString(j);
				}

				if (columnValue==null){
					columnValue = "";
				}
				rec.put(columnName[j-1], columnValue);
			}
			list.add(rec);
			
		}


		return list;
	}
	
	/**
	 * Oracle Table Clob 필드 update
	 * 
	 * @param conn
	 * @param sql
	 * @throws Exception
	 */
	/*
	public static void updateClob(Connection conn, String sql, String value) throws Exception{
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Writer writer = null;
		Reader src = null;
		
		try{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				oracle.sql.CLOB clob =  (CLOB)rs.getClob(1);
				writer = clob.getCharacterOutputStream();
				src = new CharArrayReader(value.toCharArray());
				char[] buffer = new char[1024];
				int read = 0;
				while ( (read = src.read(buffer,0,1024)) != -1) {
					writer.write(buffer, 0, read); // write clob.
				}
			}
		} finally {
			try { if (src != null) src.close(); } catch (Exception e) {}
			try { if (writer != null) writer.close(); } catch (Exception e) {}
			try { if (rs != null) rs.close(); } catch (Exception e) {}
			try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
		}
	}
	*/
	
	public static String getPageLink(int cpage, int totalPage) {
		int pageRange = 5; // 블럭당 페이지 Link 수
		return getPageLink(cpage,totalPage, pageRange);
	}
	/**
	 * 리스트 데이타 하단의 페이징 문자열을 만든다. 
	 * 
	 * @param cpage
	 * @param totalPage
	 * @return
	 */
	public static String getPageLink(int cpage, int totalPage, int pRange) {
		int pageRange = pRange; // 블럭당 페이지 Link 수
		
		String token = " ";
		//System.out.println("totalPage"+totalPage);
		
		StringBuffer buffer = new StringBuffer();
		int dest_page; 
		int total_block = (int)Math.ceil((double)totalPage / (double)pageRange);
		int block = (int)Math.ceil((double)(cpage) / (double)pageRange);
		int first_page = (block - 1) * pageRange;
		int last_page = block * pageRange;
		
		if(total_block <= block){
			last_page = totalPage;
		}

		dest_page = (block - 1) * pageRange - pageRange +1;
		buffer.append(" <li class=\"lasttxt\"> Page ");
		
		if(block > 1){
			dest_page = (block - 1) * pageRange - pageRange +1;
 			buffer.append(" <a href=\"javascript:pageLink('" + dest_page + "');\">&lt;</a>");
 			buffer.append(" <a href=\"javascript:pageLink('1');\">1</a> ... ");
		}
		
		if(totalPage<1){
			buffer.append("<strong>1</strong>");
		}
		
		
		for(int direct_page = first_page + 1; direct_page <= last_page; direct_page++) {
			 if(direct_page == first_page + 1)
				  buffer.append(" ");
			 else
				  buffer.append(token);
			 if(cpage == direct_page)
				  buffer.append("<strong>").append(cpage).append("</strong>");
			 else
				  buffer.append("<a href=\"javascript:pageLink('" + direct_page + "');\">" + direct_page + "</a>");
			 if(direct_page == last_page)
				  buffer.append(" ");
		}
		
		if(block < total_block)
		{
			buffer.append(" ... <a href=\"javascript:pageLink('" + totalPage + "');\">" + totalPage + "</a> ");
			dest_page = last_page + 1;
			buffer.append(" <a href=\"javascript:pageLink('" + dest_page + "');\">&gt;</a>  ");
		}
		
		buffer.append(" </li> ");
		
		return buffer.toString();
	}
	
	
	
	
	public static String getAdminPageLink(int cpage, int totalPage) {
		int pageRange = 5; // 블럭당 페이지 Link 수
		return getAdminPageLink(cpage,totalPage, pageRange);
	}
	/**
	 * 리스트 데이타 하단의 페이징 문자열을 만든다. 관리자용
	 * 
	 * @param cpage
	 * @param totalPage
	 * @return
	 */
	public static String getAdminPageLink(int cpage, int totalPage, int pRange) {
		int pageRange = pRange; // 블럭당 페이지 Link 수
		
		String token = " ";
		
		StringBuffer buffer = new StringBuffer();
		int dest_page; //링크되는 페이지
		int total_block = (int)Math.ceil((double)totalPage / (double)pageRange);
		int block = (int)Math.ceil((double)(cpage) / (double)pageRange);
		int first_page = (block - 1) * pageRange;
		int last_page = block * pageRange;
		
		if(total_block <= block){
			last_page = totalPage;
		}

		dest_page = (block - 1) * pageRange - pageRange +1;
		
		buffer.append(" <div class=\"manu\"> ");
		
		if(block > 1){
			dest_page = (block - 1) * pageRange - pageRange +1;
 			buffer.append(" <a href=\"javascript:pageLink('" + dest_page + "');\">&lt;&lt;</a> ");
		}
		
		if(totalPage<1){
			buffer.append("<span class=\"current\">1</span>");
		}
		
		
		for(int direct_page = first_page + 1; direct_page <= last_page; direct_page++) {
			 if(direct_page == first_page + 1)
				  buffer.append(" ");
			 else
				  buffer.append(token);
			 if(cpage == direct_page)
				  buffer.append("<span class=\"current\">").append(cpage).append("</span>");
			 else
				  buffer.append("<a href=\"javascript:pageLink('" + direct_page + "');\">" + direct_page + "</a>");
			 if(direct_page == last_page)
				  buffer.append(" ");
		}
		
		if(block < total_block)
		{
			dest_page = last_page + 1;
			buffer.append(" <a href=\"javascript:pageLink('" + dest_page + "');\">&gt;&gt;</a>  ");
		}
		
		buffer.append(" </div> ");

		return buffer.toString();
	}
	
	
	
	/**
	 * 방문자 Count
	 * 
	 * @param 
	 * @return
	 */
	 public static void writeTodayCount(){
		  PreparedStatement pstmt = null;
		  Connection conn = getConnection();

		  try {
		   conn.setAutoCommit(false);
		   SqlMaker sm = new SqlMaker();
		   String connectDate = DateUtil.getStrByPattern("yyyyMMdd");
		   sm.write("SELECT * FROM edm_count");
		   sm.where("cnt_date",connectDate);
		   Record rec = executeSingleQuery(conn, sm.getSql());
		   if (rec.size() == 0){
		    //insert();
		    sm.reset();
		    sm.write("INSERT INTO edm_count ");
		    sm.write("(cnt_date, cnt_hit) ");
		    sm.write(" VALUES (");
		    
		    sm.write("?, 1 "); 
		    sm.write(" )");
		    
		    //System.out.println("방문자수 INSERT>>" + sm.toString());
		    pstmt = conn.prepareStatement(sm.getSql());
		          pstmt.setString(1,connectDate);
		    pstmt.executeUpdate();
		   } else {
		    // update();
		    sm.reset();
		    sm.write("UPDATE edm_count ");
		    sm.setNumber("cnt_hit","cnt_hit+1");
		    sm.where("cnt_date",connectDate);
		    //System.out.println("방문자수 UPDATE>>" + sm.toString());

		    pstmt = conn.prepareStatement(sm.getSql());
		    pstmt.executeUpdate();
		   }
		   
		   //System.out.println("방문자 sm.getSQL=" + sm.getSql()); 
		   conn.commit();
		  } catch (Exception e) {
		   try {conn.rollback();} catch(Exception se){}
		   e.printStackTrace();
		   // TODO: handle exception
		  } finally {
		   try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
		   close(conn);
		  }
		 }
	 
	 	 /**
		 * 방문자 Total Count
		 * 
		 * @param 
		 * @return
		 */
		 public static void writeTotalCount(){
			  PreparedStatement pstmt = null;
			  Connection conn = getConnection();

			  try {
			   conn.setAutoCommit(false);
			   SqlMaker sm = new SqlMaker();
			   sm.write("SELECT * FROM edm_totalcount");
			    // update();
			    sm.reset();
			    sm.write("UPDATE edm_totalcount ");
			    sm.setNumber("ttcnt_hit","ttcnt_hit+1");
			    sm.where("ttcnt_date","1");
			    //System.out.println(">>" + sm.toString());

			    pstmt = conn.prepareStatement(sm.getSql());
			    pstmt.executeUpdate();
			   
			   //System.out.println("SQL=" + sm.getSql()); 
			   conn.commit();
			  } catch (Exception e) {
			   try {conn.rollback();} catch(Exception se){}
			   e.printStackTrace();
			   // TODO: handle exception
			  } finally {
			   try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
			   close(conn);
			  }
			 }
		
		 /**
		  * 일일 방문자 수 가져오기
		  * 
		  * @return
		  */
		 
		 public String getCount(){
			 String count = "";
			 
				SqlMaker sm = new SqlMaker();
				Connection conn = getConnection();
				String connectDate = DateUtil.getStrByPattern("yyyyMMdd");
				
				sm.write(" SELECT cnt_hit FROM edm_count");
				sm.write(" where cnt_date = '"+connectDate+"' ");
				//System.out.println("SQL=" + sm.getSql());
				try {
					Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
					count = rec.get("cnt_hit", "0");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
				   close(conn);
			  }
			 return count;
			 
		 	}
		 
		 /**
		  * 월간 방문자 수 가져오기
		  * 
		  * @return
		  */
		 
		 public String getmonthCount(){
			 String monthcount = "";
			 
				SqlMaker sm = new SqlMaker();
				Connection conn = getConnection();
				String connectDate = DateUtil.getStrByPattern("yyyyMMdd");
				String monthDate = connectDate.substring(0,6);
				
				
				sm.write(" SELECT sum(cnt_hit) FROM edm_count");
				sm.write(" where cnt_date like '%"+monthDate+"%' ");
				
				//System.out.println("SQL=" + sm.getSql());
				try {
					Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
					monthcount = rec.get("sum(cnt_hit)", "0");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
				   close(conn);
			  }
			 return monthcount;
			 
		 	}
		 
		 
		 
		 
		 
		 /**
		  * 총 방문자 수 가져오기
		  * 
		  * @return
		  */
		 
		 public String getTotalCount(){
			 String totalcount = "";
			 
				SqlMaker sm = new SqlMaker();
				Connection conn = getConnection();
				
				sm.write(" SELECT ttcnt_hit FROM edm_totalcount");
				sm.write(" where ttcnt_date = 1");
				//System.out.println("SQL=" + sm.getSql());
				try {
					Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
					totalcount = rec.get("ttcnt_hit", "0");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
				   close(conn);
			  }
			 return totalcount;
			 
		 	}
		 
		 
		 
		 
		 
	
	/**
	 * @deprecated 데이타가 많을경우 성능에 문제가 있다.
	 * 
	 * 전체 데이타를 select 하여 보여줄 데이타만 ListData 에 담는다
	 * 
	 * @param conn
	 * @param list
	 * @param sql
	 * @return
	 * @throws Exception
	 */
/*
	public static ListData executeQuery(Connection conn, ListData list, String sql) throws Exception
	{
		ResultSet rs = null;
		PreparedStatement pstmt = null;


		try{
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();

			list = getResultSet(rs, list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("select error :" + e.getMessage());
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}
			try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
		}
	}
*/
	
	/**
	 * @deprecated  데이타가 많을경우 성능에 문제가 있다.
	 * 
	 * 조회된 데이타중(ResultSet) 보여줄 데이타만 ListData 에 담는다.
	 * 
	 * @param resultSet
	 * @param list
	 * @return
	 * @throws Exception
	 */
/*	
	public static ListData getResultSet(ResultSet resultSet, ListData list) throws Exception{
		
		ResultSetMetaData 	rsmd = resultSet.getMetaData();
		String[] 	columnName = new String[rsmd.getColumnCount()];
		String 		columnValue = "";

		for (int i = 1; i < rsmd.getColumnCount()+1; i++) {
			columnName[i-1] = rsmd.getColumnName(i).toLowerCase();   // toUpperCase
			//System.out.println("Column " + rsmd.getColumnName(j));
		}
		
		int start_page = list.getCurrentPage();
		int count = list.pageRange;
		
		int start = (start_page-1) * count;
		int r_count = count;

		resultSet.last();
		int total_size = resultSet.getRow();
		list.setTotalPage(total_size);
		
//		resultSet.first();	
		if (start >= 0 && resultSet.absolute(start + 1)) {

			do {
				Hashtable ht = new Hashtable();
				for (int j = 1; j < rsmd.getColumnCount() +1; j++) {
					int ftype = rsmd.getColumnType(j);
					//System.out.println("COLUMN=" +rsmd.getColumnName(j) + " type=" + ftype);
	
					if (ftype == Types.CLOB){
						
						
						oracle.sql.CLOB clob = (CLOB)resultSet.getClob(j); //1818
						if (clob == null){
							continue;
						}
						Reader instream = clob.getCharacterStream();
						char[] buffer = new char[1024];
						int length=-1;
						StringBuffer sb = new StringBuffer();
						while ((length = instream.read(buffer))!= -1) {
							sb.append(new String(buffer, 0, length));
						}
						instream.close();
						columnValue = sb.toString();
					}else if (ftype == Types.DATE){
						Date date = resultSet.getTimestamp(j);
						columnValue = DateUtil.getDateFormat( date, "yyyy-MM-dd HH:mm:ss");
						if ((columnValue.length() > 10) && (columnValue.substring(11).equals("00:00:00"))){
							columnValue = columnValue.substring(0,10);
						}
											
	//				} else if ((ftype==Types.INTEGER) || (ftype==Types.NUMERIC)) {
	//					///rd.setValue(columnName[j-1], new Integer(resultSet.getInt(j)));
	//					ht.put(columnName[j-1], new Integer(resultSet.getInt(j)));
	//					continue;
	//				} else	if (ftype< 0) {
	//					continue;
					} else {
						columnValue = resultSet.getString(j);
					}
	
					if (columnValue==null){
						columnValue = "";
					}
					///rd.setValue(cName[j-1], columnValue);
					ht.put(columnName[j-1], columnValue);
				}
				list.add(ht);
			} while(resultSet.next() && (--r_count > 0));
		}		
		return list;
	}
*/	
	
	
	
}
