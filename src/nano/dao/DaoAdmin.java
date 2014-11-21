package nano.dao;


import nano.*;
import nano.util.*;

import java.sql.*;
import java.util.*;

import javax.servlet.http.HttpSession;

import org.apache.log4j.*;

/*****************************************************************
 * @description : 관리자 관련 로직 처리하기위한 class
 * $Id: DaoUser.java,v 1.13 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 12. 20	정수현		최초작성
 * 
 *****************************************************************/


public class DaoAdmin {
	private static Logger logger = Logger.getLogger(DaoAdmin.class.getName());
	private Connection conn = null;
	SqlMaker sql = null;
	
	public DaoAdmin(Connection conn){
		this.conn = conn;
		sql = new SqlMaker();
	}
	
	/**
	 * 관리자 정보 조회
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record login(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		sm.write("SELECT * FROM tbl_admin ");
		sm.where("admin_id",props.getProperty("admin_id"));
		sm.where("admin_password",props.getProperty("admin_pw"));
		
		//System.out.println("회원정보SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());

			//logger.debug("로그인 사용자 정보:" + rec.toString());

			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000", "로그인 사용자 조회 오류");
		}
	}
	
	
	/**
	 * 조회할 데이터를 Select 한다.
	 * 조회할때 반드시 count() method 를 호출 하여 데이터의 숫자를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getReportList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(10);
		int totalCnt = reportCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		String menu_type1 = props.getProperty("menu_type1", "A");
		String menu_type2 = props.getProperty("menu_type2", "A");
		String category_code = props.getProperty("category_code", "");
		String subdivision_code = props.getProperty("subdivision_code", "");
		
		if(menu_type1.equals("F")){
			sm.write(" SELECT * FROM tbl_product");
			sm.write(" WHERE category_code = '"+category_code+"'");
			sm.write(" AND subdivision_code = '"+subdivision_code+"'");
			sm.write(" ORDER BY product_title asc");
		}else{
			sm.write(" SELECT * FROM tbl_product");
			sm.write(" WHERE product_type = '"+menu_type2+"'");
			sm.write(" ORDER BY product_num desc");
			sm.write(" limit "+first+", "+list.itemRange);
		}
		//System.out.println("게시판 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	/**
	 * 조회할 데이터의 row count 를 계산한다.
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int reportCount(Properties props) throws BizException {
		String menu_type2 = props.getProperty("menu_type2", "A");
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM tbl_product");
		sql.write(" WHERE product_type = '"+menu_type2+"'");
		
		//System.out.println("count SQL=" + sql.getSql());
		
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sql.getSql());
			Object obj = rec.get("row_count");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * 
	 * 분류 목록
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getCategoryList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		sm.write(" SELECT * FROM tbl_category");
		sm.write(" ORDER BY category_code asc");
		
		//System.out.println("분류 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	/**
	 * 
	 * 하위분류 목록
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getSubdivisionList(String category_code) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		sm.write(" SELECT * FROM tbl_subdivision");
		sm.where("category_code", category_code);
		
		//System.out.println("하위분류 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	
	/**
	 * 보고서 상세 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getReport(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String product_num = props.getProperty("product_num");
		
		sm.write("SELECT A.*, B.file_name, B.file_num, C.category_name, D.subdivision_name FROM (");
		sm.write("		SELECT * FROM tbl_product");
		sm.write("		WHERE product_num= '"+product_num+"'");
		sm.write(") A LEFT OUTER JOIN tbl_file B ");
		sm.write("ON A.product_num = B.product_num");
		sm.write("LEFT OUTER JOIN tbl_category C ");
		sm.write("ON A.category_code = C.category_code");
		sm.write("LEFT OUTER JOIN  tbl_subdivision D");
		sm.write("ON A.subdivision_code = D.subdivision_code");
		
		//System.out.println("보고서 상세 정보 get SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 등록예정 보고서 상세 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getUpcomming(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String upcomming_num = props.getProperty("upcomming_num");
		
		sm.write("SELECT A.*, B.category_name, C.subdivision_name FROM (");
		sm.write("		SELECT * FROM tbl_upcomming");
		sm.write("		WHERE upcomming_num= '"+upcomming_num+"'");
		sm.write(") A LEFT OUTER JOIN tbl_category B ");
		sm.write("ON A.category_code = B.category_code");
		sm.write("LEFT OUTER JOIN  tbl_subdivision C");
		sm.write("ON A.subdivision_code = C.subdivision_code");
		
		//System.out.println("등록예정 보고서 상세 정보 get SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
		
	
	/**
	 * 보고서 date 입력
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 * 오라클 CLOB 관련은 다음을 참조 한다.
	 * @see http://www.oracle.com/technology/sample_code/tech/java/codesnippet/jdbc
	 * 			/clob10g/handlingclobsinoraclejdbc10g.html#createTab
	 */
	public String reportInsert(Properties props) throws BizException {
		int product_num = getProductnum()+1;
		SqlMaker sm = new SqlMaker();
		String category_code 		= props.getProperty("category_code", "");
		String subdivision_code 	= props.getProperty("subdivision_code", "");
		String product_title 			= props.getProperty("product_title", "");
		String product_content 	= props.getProperty("product_content", "");
		
		
		if(category_code.equals("null")||category_code.equals("")){
			category_code = "0";
		}
		if(subdivision_code.equals("null")||subdivision_code.equals("")){
			subdivision_code = "0";
		}
		
		sm.write("INSERT INTO tbl_product ");
		sm.write("(product_num, product_title, product_price, product_content, product_reg_date, category_code, subdivision_code, product_type, product_pcs_cnt, product_month) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = product_num +",'";
		strSql += product_title+"','";
		strSql += props.getProperty("product_price", "0")	+"','";
		strSql += product_content	+"',";
		strSql += "now()"	+",";
		strSql += category_code	+",";
		strSql += subdivision_code	+",'";
		strSql += props.getProperty("product_type", "")	+"',";
		strSql += "0"+",";
		strSql += props.getProperty("product_month", "0");
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("SQL게시물 등록=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
		return Integer.toString(product_num);
	}

	/**
	 * 보고서 게시물 멕스ID값 
	 * @return
	 */
	private int getProductnum(){
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT MAX(product_num) AS max_seq FROM tbl_product ");

		//System.out.println("SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			Object obj = rec.get("max_seq");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	
	/**
	 * 보고서 수정
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void reportUpdate(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		String category_code 		= props.getProperty("category_code", "");
		String subdivision_code 	= props.getProperty("subdivision_code", "");
		String product_title 			= props.getProperty("product_title", "");
		String product_content 	= props.getProperty("product_content", "");
		
		product_title	= StrUtil.insertReplace(product_title);
		product_content	= StrUtil.insertReplace(product_content);
		
		if(category_code.equals("null")||category_code.equals("")){
			category_code = "0";
		}
		if(subdivision_code.equals("null")||subdivision_code.equals("")){
			subdivision_code = "0";
		}
    	
		sm.write("UPDATE tbl_product ");
		sm.set("product_title",product_title);
		sm.set("product_content",product_content);
		sm.set("category_code",category_code);
		sm.set("subdivision_code",subdivision_code);
		sm.set("product_price",props.getProperty("product_price"));
		sm.set("product_month",props.getProperty("product_month","0"));
		sm.where("product_num",props.getProperty("product_num"));

		//System.out.println("보고서 수정SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
		
	
	/**
	 * 보고서 첨부파일 등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 * 오라클 CLOB 관련은 다음을 참조 한다.
	 * @see http://www.oracle.com/technology/sample_code/tech/java/codesnippet/jdbc
	 * 			/clob10g/handlingclobsinoraclejdbc10g.html#createTab
	 */
	public void fileInsert(Properties props, String product_num) throws BizException {
		int file_num = getFilenum()+1;
		SqlMaker sm = new SqlMaker();
		
		sm.write("INSERT INTO tbl_file ");
		sm.write("(file_num, product_num, file_name) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = file_num +",";
		strSql += product_num+",'";
		strSql += props.getProperty("file_name", "")+"'";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("보고서 첨부파일 등록=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}

	/**
	 * 파일 게시물 멕스ID값 
	 * @return
	 */
	private int getFilenum(){
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT MAX(file_num) AS max_seq FROM tbl_file ");

		//System.out.println("SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			Object obj = rec.get("max_seq");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 보고서 첨부파일 수정
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void fileUpdate(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE tbl_file ");
		sm.set("file_name",props.getProperty("file_name"));
		sm.where("product_num",props.getProperty("product_num"));

		//System.out.println("보고서 첨부파일 수정SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	
	/**
	 * 파일 삭제
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void fileDelete(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("DELETE FROM tbl_file ");
		sm.write("WHERE product_num = '"+props.getProperty("product_num", "")+"'");

		//System.out.println("파일 삭제 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	/**
	 * 보고서 삭제
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void reportDelete(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("DELETE FROM tbl_product ");
		sm.write("WHERE product_num = '"+props.getProperty("product_num", "")+"'");

		//System.out.println("보고서 삭제 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	/**
	 * 등록예정 보고서 date 입력
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 * 오라클 CLOB 관련은 다음을 참조 한다.
	 * @see http://www.oracle.com/technology/sample_code/tech/java/codesnippet/jdbc
	 * 			/clob10g/handlingclobsinoraclejdbc10g.html#createTab
	 */
	public void upcommingInsert(Properties props) throws BizException {
		int upcomming_num = getUpcommingnum()+1;
		SqlMaker sm = new SqlMaker();
		
		String category_code 			= props.getProperty("category_code", "");
		String subdivision_code 		= props.getProperty("subdivision_code", "");
		String upcomming_title 		= props.getProperty("upcomming_title", "");
		String upcomming_content 	= props.getProperty("upcomming_content", "");
		String upcomming_date 		= props.getProperty("upcomming_date", "");
		
		upcomming_title					= StrUtil.insertReplace(upcomming_title);
		upcomming_content			= StrUtil.insertReplace(upcomming_content);
		
		
		upcomming_date 				= "str_to_date('"+upcomming_date+"','%m/%d/%Y')";
		
		if(category_code.equals("null")||category_code.equals("")){
			category_code = "0";
		}
		if(subdivision_code.equals("null")||subdivision_code.equals("")){
			subdivision_code = "0";
		}
		
		sm.write("INSERT INTO tbl_upcomming ");
		sm.write("(upcomming_num, upcomming_title, upcomming_content, upcomming_reg_date, upcomming_date, category_code, subdivision_code, upcomming_flag) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = upcomming_num +",'";
		strSql += upcomming_title	+"','";
		strSql += upcomming_content	+"',";
		strSql += "now()"	+",";
		strSql += upcomming_date	+",";
		strSql += category_code	+",";
		strSql += subdivision_code	+",";
		strSql += "0";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("SQL게시물 등록=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}

	/**
	 * 등록예정 보고서 게시물 멕스ID값 
	 * @return
	 */
	private int getUpcommingnum(){
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT MAX(upcomming_num) AS max_seq FROM tbl_upcomming ");

		//System.out.println("SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			Object obj = rec.get("max_seq");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}	
	
	
	
	/**
	 * 
	 * 문의/판매요청 목록
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getInquiryList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(10);
		int totalCnt = inquiryCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		String menu_type2 = props.getProperty("menu_type2", "A");
		
		
		sm.write(" SELECT * FROM tbl_inquiry");
		sm.write(" WHERE inquiry_type = '"+menu_type2+"'");
		sm.write(" limit "+first+", "+list.itemRange);
		
		//System.out.println("문의/판매요청 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	
	/**
	 * 조회할 데이터의 row count 를 계산한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int inquiryCount(Properties props) throws BizException {
		String menu_type2 = props.getProperty("menu_type2", "A");
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM tbl_inquiry");
		sql.write(" WHERE inquiry_type = '"+menu_type2+"'");
		
		//System.out.println("count SQL=" + sql.getSql());
		
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sql.getSql());
			Object obj = rec.get("row_count");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * 문의/판매요청 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getInquiry(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String inquiry_num = props.getProperty("inquiry_num");
		
		sm.write("SELECT C.*,D.country_name FROM (");
		sm.write("		SELECT A.*, B.onzt_type_name FROM (");
		sm.write("			SELECT * FROM tbl_inquiry");
		sm.write("			WHERE inquiry_num = '"+inquiry_num+"'");
		sm.write("		) A LEFT OUTER JOIN tbl_organization_type B");
		sm.write("		ON A.onzt_type_code = B.onzt_type_code");
		sm.write(") C LEFT OUTER JOIN tbl_country D");
		sm.write("ON C.country_code = D.country_code");
		
		//System.out.println("문의/판매요청 정보 get SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 
	 * 메뉴 목록
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getMenuList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		sm.write(" SELECT A.*, B.subdivision_code, B.subdivision_name FROM tbl_category A");
		sm.write(" LEFT OUTER JOIN tbl_subdivision B");
		sm.write(" ON A.category_code = B.category_code");
		sm.write(" ORDER BY A.category_code");
		
		//System.out.println("메뉴 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}

	
	/**
	 * 상위 카테고리 입력
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 * 오라클 CLOB 관련은 다음을 참조 한다.
	 * @see http://www.oracle.com/technology/sample_code/tech/java/codesnippet/jdbc
	 * 			/clob10g/handlingclobsinoraclejdbc10g.html#createTab
	 */
	public void categoryInsert(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		
		sm.write("INSERT INTO tbl_category ");
		sm.write("(category_code, category_name) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = props.getProperty("category_code", "") +",'";
		strSql += props.getProperty("category_name", "")	+"'";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("SQL게시물 등록=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}

	/**
	 * 상위 카테고리 삭제
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void categoryDel(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();

		int category_code = Integer.parseInt(props.getProperty("category_code"));
		sm.write("DELETE FROM tbl_category ");
		sm.where("category_code",category_code);

		//System.out.println("카테고리 삭제=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	/**
	 * 하위 카테고리 삭제
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void subdivisionDel(Properties props, String del_code) throws BizException {
		SqlMaker sm = new SqlMaker();
		int category_code = Integer.parseInt(props.getProperty("category_code"));
		
		if(del_code.equals("")){
			sm.write("DELETE FROM tbl_subdivision ");
			sm.write("WHERE category_code = '"+category_code+"'");
		}else{
			sm.write("DELETE FROM tbl_subdivision ");
			sm.write("WHERE category_code = '"+category_code+"'");
			sm.write("AND subdivision_code = '"+del_code+"'");
		}

		//System.out.println("하위 카테고리 삭제=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	/**
	 * 하위 카테고리 입력
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 * 오라클 CLOB 관련은 다음을 참조 한다.
	 * @see http://www.oracle.com/technology/sample_code/tech/java/codesnippet/jdbc
	 * 			/clob10g/handlingclobsinoraclejdbc10g.html#createTab
	 */
	public void subdivisionInsert(Properties props, String add_txt) throws BizException {
		SqlMaker sm = new SqlMaker();
		int getSubnum = getSubnum()+1;
		sm.write("INSERT INTO tbl_subdivision ");
		sm.write("(subdivision_code, category_code, subdivision_name) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = getSubnum +",'";
		strSql += props.getProperty("category_code", "")	+"','";
		strSql += add_txt	+"'";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("하위 카테고리 등록=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
		
	}
	
	
	/**
	 * 하위 카테고리 멕스ID값 
	 * @return
	 */
	private int getSubnum(){
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT MAX(subdivision_code) AS max_seq FROM tbl_subdivision ");

		//System.out.println("SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			Object obj = rec.get("max_seq");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	
	/**
	 * 등록 예정 보고서 목록
	 * 조회할때 반드시 count() method 를 호출 하여 데이터의 숫자를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getUpcommingList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(10);
		int totalCnt = upcommingCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		
		sm.write(" SELECT * FROM tbl_upcomming");
		sm.write(" WHERE upcomming_flag = '0'");
		sm.write(" ORDER BY upcomming_num desc");
		sm.write(" limit "+first+", "+list.itemRange);
		
		//System.out.println("게시판 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	/**
	 * 조회할 데이터의 row count 를 계산한다.
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int upcommingCount(Properties props) throws BizException {
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM tbl_upcomming");
		sql.write(" WHERE upcomming_flag = '0'");
		
		//System.out.println("count SQL=" + sql.getSql());
		
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sql.getSql());
			Object obj = rec.get("row_count");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * 등록 예정 보고서 등록 후 Flag 변경
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void upcommingFlag(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		try {
			sm.write("UPDATE tbl_upcomming ");
			sm.set("upcomming_flag","1");
			sm.where("upcomming_num",props.getProperty("upcomming_num"));
			//System.out.println("등록 예정 보고서 등록 후 Flag 변경 SQL=" + sm.getSql());
		
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			//System.out.println("Data mapping Error = " + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	/**
	 * 등록 예정 보고서 수정
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void upcommingUpdate(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		String upcomming_title 		= props.getProperty("upcomming_title", "");
		String upcomming_content 	= props.getProperty("upcomming_content", "");
		String upcomming_date 		= props.getProperty("upcomming_date", "");
		
		upcomming_title					= StrUtil.insertReplace(upcomming_title);
		upcomming_content			= StrUtil.insertReplace(upcomming_content);
		
		upcomming_date 				= "str_to_date('"+upcomming_date+"','%m/%d/%Y')";
		
		sm.write("UPDATE tbl_upcomming ");
		
		sm.set("upcomming_title",upcomming_title);
		sm.set("upcomming_content",upcomming_content);
		sm.write(", upcomming_date = "+upcomming_date);
		sm.set("category_code",props.getProperty("category_code", "0"));
		sm.set("subdivision_code",props.getProperty("subdivision_code", "0"));
		
		sm.where("upcomming_num",props.getProperty("upcomming_num"));
		
		//System.out.println("등록 예정 보고서 수정 SQL=" + sm.getSql());
	
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			//System.out.println("Data mapping Error = " + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	/**
	 * 등록 예정 보고서 삭제
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void upcommingDelete(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();

		sm.write("DELETE FROM tbl_upcomming ");
		sm.write("WHERE upcomming_num = '"+props.getProperty("upcomming_num", "")+"'");

		//System.out.println("등록 예정 보고서 삭제=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	/**
	 * 구매 리스트
	 * 조회할때 반드시 count() method 를 호출 하여 데이터의 숫자를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getPurchaseList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(10);
		int totalCnt = purchaseCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		sm.write("SELECT C.*, D.product_title, D.product_type, D.product_price FROM (");
		sm.write(" 	SELECT A.*, B.pay_price, B.pay_refunded_price, B.pay_date FROM tbl_purchase A, tbl_pay B");
		sm.write(" 	WHERE A.pay_num = B.pay_num");
		sm.write(" 	AND B.pay_flag in ('1','2')");
		sm.write(") C LEFT OUTER JOIN tbl_product D");
		sm.write("ON C.product_num = D.product_num");
		sm.write("ORDER BY C.pay_date desc");
		sm.write("limit "+first+", "+list.itemRange);
		
		//System.out.println("구매 리스트 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	/**
	 * 조회할 데이터의 row count 를 계산한다.
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int purchaseCount(Properties props) throws BizException {
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM tbl_purchase A, tbl_pay B");
		sql.write("WHERE A.pay_num = B.pay_num");
		sql.write("AND B.pay_flag in ('1','2')");
		
		//System.out.println("purchaseCount SQL=" + sql.getSql());
		
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sql.getSql());
			Object obj = rec.get("row_count");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * 구매 상세 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getpurchase(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String pcs_num = props.getProperty("pcs_num");
		
		sm.write("SELECT A.*, B.product_title, B.product_type, B.product_price, B.category_code, B.subdivision_code, C.category_name, D.subdivision_name FROM (");
		sm.write("		");
		sm.write("		SELECT E.*, F.PAY_DATE, F.PAY_PRICE, F.PAY_REFUNDED_PRICE, F.PAY_TXN_ID FROM tbl_purchase E, tbl_pay F");
		sm.write("		WHERE E.pcs_num= '"+pcs_num+"'");
		sm.write("		AND E.pay_num = F.pay_num");
		sm.write(") A LEFT OUTER JOIN tbl_product B ");
		sm.write("ON A.product_num = B.product_num");
		sm.write("LEFT OUTER JOIN tbl_category C ");
		sm.write("ON B.category_code = C.category_code");
		sm.write("LEFT OUTER JOIN  tbl_subdivision D");
		sm.write("ON B.subdivision_code = D.subdivision_code");
		
		//System.out.println("구매 상세 정보 get SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	

	/**
	 * 유저 리스트
	 * 조회할때 반드시 count() method 를 호출 하여 데이터의 숫자를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getMemberList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(10);
		int totalCnt = memberCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		sm.write(" SELECT * FROM tbl_user");
		sm.write(" ORDER BY user_email desc");
		sm.write(" limit "+first+", "+list.itemRange);
		
		//System.out.println("유저 리스트 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	/**
	 * 조회할 데이터의 row count 를 계산한다.
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int memberCount(Properties props) throws BizException {
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM tbl_user");
		
		//System.out.println("count SQL=" + sql.getSql());
		
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sql.getSql());
			Object obj = rec.get("row_count");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * 유저 상세 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getMember(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String user_email = props.getProperty("user_email");
		
		sm.write("SELECT A.*, B.ONZT_TYPE_NAME, C.COUNTRY_NAME FROM (");
		sm.write("		SELECT * FROM tbl_user");
		sm.write("		WHERE user_email= '"+user_email+"'");
		sm.write(") A LEFT OUTER JOIN tbl_organization_type B");
		sm.write("ON A.ONZT_TYPE_CODE = B.ONZT_TYPE_CODE");
		sm.write("LEFT OUTER JOIN tbl_country C");
		sm.write("ON A.COUNTRY_CODE = C.COUNTRY_CODE");
		
		//System.out.println("유저 상세 정보 get SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 보고서 번호를 이용해 파일정보를 가져온다
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public String getFile(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String file_num = "";
		sm.write(" SELECT file_num FROM tbl_file");
		sm.write(" WHERE product_num = '"+props.getProperty("product_num")+"'");
		
		//System.out.println("get File SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			file_num = rec.get("file_num","");
			return file_num;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * 홍보 목록
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getPromotionList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		int totalCnt = promotionCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		
		sm.write(" SELECT * FROM tbl_promotion");
		sm.write(" WHERE promotion_code = '"+props.getProperty("menu_type2", "A")+"'");
		sm.write(" ORDER BY promotion_num asc");
		
		//System.out.println("홍보 목록 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	
	/**
	 * 조회할 데이터의 row count 를 계산한다.
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int promotionCount(Properties props) throws BizException {
		
		sql.reset();
		sql.write(" SELECT count(*) as row_count FROM tbl_promotion");
		sql.write(" WHERE promotion_code = '"+props.getProperty("menu_type2", "A")+"'");
		
		//System.out.println("count SQL=" + sql.getSql());
		
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sql.getSql());
			Object obj = rec.get("row_count");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
	/**
	 * 프로모션 등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 * 오라클 CLOB 관련은 다음을 참조 한다.
	 * @see http://www.oracle.com/technology/sample_code/tech/java/codesnippet/jdbc
	 * 			/clob10g/handlingclobsinoraclejdbc10g.html#createTab
	 */
	public void promotionInsert(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		int promotion_num = getPromotionSeq()+1;
		
		sm.write("INSERT INTO tbl_promotion ");
		sm.write("(promotion_num, promotion_title, promotion_img, promotion_code, product_num) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = promotion_num +",'";
		strSql += props.getProperty("promotion_title", "")	+"','";
		strSql += props.getProperty("file_name", "")	+"','";
		strSql += props.getProperty("promotion_code", "")	+"','";
		strSql += props.getProperty("product_num", "")	+"'";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("프로모션 SQL게시물 등록=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}
	
	/**
	 * 프로모션 게시물 멕스ID값 
	 * @return
	 */
	private int getPromotionSeq(){
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT MAX(promotion_num) AS max_seq FROM tbl_promotion ");

		//System.out.println("SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			Object obj = rec.get("max_seq");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 프로모션 상세 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getPromotion(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String promotion_num = props.getProperty("promotion_num");
		
		sm.write("SELECT * FROM (");
		sm.write("		SELECT * FROM tbl_promotion");
		sm.write("		WHERE promotion_num= '"+promotion_num+"'");
		sm.write(") A LEFT OUTER JOIN tbl_product B ");
		sm.write("ON A.product_num = B.product_num");
		
		//System.out.println("프로모션 상세 정보 get SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 프로모션 수정
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void promotionUpdate(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		String promotion_num 	= props.getProperty("promotion_num", "");
		String promotion_title 		= props.getProperty("promotion_title", "");
		String promotion_img 		= props.getProperty("file_name", "");
		String promotion_code 	= props.getProperty("promotion_code", "");
		String product_num 		= props.getProperty("product_num", "");
		
		sm.write("UPDATE tbl_promotion ");
		sm.set("promotion_title",promotion_title);
		sm.set("promotion_img",promotion_img);
		sm.set("promotion_code",promotion_code);
		sm.set("product_num",product_num);
		sm.where("promotion_num",promotion_num);

		//System.out.println("프로모션 수정SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	
	/**
	 * 프로모션 삭제
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void promotionDel(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("DELETE FROM tbl_promotion ");
		sm.write("WHERE promotion_num = '"+props.getProperty("promotion_num", "")+"'");

		//System.out.println("프로모션 삭제=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","delete 오류");
		}
	}
	
	
	
	/**
	 * 샘플 파일 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getSampleFile(String sample_num) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("	SELECT * FROM tbl_sample");
		sm.write("	WHERE sample_num= '"+sample_num+"'");
		
		//System.out.println(" 샘플 파일 get SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	/**
	 * 샘플파일 수정
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void sampleFileUpdate(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE tbl_sample ");
		sm.set("sample_content",props.getProperty("sample_content"));
		sm.set("sample_file",props.getProperty("file_name"));
		sm.where("sample_num",props.getProperty("sample_num"));

		//System.out.println("샘플파일 수정SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	/**
	 * 샘플 파일 삭제
	 * 
	 * 삭제 명령이 왔을 경우 sample_num은 유지
	 * 그 외 자료만 삭제
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void sampleFileDelete(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("update tbl_sample ");
		sm.set("sample_content", "");
		sm.set("sample_file", "");
		sm.write("WHERE sample_num = '"+props.getProperty("sample_num", "")+"'");

		//System.out.println("샘플 파일 삭제=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	
	/**
	 * 
	 * 구독 신청 목록
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getSubscriptionList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(10);
		int totalCnt = subscriptionCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		String flag_value 	= props.getProperty("flag_value", "");
		String whereSql		= "";
		
		if(!flag_value.equals("")){
			whereSql = "WHERE sst_flag = '"+flag_value+"'";
		}
		
		sm.write(" SELECT * FROM tbl_subscription");
		sm.write(whereSql);
		sm.write(" ORDER BY sst_reg_date desc");
		sm.write(" limit "+first+", "+list.itemRange);
		
		//System.out.println("구독 신청 목록 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	/**
	 * 조회할 데이터의 row count 를 계산한다.
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int subscriptionCount(Properties props) throws BizException {
		
		String flag_value 	= props.getProperty("flag_value", "");
		String whereSql		= "";
		
		if(!flag_value.equals("")){
			whereSql = "WHERE sst_flag = '"+flag_value+"'";
		}
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM tbl_subscription");
		sql.write(whereSql);
		
		//System.out.println("subscriptionCount SQL=" + sql.getSql());
		
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sql.getSql());
			Object obj = rec.get("row_count");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
	/**
	 * 구독 신청 상세내용 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getSubscription(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("	SELECT A.*, B.user_firstname, B.user_lastname, B.user_job, B.user_onzt, B.user_onzt_website, ");
		sm.write("	B.user_tel, C.onzt_type_name, D.country_name, E.product_title, E.product_content, E.product_price, E.product_month FROM (");
		sm.write("		SELECT * FROM tbl_subscription");
		sm.write("		WHERE sst_num= '"+props.getProperty("sst_num")+"'");
		sm.write("	) A LEFT OUTER JOIN tbl_user B");
		sm.write("	ON A.user_email = B.user_email");
		sm.write("	LEFT OUTER JOIN tbl_organization_type C");
		sm.write("	ON B.onzt_type_code = C.onzt_type_code");
		sm.write("	LEFT OUTER JOIN tbl_country D");
		sm.write("	ON B.country_code = D.country_code");
		sm.write("	LEFT OUTER JOIN tbl_product E");
		sm.write("	ON A.product_num = E.product_num");
		
		//System.out.println(" 구독 신청 상세내용 get SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 답변 목록 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getReplyList(String parents_num) throws BizException {
		SqlMaker sm 			= new SqlMaker();
		ListData list 				= new ListData();
		
		
		sm.write("	SELECT * FROM tbl_reply");
		sm.write("	WHERE parents_num = '"+parents_num +"'");
		
		//System.out.println("답변 목록 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	/**
	 * 구독 서비스 FLAG 변경
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void subscriptionPricing(Properties props, String product_num) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("update tbl_subscription ");
		sm.set("product_num", product_num);
		sm.set("sst_flag", "1");
		sm.write("WHERE sst_num = '"+props.getProperty("sst_num", "")+"'");
		
		//System.out.println("구독 서비스 FLAG 변경 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	/**
	 * 주문 서비스 FLAG 변경
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void orderPricing(Properties props, String product_num) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("update tbl_order ");
		sm.set("product_num", product_num);
		sm.set("order_flag", "1");
		sm.write("WHERE order_num = '"+props.getProperty("order_num", "")+"'");
		
		//System.out.println("주문 서비스 FLAG 변경 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	/**
	 * 사용자 카트 등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void userCartInsert(Properties props, String product_num) throws BizException {
		SqlMaker sm = new SqlMaker();
		int cart_num = getCartnum()+1;

		sm.write("INSERT INTO tbl_cart ");
		sm.write("(cart_num, user_email, product_num, cart_reg_date) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = cart_num	+",'";
		strSql += props.getProperty("user_email", "")	+"','";
		strSql += product_num	+"',";
		strSql += "now()";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("사용자 카트 등록 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}
	
	/**
	 * 카트 게시물 멕스ID값 
	 * @return
	 */
	private int getCartnum(){
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT MAX(cart_num) AS max_seq FROM tbl_cart ");

		//System.out.println("getCartnum SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			Object obj = rec.get("max_seq");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 카트 확인
	 * @return
	 */
	public boolean cartCheck(Properties props, String product_num){
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT cart_num FROM tbl_cart ");
		sm.write(" WHERE user_email = '"+props.getProperty("user_email", "")+"'");
		sm.write(" AND product_num = '"+product_num+"'");

		//System.out.println("cartCheck SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			//System.out.println(">>>"+rec.get("cart_num")+"<<<<");
			if(rec.get("cart_num").equals("")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 답변 등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void replyInsert(Properties props) throws BizException {
		SqlMaker sm 			= new SqlMaker();
		int reply_num			= getReplynum()+1;
		String parents_num 	= props.getProperty("parents_num", "");
		String parents_type		= props.getProperty("parents_type", "");
		String reply_content 	= props.getProperty("reply_content", "");
		
		reply_content			= StrUtil.insertReplace(reply_content);
		
		sm.write("INSERT INTO tbl_reply ");
		sm.write("(reply_num, parents_num, parents_type, reply_content, reply_writer, reply_step, reply_reg_date) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = reply_num 			+",";
		strSql += parents_num		+",'";
		strSql += parents_type		+"','";
		strSql += reply_content	+"','";
		strSql += props.getProperty("reply_writer", "")	+"','";
		strSql += "B',";
		strSql += "now()";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("답변 등록 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}
	
	
	/**
	 * 답변 등록 번호 가져오기
	 * @return
	 */
	private int getReplynum(){
		SqlMaker sm 		= new SqlMaker();
		
		sm.write(" SELECT MAX(reply_num) AS max_seq FROM tbl_reply ");

		//System.out.println("답변 등록 번호 가져오기 SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			Object obj = rec.get("max_seq");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 *  구독 시 답변 등록 FLAG 변경
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void replySubscription(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE tbl_subscription ");
		sm.write("set sst_flag = '5'");
		sm.where("sst_num",props.getProperty("parents_num",""));
		
		//System.out.println("구독 시 답변 등록 FLAG 변경 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
		
	}
	
	/**
	 *  주문 시 답변 등록 FLAG 변경
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void replyOrder(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE tbl_order ");
		sm.write("set order_flag = '5'");
		sm.where("order_num",props.getProperty("parents_num",""));
		
		//System.out.println("주문 시 답변 등록 FLAG 변경 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
		
	}
	
	/**
	 * 
	 * 주문요청 목록
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getOrderList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(10);
		int totalCnt = orderCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		String flag_value 	= props.getProperty("flag_value", "");
		String whereSql		= "";
		
		if(!flag_value.equals("")){
			whereSql = "WHERE order_flag = '"+flag_value+"'";
		}
		
		sm.write(" SELECT * FROM tbl_order");
		sm.write(whereSql);
		sm.write(" ORDER BY order_reg_date desc");
		sm.write(" limit "+first+", "+list.itemRange);
		
		//System.out.println("주문요청 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	/**
	 * 조회할 데이터의 row count 를 계산한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int orderCount(Properties props) throws BizException {
		
		String flag_value 	= props.getProperty("flag_value", "");
		String whereSql		= "";
		
		if(!flag_value.equals("")){
			whereSql = "WHERE order_flag = '"+flag_value+"'";
		}
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM tbl_order");
		sql.write(whereSql);
		
		//System.out.println("count SQL=" + sql.getSql());
		
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sql.getSql());
			Object obj = rec.get("row_count");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * 주문요청 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getOrder(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String order_num = props.getProperty("order_num");
		
		//sm.write("	SELECT * FROM tbl_order");
		//sm.write("	WHERE order_num = '"+order_num+"'");
		sm.write("	SELECT A.*, B.user_firstname, B.user_lastname, B.user_job, B.user_onzt, B.user_onzt_website, ");
		sm.write("	B.user_tel, C.onzt_type_name, D.country_name, E.product_title, E.product_content, E.product_price FROM (");
		sm.write("		SELECT * FROM tbl_order");
		sm.write("		WHERE order_num= '"+order_num+"'");
		sm.write("	) A LEFT OUTER JOIN tbl_user B");
		sm.write("	ON A.user_email = B.user_email");
		sm.write("	LEFT OUTER JOIN tbl_organization_type C");
		sm.write("	ON B.onzt_type_code = C.onzt_type_code");
		sm.write("	LEFT OUTER JOIN tbl_country D");
		sm.write("	ON B.country_code = D.country_code");
		sm.write("	LEFT OUTER JOIN tbl_product E");
		sm.write("	ON A.product_num = E.product_num");
		
		//System.out.println("주문요청 정보 get SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	/**
	 * 사용자 샘플 다운로드 정보 리스트
	 * 조회할때 반드시 count() method 를 호출 하여 데이터의 숫자를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getSampleDownList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(10);
		int totalCnt = sampledownCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		sm.write(" SELECT * FROM tbl_downinfo");
		sm.write(" ORDER BY di_email desc");
		sm.write(" limit "+first+", "+list.itemRange);
		
		//System.out.println("사용자 샘플 다운로드 정보 리스트 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	/**
	 * 조회할 데이터의 row count 를 계산한다.
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int sampledownCount(Properties props) throws BizException {
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM tbl_downinfo");
		
		//System.out.println("count SQL=" + sql.getSql());
		
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sql.getSql());
			Object obj = rec.get("row_count");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * 사용자 샘플 다운로드 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getSampleDownInfo(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String di_num = props.getProperty("di_num");
		
		sm.write("SELECT A.*, B.COUNTRY_NAME FROM (");
		sm.write("		SELECT * FROM tbl_downinfo");
		sm.write("		WHERE di_num= '"+di_num+"'");
		sm.write(") A LEFT OUTER JOIN tbl_country B");
		sm.write("ON A.COUNTRY_CODE = B.COUNTRY_CODE");
		
		//System.out.println("사용자 샘플 다운로드 정보 get SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 방문 정보 목록
	 * 조회할때 반드시 count() method 를 호출 하여 데이터의 숫자를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getVisitDayStats(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(10);
		int totalCnt = visitDayStatsCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		String whereSql 		= "";
		String search_year		= props.getProperty("search_year", "");
		String search_month	= props.getProperty("search_month", "");
		String search_day		= props.getProperty("search_day", "");
		String search_date 		= props.getProperty("search_date", "");
		String search_ip 		= props.getProperty("search_ip", "");
		
		if(!search_date.equals("")){
			search_year 		= search_date.substring(6, 10);
			search_month 	= search_date.substring(0, 2);
			search_day 		= search_date.substring(3, 5);
			
			whereSql = "WHERE visit_year ="+search_year;
			whereSql += " AND visit_month ="+search_month;
			whereSql += " AND visit_day ="+search_day;
		}else{
			if(!search_year.equals("") && !search_month.equals("") && !search_day.equals("")){
				whereSql = "WHERE visit_year ="+search_year;
				whereSql += " AND visit_month ="+search_month;
				whereSql += " AND visit_day ="+search_day;
			}
		}
		
		if(!search_ip.equals("")){
			if(whereSql.equals("")){
				whereSql = "WHERE visit_ip = '"+search_ip+"'";
			}else{
				whereSql += " AND visit_ip = '"+search_ip+"'";
			}
		}
		
		sm.write("SELECT * FROM tbl_visit");
		sm.write(whereSql);
		sm.write("ORDER BY visit_year desc, visit_month desc, visit_day desc");
		sm.write("limit "+first+", "+list.itemRange);
		
		//System.out.println("방문 정보 목록 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	
	/**
	 * 조회할 데이터의 row count 를 계산한다.
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int visitDayStatsCount(Properties props) throws BizException {
		
		String whereSql 		= "";
		String search_year		= "";
		String search_month	= "";
		String search_day		= "";
		String search_date 		= props.getProperty("search_date", "");
		String search_ip 		= props.getProperty("search_ip", "");
		
		if(!search_date.equals("")){
			search_year 		= search_date.substring(6, 10);
			search_month 	= search_date.substring(0, 2);
			search_day 		= search_date.substring(3, 5);
			
			whereSql = "WHERE visit_year ="+search_year;
			whereSql += " AND visit_month ="+search_month;
			whereSql += " AND visit_day ="+search_day;
		}else{
			if(!search_year.equals("") && !search_month.equals("") && !search_day.equals("")){
				whereSql = "WHERE visit_year ="+search_year;
				whereSql += " AND visit_month ="+search_month;
				whereSql += " AND visit_day ="+search_day;
			}
		}
		
		if(!search_ip.equals("")){
			if(whereSql.equals("")){
				whereSql = "WHERE visit_ip = '"+search_ip+"'";
			}else{
				whereSql += " AND visit_ip = '"+search_ip+"'";
			}
		}
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM tbl_visit");
		sql.write(whereSql);
		
		//System.out.println("count SQL=" + sql.getSql());
		
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sql.getSql());
			Object obj = rec.get("row_count");
			return ParamUtil.getIntParameter(obj, 0);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
	/**
	 * 사용자 접속 정보 가져오기 [연도별]
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getVisitYearStats(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		String search_year		= props.getProperty("search_year", "");

		if(search_year.equals("")){
			search_year = DateUtil.getYYYY();
		}
		
		sm.write("SELECT A.v_mm, B.* FROM (");
		for(int i=1; i< 12; i++){
		sm.write("		SELECT '"+i+"' AS v_mm FROM DUAL UNION ALL ");
		}
		sm.write("		SELECT '12' AS v_mm FROM DUAL");
		sm.write(") A LEFT OUTER JOIN (");
		sm.write("		SELECT visit_month, COUNT(*) AS cnt FROM tbl_visit ");
		sm.write("		WHERE visit_year = "+search_year );
		sm.write("		GROUP BY visit_month");
		sm.write(") B");
		sm.write("ON A.v_mm = B.visit_month");
		
		//System.out.println("방문 정보 목록 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	
	/**
	 * 사용자 접속 정보 가져오기 [월별]
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getVisitMonthStats(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		String search_year		= props.getProperty("search_year", "");
		String search_month	= props.getProperty("search_month", "");

		if(search_year.equals("")){
			search_year = DateUtil.getYYYY();
		}
		if(search_month.equals("")){
			search_month = DateUtil.getMM();
		}
		
		sm.write("SELECT A.v_dd, B.* FROM (");
		
		if(search_month.equals("1")||search_month.equals("3")||search_month.equals("5")||search_month.equals("7")
				||search_month.equals("8")||search_month.equals("10")||search_month.equals("12")){
			for(int i=1; i< 30; i++){
				sm.write("		SELECT '"+i+"' AS v_dd FROM DUAL UNION ALL ");
			}
			sm.write("		SELECT '31' AS v_dd FROM DUAL");
		}else if(search_month.equals("2")){
			for(int i=1; i< 28; i++){
				sm.write("		SELECT '"+i+"' AS v_dd FROM DUAL UNION ALL ");
			}
			sm.write("		SELECT '29' AS v_dd FROM DUAL");
		}else{
			for(int i=1; i< 29; i++){
				sm.write("		SELECT '"+i+"' AS v_dd FROM DUAL UNION ALL ");
			}
			sm.write("		SELECT '30' AS v_dd FROM DUAL");
		}
		sm.write(") A LEFT OUTER JOIN (");
		sm.write("		SELECT visit_month, visit_day, COUNT(*) AS cnt FROM tbl_visit ");
		sm.write("		WHERE visit_year = "+search_year );
		sm.write("		AND visit_month = "+search_month );
		sm.write("		GROUP BY visit_month, visit_day");
		sm.write(") B");
		sm.write("ON A.v_dd = B.visit_day");
		
		//System.out.println("방문 정보 목록 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	
	
	/**
	 * 방문 연도 정보
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getYearGroup() throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		sm.write("SELECT DISTINCT visit_year FROM tbl_visit");
		sm.write("ORDER BY visit_year DESC");
		
		//System.out.println("방문 연도 정보 SQL=" + sm.getSql());

		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	
}
