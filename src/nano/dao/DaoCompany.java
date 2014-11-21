package nano.dao;


import nano.*;
import nano.util.*;

import java.sql.*;
import java.util.*;

import javax.servlet.http.HttpSession;

import org.apache.log4j.*;

import sun.misc.BASE64Encoder;

import KISA.SHA256;

/*****************************************************************
 * @description : 고객사 관련 로직 처리하기위한 class
 * $Id: DaoUser.java,v 1.13 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 12. 20	정수현		최초작성
 * 
 *****************************************************************/


public class DaoCompany {
	private static Logger logger = Logger.getLogger(DaoCompany.class.getName());
	private Connection conn = null;
	SqlMaker sql = null;
	
	public DaoCompany(Connection conn){
		this.conn = conn;
		sql = new SqlMaker();
	}
	
	
	/**
	 * 고객사 정보 조회
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record login(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		// 비밀번호 암호화 - 나중에 변경
		String user_pw = "";
		SHA256 s = new SHA256( props.getProperty("p_com_pw", "").getBytes() );
		BASE64Encoder Base64Encoder = new BASE64Encoder();	
		user_pw = Base64Encoder.encode(s.GetHashCode());
		//user_pw = props.getProperty("p_com_pw");
		
		
		sm.write("SELECT * FROM t_company ");
		sm.where("com_id",props.getProperty("p_com_id"));
		sm.where("com_pw",user_pw);
		
		//System.out.println("회원정보SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			//logger.debug("로그인 고객사 정보:" + rec.toString());

			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000", "로그인 고객사 조회 오류");
		}
	}

	
	/**
	 * 고객사 목록조회를 위한  row count 를 계산한다.
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int getCompanyCount(Properties props) throws BizException {
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM t_company");
		sql.write(" WHERE com_type='"+ props.getProperty("p_com_type") +"' ");
		/*
		if(!search_keyword.equals("")){
			sql.write(" WHERE (");
			sql.write(" product_title like '%"+search_keyword+"%' OR product_content like '%"+search_keyword+"%'");
			sql.write(" )");
		}else{
			sql.write(" WHERE product_type = '"+menu_type+"'");
		}
		sql.write(" AND product_type in ('A','B','C')");
		
		if(!category_code.equals("")){
			sql.write(" AND category_code = '"+category_code+"'");
		}
		if(!subdivision_code.equals("")){
			sql.write(" AND subdivision_code = '"+subdivision_code+"'");
		}
		*/
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
	 * 고객사 목록을  Select 한다.
	 * 조회할때 반드시 getCompanyCount() method 를 호출 하여 데이터의 총 rows를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getCompanyList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		/*
		String menu_type 			= props.getProperty("menu_type", "A");
		String category_code 		= props.getProperty("category_code", "");
		String subdivision_code 	= props.getProperty("subdivision_code", "");
		String sortBy 				= props.getProperty("sortBy", "1");
		
		String search_keyword 	= props.getProperty("search_keyword", "");
		*/
		int pagesize 				= Integer.parseInt(props.getProperty("pagesize", "5"));
		
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(pagesize);
		
		int totalCnt = getCompanyCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		
		sm.write(" SELECT * FROM t_company ");
		sm.write(" WHERE com_type='"+ props.getProperty("p_com_type") +"' ");
		/*
		if(!search_keyword.equals("")){
			sm.write(" WHERE (");
			sm.write(" product_title like '%"+search_keyword+"%' OR product_content like '%"+search_keyword+"%'");
			sm.write(" )");
		}else{
			sm.write(" WHERE product_type = '"+menu_type+"'");
		}
		sm.write(" AND product_type in ('A','B','C')");
		
		if(!category_code.equals("")){
			sm.write(" AND category_code = '"+category_code+"'");
		}
		if(!subdivision_code.equals("")){
			sm.write(" AND subdivision_code = '"+subdivision_code+"'");
		}
		
		if(sortBy.equals("1")){
			sm.write(" ORDER BY product_reg_date desc");
		}else if(sortBy.equals("2")){
			sm.write(" ORDER BY product_reg_date asc");
		}else if(sortBy.equals("3")){
			sm.write(" ORDER BY product_title asc");
		}else if(sortBy.equals("4")){
			sm.write(" ORDER BY product_title desc");
		}
		*/
		
		//sm.write(" limit "+first+", "+list.itemRange);
		
		//System.out.println("보고서 목록 SQL=" + sm.getSql());

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
	 * 고객사 상세 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getCompanyItem(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String com_id = props.getProperty("p_com_id");
		
		sm.write("SELECT * FROM t_company ");
		sm.write("WHERE com_id='" + com_id + "' ");
		
		/*
		sm.write("SELECT A.*, B.file_name, B.file_num, C.category_name, D.subdivision_name FROM (");
		sm.write("		SELECT * FROM tbl_product");
		sm.write("		WHERE product_num= '"+product_num+"'");
		sm.write(") A LEFT OUTER JOIN tbl_file B ");
		sm.write("ON A.product_num = B.product_num");
		sm.write("LEFT OUTER JOIN tbl_category C ");
		sm.write("ON A.category_code = C.category_code");
		sm.write("LEFT OUTER JOIN  tbl_subdivision D");
		sm.write("ON A.subdivision_code = D.subdivision_code");
		*/
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
	 * 고객사 주문목록조회를 위한  row count 를 계산한다.
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int getCompanyOrderCount(Properties props) throws BizException {
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM t_company_order");
		sql.write(" WHERE com_id='"+ props.getProperty("p_com_id") +"' ");
		/*
		if(!search_keyword.equals("")){
			sql.write(" WHERE (");
			sql.write(" product_title like '%"+search_keyword+"%' OR product_content like '%"+search_keyword+"%'");
			sql.write(" )");
		}else{
			sql.write(" WHERE product_type = '"+menu_type+"'");
		}
		sql.write(" AND product_type in ('A','B','C')");
		
		if(!category_code.equals("")){
			sql.write(" AND category_code = '"+category_code+"'");
		}
		if(!subdivision_code.equals("")){
			sql.write(" AND subdivision_code = '"+subdivision_code+"'");
		}
		*/
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
	 * 고객사 주문 목록을  Select 한다.
	 * 조회할때 반드시 getCompanyOrderCount() method 를 호출 하여 데이터의 총 rows를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getCompanyOrderList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		int pagesize 				= Integer.parseInt(props.getProperty("pagesize", "5"));
		
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(pagesize);
		
		int totalCnt = getCompanyOrderCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		
		sm.write(" SELECT A.*, B.product_title, C.option_text FROM t_company_order A, t_product B, t_option C ");
		sm.write(" WHERE A.com_id='"+ props.getProperty("p_com_id") +"' ");
		sm.write(" AND A.product_id = B.product_id ");
		sm.write(" AND A.option_id = C.option_id ");
		
		sm.write(" ORDER BY A.result_datetime ");

		
		//sm.write(" limit "+first+", "+list.itemRange);
		
		//System.out.println("보고서 목록 SQL=" + sm.getSql());

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
	 * 고객사 신규등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void addCompany(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		// 비밀번호 암호화
		String user_pw = "";
		SHA256 s = new SHA256( props.getProperty("p_com_rpw", "").getBytes() );
		BASE64Encoder Base64Encoder = new BASE64Encoder();	
		user_pw = Base64Encoder.encode(s.GetHashCode());
		//user_pw = props.getProperty("p_com_pw", "");
		
		sm.write("INSERT INTO t_company ");
		sm.write("(com_id, com_pw, com_email, com_type, com_name, reg_datetime, upt_datetime) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = "'";
		strSql += props.getProperty("p_com_rid", "")	+"','";
		strSql += user_pw	+"','";
		strSql += props.getProperty("p_com_email", "")	+"','";
		strSql += props.getProperty("p_com_type", "")	+"','";
		strSql += props.getProperty("p_com_rname", "")	+"',";
		strSql += "now(),";
		strSql += "now()";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("고객사 등록 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}	
	
	
	/**
	 *  고객사 정보 변경
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void updateCompany(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		String user_pw 			= "";
	    // 비밀번호 암호화
		//SHA256 s = new SHA256( props.getProperty("user_password").getBytes() );
		//BASE64Encoder Base64Encoder = new BASE64Encoder();	
		//user_pw = Base64Encoder.encode(s.GetHashCode());
		user_pw = props.getProperty("p_com_pw", "");
	 		
		sm.write("UPDATE t_company ");
		sm.set("com_pw",user_pw);
		sm.set("com_name",props.getProperty("p_com_name"));
		sm.set("com_type",props.getProperty("p_com_type"));
		sm.set("com_reg_no",props.getProperty("p_com_reg_no"));
		sm.set("com_ent_no",props.getProperty("p_com_ent_no"));
		sm.set("ceo_name",props.getProperty("p_ceo_name"));
		sm.set("com_tae",props.getProperty("p_com_tae"));
		sm.set("com_jong",props.getProperty("p_com_jong"));
		sm.set("com_url",props.getProperty("p_com_url"));
		sm.set("region",props.getProperty("p_region"));
		sm.set("post_no",props.getProperty("p_post_no"));
		sm.set("address",props.getProperty("p_address"));
		sm.set("tel_no",props.getProperty("p_tel_no"));
		sm.set("fax_no",props.getProperty("p_fax_no"));
		sm.set("linker_name",props.getProperty("p_linker_name"));
		sm.set("linker_work",props.getProperty("p_linker_work"));
		sm.set("linker_tel_no",props.getProperty("p_linker_tel_no"));
		sm.set("linker_hp_no",props.getProperty("p_linker_hp_no"));
		sm.set("linker_email",props.getProperty("p_linker_email"));
		//sm.set("reg_datetime",props.getProperty("p_post_no"));
		sm.set("upt_datetime","now()");		
		
		sm.where("com_id",props.getProperty("p_com_id"));
		
		//System.out.println("고객사 정보 변경 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
		
	
	/**
	 * 고객사 삭제
	 * 
	 * @param props
	 * @return void
	 * @throws BizException
	 */
	public void deleteCompany(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("DELETE FROM t_company WHERE com_id = '" + props.getProperty("p_com_id") + "' ");
		
		//System.out.println("고객사 비번확인 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 고객사 삭제 - 상태 update로 데이터는 유지..
	 * 
	 * @param props
	 * @return void
	 * @throws BizException
	 */
	public void deleteStateCompany(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE t_company SET use_yn = 'N' ");
		
		//System.out.println("고객사 비번확인 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
	/**
	 * 고객사 비번확인
	 * 
	 * @param props
	 * @return String
	 * @throws BizException
	 */
	public boolean pwCheckCompany(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		String user_pw 			= "";
	    // 비밀번호 암호화
		//SHA256 s = new SHA256( props.getProperty("current_password","").getBytes() );
		//BASE64Encoder Base64Encoder = new BASE64Encoder();	
		//user_pw = Base64Encoder.encode(s.GetHashCode());
		user_pw = props.getProperty("p_com_pw", "");	 		
		
		sm.write("SELECT com_id FROM t_company");
		sm.write("WHERE com_id = '"+props.getProperty("p_com_id", "")+"'");
		sm.write("AND com_pw = '"+user_pw+"'");
		
		//System.out.println("고객사 비번확인 SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			if(rec.get("com_id", "").equals("")){
				return false;
			}else{
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}	
	
	
	
    /*
     * 일단 여기까지 2014-01-07 까지 기본 정의  
     */	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 분류 목록
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getCategoryList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		sm.write("SELECT A.*, B.subdivision_code, B.subdivision_name FROM");
		sm.write("tbl_category A LEFT OUTER JOIN tbl_subdivision B");
		sm.write("ON A.category_code = B.category_code");
		
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
	 * 하위분류 목록
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getSubdivisionList() throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		sm.write(" SELECT * FROM tbl_subdivision");
		//sm.where("category_code", category_code);
		
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
	 * Popular Report 목록
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getPopularList() throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		sm.write("SELECT * FROM tbl_product");
		sm.write("WHERE product_type in ('A','B','C')");
		sm.write("ORDER BY product_pcs_cnt desc");
		sm.write("limit 0, 10");
		
		//System.out.println("Popular Report 목록 SQL=" + sm.getSql());

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
	 * 카테고리명 가져오기
	 * 
	 * @return String
	 * @throws BizException
	 */
	public String getCategoryName(String category_code) throws BizException{
		SqlMaker sm = new SqlMaker();
		String category_name = "";
		
		sm.write("SELECT category_name FROM tbl_category");
		sm.write("WHERE category_code = '"+category_code+"'");
		
		//System.out.println("카테고리명 가져오기  SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			category_name = rec.get("category_name").toString();
			return category_name;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 하위 카테고리명 가져오기
	 * 
	 * @return String
	 * @throws BizException
	 */
	public String getSubdivisionName(String subdivision_code) throws BizException{
		SqlMaker sm = new SqlMaker();
		String subdivision_name = "";
		
		sm.write("SELECT subdivision_name FROM tbl_subdivision");
		sm.write("WHERE subdivision_code = '"+subdivision_code+"'");
		
		//System.out.println("하위 카테고리명 가져오기  SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			subdivision_name = rec.get("subdivision_name").toString();
			return subdivision_name;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
		
		String menu_type 			= props.getProperty("menu_type", "A");
		String category_code 		= props.getProperty("category_code", "");
		String subdivision_code 	= props.getProperty("subdivision_code", "");
		String sortBy 				= props.getProperty("sortBy", "1");
		int pagesize 					= Integer.parseInt(props.getProperty("pagesize", "5"));
		String search_keyword 	= props.getProperty("search_keyword", "");
		
		
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(pagesize);
		int totalCnt = reportCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		
		sm.write(" SELECT * FROM tbl_product");
		if(!search_keyword.equals("")){
			sm.write(" WHERE (");
			sm.write(" product_title like '%"+search_keyword+"%' OR product_content like '%"+search_keyword+"%'");
			sm.write(" )");
		}else{
			sm.write(" WHERE product_type = '"+menu_type+"'");
		}
		sm.write(" AND product_type in ('A','B','C')");
		
		if(!category_code.equals("")){
			sm.write(" AND category_code = '"+category_code+"'");
		}
		if(!subdivision_code.equals("")){
			sm.write(" AND subdivision_code = '"+subdivision_code+"'");
		}
		
		if(sortBy.equals("1")){
			sm.write(" ORDER BY product_reg_date desc");
		}else if(sortBy.equals("2")){
			sm.write(" ORDER BY product_reg_date asc");
		}else if(sortBy.equals("3")){
			sm.write(" ORDER BY product_title asc");
		}else if(sortBy.equals("4")){
			sm.write(" ORDER BY product_title desc");
		}
		
		sm.write(" limit "+first+", "+list.itemRange);
		
		//System.out.println("보고서 목록 SQL=" + sm.getSql());

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
		String menu_type 			= props.getProperty("menu_type", "A");
		String category_code 		= props.getProperty("category_code", "");
		String subdivision_code 	= props.getProperty("subdivision_code", "");
		String search_keyword 	= props.getProperty("search_keyword", "");
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM tbl_product");
		if(!search_keyword.equals("")){
			sql.write(" WHERE (");
			sql.write(" product_title like '%"+search_keyword+"%' OR product_content like '%"+search_keyword+"%'");
			sql.write(" )");
		}else{
			sql.write(" WHERE product_type = '"+menu_type+"'");
		}
		sql.write(" AND product_type in ('A','B','C')");
		
		if(!category_code.equals("")){
			sql.write(" AND category_code = '"+category_code+"'");
		}
		if(!subdivision_code.equals("")){
			sql.write(" AND subdivision_code = '"+subdivision_code+"'");
		}
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
		
		String sortBy 				= props.getProperty("sortBy", "1");
		int pagesize 					= Integer.parseInt(props.getProperty("pagesize", "5"));
		
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(pagesize);
		int totalCnt = upcommingCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		
		sm.write(" SELECT * FROM tbl_upcomming");
		sm.write(" WHERE upcomming_flag = '0'");
		
		if(sortBy.equals("1")){
			sm.write(" ORDER BY upcomming_reg_date desc");
		}else if(sortBy.equals("2")){
			sm.write(" ORDER BY upcomming_reg_date asc");
		}else if(sortBy.equals("3")){
			sm.write(" ORDER BY upcomming_title asc");
		}else if(sortBy.equals("4")){
			sm.write(" ORDER BY upcomming_title desc");
		}
		
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
	 * 등록 예정 보고서 Count
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
	 * 등록 예정 보고서 상세 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getUpcommingReport(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String upcomming_num = props.getProperty("upcomming_num");
		
		sm.write("SELECT A.*, B.category_name, C.subdivision_name FROM (");
		sm.write("		SELECT * FROM tbl_upcomming");
		sm.write("		WHERE upcomming_num= '"+upcomming_num+"'");
		sm.write(") A LEFT OUTER JOIN tbl_category B ");
		sm.write("ON A.category_code = B.category_code");
		sm.write("LEFT OUTER JOIN  tbl_subdivision C");
		sm.write("ON A.subdivision_code = C.subdivision_code");
		
		//System.out.println("등록 예정 보고서 상세 정보 get SQL=" + sm.getSql());
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
	 * 조직 분류 목록
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getOrganizationList() throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		sm.write(" SELECT * FROM tbl_organization_type");
		
		//System.out.println("조직 분류 목록 SQL=" + sm.getSql());

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
	 * 국가 정보 목록
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getCountryList() throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		sm.write(" SELECT * FROM tbl_country");
		
		//System.out.println("국가 정보 목록 SQL=" + sm.getSql());

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
	 * 고객사 등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void registerUser(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		// 비밀번호 암호화
		String user_pw = "";
		SHA256 s = new SHA256( props.getProperty("user_password", "").getBytes() );
		BASE64Encoder Base64Encoder = new BASE64Encoder();	
		user_pw = Base64Encoder.encode(s.GetHashCode());
		
		
		sm.write("INSERT INTO tbl_user ");
		sm.write("(user_email, user_password, user_firstname, user_lastname, user_job, user_department, user_onzt, user_onzt_website, onzt_type_code, user_tel, country_code, user_reg_date, user_grade, user_pay_month, user_term) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = "'";
		strSql += props.getProperty("user_email", "")	+"','";
		strSql += user_pw	+"','";
		strSql += props.getProperty("user_firstname", "")	+"','";
		strSql += props.getProperty("user_lastname", "")	+"','";
		strSql += props.getProperty("user_job", "")	+"','";
		strSql += props.getProperty("user_department", "")	+"','";
		strSql += props.getProperty("user_onzt", "")	+"','";
		strSql += props.getProperty("user_onzt_website", "")	+"','";
		strSql += props.getProperty("onzt_type_code", "")	+"','";
		strSql += props.getProperty("user_tel", "")	+"','";
		strSql += props.getProperty("country_code", "")	+"',";
		strSql += "now(),'";
		strSql += "N',";
		strSql += "0,";
		strSql += "now()";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("고객사 등록 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}
	
	
	/**
	 * 메일 중복 확인
	 * 
	 * @param props
	 * @return String
	 * @throws BizException
	 */
	public boolean mailOverlapCheck(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String user_email 	= props.getProperty("user_email");
		
		sm.write("SELECT user_email FROM tbl_user");
		sm.write("WHERE user_email= '"+user_email+"'");
		
		//System.out.println("메일 중복 확인 SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			if(!rec.get("user_email", "").equals("")){
				return false;
			}else{
				return true;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	

	/**
	 * 요청 등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void InquiryInsert(Properties props) throws BizException {
		SqlMaker sm 			= new SqlMaker();
		int inquiry_num 		= getInquirynum()+1;
		String inquiry_title 		= "";
		String inquiry_content 	= "";
		String inquiry_type 		= "";
		String cmd 				= props.getProperty("cmd", "");
		
		if(cmd.equals("customReport")){
			inquiry_type = "A";
		}else if(cmd.equals("becomePartner")){
			inquiry_type = "B";
		}else if(cmd.equals("contactUs")){
			inquiry_type = "C";
		}
		
		inquiry_title = StrUtil.insertReplace(props.getProperty("inquiry_title", ""));
		inquiry_content = StrUtil.insertReplace(props.getProperty("inquiry_content", ""));
		
		sm.write("INSERT INTO tbl_inquiry ");
		sm.write("(inquiry_num, inquiry_email, inquiry_firstname, inquiry_lastname, inquiry_job, inquiry_department, inquiry_onzt, inquiry_onzt_website, onzt_type_code, inquiry_tel, country_code, inquiry_title, inquiry_content, inquiry_type, inquiry_reg_date) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = inquiry_num	+",'";
		strSql += props.getProperty("inquiry_email", "")	+"','";
		strSql += props.getProperty("inquiry_firstname", "")	+"','";
		strSql += props.getProperty("inquiry_lastname", "")	+"','";
		strSql += props.getProperty("inquiry_job", "")	+"','";
		strSql += props.getProperty("inquiry_department", "")	+"','";
		strSql += props.getProperty("inquiry_onzt", "")	+"','";
		strSql += props.getProperty("inquiry_onzt_website", "")	+"','";
		strSql += props.getProperty("onzt_type_code", "")	+"','";
		strSql += props.getProperty("inquiry_tel", "")	+"','";
		strSql += props.getProperty("country_code", "")	+"','";
		strSql += inquiry_title	+"','";
		strSql += inquiry_content	+"','";
		strSql += inquiry_type+"',";
		strSql += "now()";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("요청 등록 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}
	
	/**
	 * 문의 게시물 멕스ID값 
	 * @return
	 */
	private int getInquirynum(){
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT MAX(inquiry_num) AS max_seq FROM tbl_inquiry ");

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
	 * 유저 정보 가져오기
	 * 
	 * @param props
	 * @return String
	 * @throws BizException
	 */
	public Record getUserinfo(String user_email) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("SELECT * FROM tbl_user");
		sm.write("WHERE user_email= '"+user_email+"'");
		
		//System.out.println("유저 정보 가져오기 SQL=" + sm.getSql());
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
	 * 고객사 주문 목록
	 * 조회할때 반드시 count() method 를 호출 하여 데이터의 숫자를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getInquiryList(Properties props, String ss_user_email) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();

		String sortBy 				= props.getProperty("sortBy", "1");
		int pagesize 					= Integer.parseInt(props.getProperty("pagesize", "5"));
		
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(pagesize);
		int totalCnt = inquiryCount(props, ss_user_email);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		
		sm.write(" SELECT * FROM tbl_inquiry");
		//sm.write(" WHERE inquiry_type = 'C'");
		sm.write(" WHERE inquiry_email = '"+ss_user_email+"'");
		
		if(sortBy.equals("1")){
			sm.write(" ORDER BY inquiry_reg_date desc");
		}else if(sortBy.equals("2")){
			sm.write(" ORDER BY inquiry_reg_date asc");
		}else if(sortBy.equals("3")){
			sm.write(" ORDER BY inquiry_title asc");
		}else if(sortBy.equals("4")){
			sm.write(" ORDER BY inquiry_title desc");
		}
		
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
	 * 고객사 주문 보고서 Count
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int inquiryCount(Properties props, String ss_user_email) throws BizException {
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM tbl_inquiry");
		//sql.write("WHERE inquiry_type = 'A'");
		sql.write("WHERE inquiry_email = '"+ss_user_email+"'");
		
		//System.out.println("고객사 주문 보고서 count SQL=" + sql.getSql());
		
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
	 * 문의 내역 가져오기
	 * 
	 * @param props
	 * @return String
	 * @throws BizException
	 */
	public Record getInquiry(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("SELECT * FROM tbl_inquiry");
		sm.write("WHERE inquiry_num = '"+props.getProperty("inquiry_num", "")+"'");
		
		//System.out.println("문의 내역 가져오기 SQL=" + sm.getSql());
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
	 * 고객사 구매 목록
	 * 조회할때 반드시 count() method 를 호출 하여 데이터의 숫자를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getUserPurchaseList(Properties props, String ss_user_email) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();

		String sortBy 				= props.getProperty("sortBy", "1");
		int pagesize 					= Integer.parseInt(props.getProperty("pagesize", "5"));
		
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(pagesize);
		int totalCnt = purchaseCount(props, ss_user_email);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		sm.write("	SELECT * FROM (");
		sm.write(" 	SELECT A.*, B.PAY_DATE, B.PAY_FLAG, B.PAY_PRICE, B.PAY_TXN_ID FROM tbl_purchase A, tbl_pay B");
		sm.write(" 	WHERE A.user_email = '"+ss_user_email+"'");
		sm.write(" 	AND A.pay_num = B.pay_num");
		sm.write(" 	AND B.pay_flag = '1'");
		sm.write(" 	AND A.down_remaining > 0 ");
		sm.write(" ) C , tbl_product D	");
		sm.write(" WHERE C.product_num = D.product_num");
		sm.write(" AND D.product_type in ('A','B','C')");
		if(sortBy.equals("1")){
			sm.write(" 	ORDER BY C.pay_date desc");
		}else if(sortBy.equals("2")){
			sm.write(" 	ORDER BY C.pay_date asc");
		}else if(sortBy.equals("3")){
			sm.write(" 	ORDER BY D.product_title asc");
		}else if(sortBy.equals("4")){
			sm.write(" 	ORDER BY D.product_title desc");
		}
		sm.write(" 	limit "+first+", "+list.itemRange);
	    	       
		//System.out.println("고객사 구매 목록 SQL=" + sm.getSql());

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
	 * 고객사 구매 내역 Count
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int purchaseCount(Properties props, String ss_user_email) throws BizException {
		
		sql.write(" SELECT count(*) as row_count FROM (");
		sql.write(" 	SELECT A.product_num FROM tbl_purchase A, tbl_pay B");
		sql.write(" 	WHERE A.user_email = '"+ss_user_email+"'");
		sql.write(" 	AND A.pay_num = B.pay_num");
		sql.write(" 	AND B.pay_flag = '1'");
		sql.write(" ) C , tbl_product D	");
		sql.write(" WHERE C.product_num = D.product_num");
		sql.write(" AND D.product_type in ('A','B','C')");
		//System.out.println("고객사 구매 내역 count SQL=" + sql.getSql());
		
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
	 * 구매 내역 가져오기
	 * 
	 * @param props
	 * @return String
	 * @throws BizException
	 */
	public Record getPurchase(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("	SELECT * FROM (");
		sm.write("		SELECT * FROM tbl_purchase");
		sm.write("		WHERE pcs_num = '"+props.getProperty("pcs_num", "")+"'");
		sm.write("	) A LEFT OUTER JOIN tbl_product B");
		sm.write("	ON A.product_num = B.product_num");
		//System.out.println("구매 내역 가져오기 SQL=" + sm.getSql());
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
	 * 카트 등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void addCart(Properties props, String ss_user_email) throws BizException {
		SqlMaker sm = new SqlMaker();
		int cart_num = getCartnum()+1;

		sm.write("INSERT INTO tbl_cart ");
		sm.write("(cart_num, user_email, product_num, cart_reg_date) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = cart_num	+",'";
		strSql += ss_user_email	+"','";
		strSql += props.getProperty("product_num", "")	+"',";
		strSql += "now()";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("카트 등록 SQL=" + sm.getSql());
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
	 * 카트 목록 가져오기
	 * 
	 * @param props
	 * @return String
	 * @throws BizException
	 */
	public ListData getcartList(String ss_user_email) throws BizException{
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		
		sm.write("SELECT A.cart_num, A.user_email, A.cart_reg_date ,B.* FROM (");
		sm.write("		SELECT * FROM tbl_cart");
		sm.write("		WHERE user_email = '"+ss_user_email+"'");
		sm.write("		ORDER BY cart_num DESC");
		sm.write(") A LEFT OUTER JOIN tbl_product B");
		sm.write("ON A.product_num = B.product_num");
		
		//System.out.println("카트 목록 가져오기 SQL=" + sm.getSql());
		try {
			list = DaoCom.executeQueryAll(conn, list, sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","select 오류");
		}
	}
	
	
	/**
	 * 카트 정보 가져오기
	 * @return
	 */
	public Record getcartInfo(String ss_user_email) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT COUNT(*) as row_cnt, SUM(B.product_price) as sum_price FROM ( ");
		sm.write(" 	SELECT * FROM tbl_cart ");
		sm.write(" 	WHERE user_email = '"+ss_user_email+"'");
		sm.write(" ) A LEFT OUTER JOIN tbl_product B ");
		sm.write(" ON A.product_num = B.product_num");
		
		//System.out.println("SQL=" + sm.getSql());
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
	 * 카트 중복 확인
	 * @return
	 */
	public boolean duplicateCheck(Properties props, String ss_user_email) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT cart_num FROM tbl_cart ");
		sm.write(" WHERE user_email = '"+ss_user_email+"'");
		sm.write(" AND product_num = '"+props.getProperty("product_num", "")+"'");
		
		//System.out.println("카트 중복 확인 SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			if(rec.get("cart_num", "").equals("")){
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
	 * 구입 여부 확인
	 * @return
	 */
	public boolean purchaseCheck(Properties props, String ss_user_email) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT pcs_num FROM  tbl_purchase A, tbl_pay B ");
		sm.write(" WHERE A.user_email = '"+ss_user_email+"'");
		sm.write(" AND A.pay_num = B.pay_num");
		sm.write(" AND B.pay_flag = '1'");
		sm.write(" AND product_num = '"+props.getProperty("product_num", "")+"'");
		sm.write(" AND A.down_remaining > 0 ");
		
		//System.out.println("구입 여부 확인 SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			if(rec.get("pcs_num", "").equals("")){
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
	 * 구독 여부 확인
	 * @return
	 */
	public boolean subscriptionCheck(Properties props, String ss_user_email) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT user_grade, user_pay_month, user_term FROM tbl_user ");
		sm.write(" WHERE user_email = '"+ss_user_email+"'");
		sm.write(" AND user_grade != 'N'");
		sm.write(" AND user_pay_month > 0");
		sm.write(" AND user_term > now()");
		
		//System.out.println("구독 여부 확인 SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			if(rec.get("user_grade", "").equals("")){
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
	 * 카트 목록 삭제
	 * 7일이 지난 목록은 삭제
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void cartCheck(String ss_user_email) throws BizException {
		SqlMaker sm = new SqlMaker();

		sm.write("DELETE FROM tbl_cart ");
		sm.write("WHERE user_email = '"+ss_user_email+"'");
		sm.write("AND NOW() > DATE_ADD(cart_reg_date, INTERVAL 7 DAY)");
		
		//System.out.println("카트 목록 확인 후 삭제=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	/**
	 * 카트 목록 삭제
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void removeCart(Properties props) throws BizException {
		SqlMaker sm 		= new SqlMaker();
		String whereSql 	= "";
		
		if(!props.getProperty("custom","").equals("")){		//결제가 된 이후 일 경우
			whereSql = "WHERE user_email = '"+props.getProperty("cart_delete_id", "")+"'";
		}else{
			if(!props.getProperty("product_num","").equals("")){
				whereSql = "WHERE user_email = '"+props.getProperty("user_email", "")+"'";
				whereSql += "AND product_num = '"+props.getProperty("product_num", "")+"'";
			}else{
				whereSql = "WHERE cart_num = '"+props.getProperty("cart_num", "")+"'";
			}
		}
		
		sm.write("DELETE FROM tbl_cart ");
		sm.write(whereSql);
		
		//System.out.println("카트 목록 삭제=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	/**
	 * 임시 비밀번호 변경
	 * 
	 * @param props
	 * @throws BizException
	 */
	public String temporaryPW(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		String user_pw 			= "";
		String temporary_pw 	= "";
		
		//임시 비밀번호 생성
	    for(int i = 0; i < 8; i++){
	     //char upperStr = (char)(Math.random() * 26 + 65);
	     char lowerStr = (char)(Math.random() * 26 + 97);
	     if(i%2 == 0){
	    	 temporary_pw += (int)(Math.random() * 10);
	     }else{
	    	 temporary_pw += lowerStr;
	     }
	    }
	    
	    // 비밀번호 암호화
		SHA256 s = new SHA256( temporary_pw.getBytes() );
		BASE64Encoder Base64Encoder = new BASE64Encoder();	
		user_pw = Base64Encoder.encode(s.GetHashCode());
	 		
		
		sm.write("UPDATE tbl_user ");
		sm.set("user_password",user_pw);
		sm.where("user_email",props.getProperty("find_email"));

		//System.out.println("임시 비밀번호 변경 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			return temporary_pw;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
		
	
	/**
	 *  고객사 정보 변경
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void updateUser(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		String user_pw 			= "";
	    // 비밀번호 암호화
		SHA256 s = new SHA256( props.getProperty("user_password").getBytes() );
		BASE64Encoder Base64Encoder = new BASE64Encoder();	
		user_pw = Base64Encoder.encode(s.GetHashCode());
	 		
		sm.write("UPDATE tbl_user ");
		sm.set("user_password",user_pw);
		sm.set("user_firstname",props.getProperty("user_firstname"));
		sm.set("user_lastname",props.getProperty("user_lastname"));
		sm.set("user_job",props.getProperty("user_job"));
		sm.set("user_department",props.getProperty("user_department"));
		sm.set("user_onzt",props.getProperty("user_onzt"));
		sm.set("user_onzt_website",props.getProperty("user_onzt_website"));
		sm.set("onzt_type_code",props.getProperty("onzt_type_code"));
		sm.set("user_tel",props.getProperty("user_tel"));
		sm.set("user_tweet",props.getProperty("user_tweet"));
		sm.set("country_code",props.getProperty("country_code"));
		sm.where("user_email",props.getProperty("user_email"));
		
		//System.out.println("고객사 정보 변경 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	
	/**
	 * 고객사 비번확인
	 * 
	 * @param props
	 * @return String
	 * @throws BizException
	 */
	public boolean pwCheck(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		String user_pw 			= "";
	    // 비밀번호 암호화
		SHA256 s = new SHA256( props.getProperty("current_password","").getBytes() );
		BASE64Encoder Base64Encoder = new BASE64Encoder();	
		user_pw = Base64Encoder.encode(s.GetHashCode());
	 		
		
		sm.write("SELECT user_email FROM tbl_user");
		sm.write("WHERE user_email = '"+props.getProperty("user_email", "")+"'");
		sm.write("AND user_password = '"+user_pw+"'");
		
		//System.out.println("고객사 비번확인 SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			if(rec.get("user_email", "").equals("")){
				return false;
			}else{
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 메인 프로모션 랜덤으로 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getPromotion1() throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("	SELECT * FROM ( ");
		sm.write("		SELECT * FROM tbl_promotion");
		sm.write("		WHERE promotion_code = 'A'");
		sm.write("		order by rand() limit 1");
		sm.write("	) A LEFT OUTER JOIN tbl_product B");
		sm.write("	ON A.product_num = B.product_num");
		
		//System.out.println("메인 프로모션 랜덤으로 가져오기 get SQL=" + sm.getSql());
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
	 * 메인 프로모션 랜덤으로 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getPromotion2() throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("	SELECT * FROM ( ");
		sm.write("		SELECT * FROM tbl_promotion");
		sm.write("		WHERE promotion_code = 'B'");
		sm.write("		order by rand() limit 1");
		sm.write("	) A LEFT OUTER JOIN tbl_product B");
		sm.write("	ON A.product_num = B.product_num");
		
		//System.out.println("메인 프로모션 랜덤으로 가져오기 get SQL=" + sm.getSql());
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
	 * 결제 테이블 등록
	 * 
	 * @param props
	 * @throws BizException
	 * @return String
	 * 
	 */
	public String inserPay(Properties props) throws BizException {
		SqlMaker sm 			= new SqlMaker();
		int pay_num 			= getPaynum()+1;
		
		sm.write("INSERT INTO tbl_pay ");
		sm.write("(pay_num, pay_price, pay_flag, pay_date, pay_txn_id, pay_refunded_price ) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = pay_num	+",'";
		strSql += props.getProperty("total_price", "")	+"','";
		strSql += "0',now(),'',0";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("결제 테이블 등록 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			return Integer.toString(pay_num);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}
	
	/**
	 * 결제 게시물 멕스ID값 
	 * @return
	 */
	private int getPaynum(){
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT MAX(pay_num) AS max_seq FROM tbl_pay ");

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
	 * 구매 테이블 등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void inserPurchase(Properties props, String ss_user_email, String pay_num, String product_num) throws BizException {
		SqlMaker sm 			= new SqlMaker();
		int pcs_num 			= getPcsnum()+1;
		
		sm.write("INSERT INTO tbl_purchase ");
		sm.write("(pcs_num, user_email, product_num, pay_num, down_remaining) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = pcs_num	+",'";
		strSql += ss_user_email	+"',";
		strSql += product_num	+",";
		strSql += pay_num	+",";
		strSql += "3";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("구매 테이블 등록 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}
	
	
	/**
	 * 구매 게시물 멕스ID값 
	 * @return
	 */
	private int getPcsnum(){
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT MAX(pcs_num) AS max_seq FROM tbl_purchase ");

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
	 *  결제 성공
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void payComplete(Properties props, String pay_num) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE tbl_pay ");
		sm.set("pay_flag","1");
		sm.set("pay_txn_id",props.getProperty("txn_id"));
		sm.where("pay_num",pay_num);
		
		//System.out.println("결제 성공 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}

	/**
	 *  환불 성공
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void payRefunded(Properties props, String pay_num) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE tbl_pay ");
		sm.set("pay_flag","2");
		sm.set("pay_refunded_price",props.getProperty("payment_gross","0"));
		sm.where("pay_txn_id",props.getProperty("parent_txn_id"));
		sm.where("pay_num",pay_num);
		
		//System.out.println("환불 성공 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}

	/**
	 * 결제 완료 시 판매횟수 증가
	 * 
	 * @param recno
	 */
	public void addPurchaseCount(String recno){
		SqlMaker sm = new SqlMaker();

		sm.write("update tbl_product");
		sm.setNumber("product_pcs_cnt", " product_pcs_cnt + 1");
		sm.whereInt("product_num",recno);
		
		//System.out.println("addPurchaseCount SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 결제 완료 목록
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getpayList(Properties props, String pay_num) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		/*
		sm.write(" SELECT * FROM (");
		sm.write(" 	SELECT A.PCS_NUM, A.USER_EMAIL, A.PRODUCT_NUM, A.DOWN_REMAINING, B.* FROM (");
		sm.write(" 		SELECT * FROM tbl_purchase");
		sm.write(" 		WHERE user_email = '"+ss_user_email +"'");
		sm.write(" 	) A, tbl_pay B");
		sm.write(" 	WHERE A.pay_num = B.pay_num ");
		sm.write(" 	AND pay_flag = '1'");
		sm.write("	) C, tbl_product D");
		sm.write("	WHERE C.product_num = D.product_num");
		*/
		sm.write("	SELECT A.product_num, B.category_code, B.product_month, B.product_type FROM (");
		sm.write("		SELECT product_num FROM tbl_purchase");
		sm.write("		WHERE pay_num = '"+pay_num +"'");
		sm.write("	) A left outer join tbl_product B");
		sm.write("	ON A.product_num = B.product_num");
		
		//System.out.println("결제 완료 목록 SQL=" + sm.getSql());

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
	 * 결제 이후 카트 목록 삭제
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void afterPay(String ss_user_email) throws BizException {
		SqlMaker sm = new SqlMaker();

		sm.write("DELETE FROM tbl_cart ");
		sm.write("WHERE user_email = '"+ss_user_email+"'");
		
		//System.out.println("결제 이후 카트 목록 삭제=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	/**
	 * 결제 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getpayInfo(Properties props, String ss_user_email) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("	SELECT * FROM tbl_purchase A, tbl_pay B");
		sm.write("	WHERE user_email = '"+ss_user_email+"'");
		sm.write("	AND product_num = '"+props.getProperty("product_num")+"'");
		sm.write("	AND A.pay_num = B.pay_num");
		sm.write("	AND B.pay_flag = '1'");
		sm.write("	AND A.down_remaining > 0");
		
		//System.out.println("결제 정보 가져오기 get SQL=" + sm.getSql());
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
	 *  고객사 구독 신청 완료
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void subscribeAdd(String category_code, int product_month, String ss_user_email) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		String user_grade 		= "N";
		int user_pay_month 	= 0;
		String user_term	 		= "now()";
		
		if(category_code.equals("1")){
			user_grade = "B";
		}else if(category_code.equals("2")){
			user_grade = "C";
		}else if(category_code.equals("3")){
			user_grade = "D";
		}
		
		user_pay_month 	= product_month;
		user_term				= "DATE_ADD(now(), INTERVAL "+user_pay_month+" month)	";
		
		/*
		if(product_num == 1){
			user_grade 			= "B";
			user_pay_month 	= 12;
			user_term				= "DATE_ADD(now(), INTERVAL "+user_pay_month+" month)	";
		}else if(product_num == 2){
			user_grade 			= "B";
			user_pay_month 	= 6;
			user_term				= "DATE_ADD(now(), INTERVAL "+user_pay_month+" month)	";
		}else if(product_num == 3){
			user_grade 			= "B";
			user_pay_month 	= 3;
			user_term				= "DATE_ADD(now(), INTERVAL "+user_pay_month+" month)	";
		}else if(product_num == 4){
			user_grade 			= "C";
			user_pay_month 	= 12;
			user_term				= "DATE_ADD(now(), INTERVAL "+user_pay_month+" month)	";
		}else if(product_num == 5){
			user_grade 			= "C";
			user_pay_month 	= 6;
			user_term				= "DATE_ADD(now(), INTERVAL "+user_pay_month+" month)	";
		}else if(product_num == 6){
			user_grade 			= "C";
			user_pay_month 	= 3;
			user_term				= "DATE_ADD(now(), INTERVAL "+user_pay_month+" month)	";
		}else if(product_num == 7){
			user_grade 			= "D";
			user_pay_month 	= 12;
			user_term				= "DATE_ADD(now(), INTERVAL "+user_pay_month+" month)	";
		}else if(product_num == 8){
			user_grade 			= "D";
			user_pay_month 	= 6;
			user_term				= "DATE_ADD(now(), INTERVAL "+user_pay_month+" month)	";
		}else if(product_num == 9){
			user_grade 			= "D";
			user_pay_month 	= 3;
			user_term				= "DATE_ADD(now(), INTERVAL "+user_pay_month+" month)	";
		}
		*/
		
		sm.write("UPDATE tbl_user ");
		sm.set("user_grade",user_grade);
		sm.set("user_pay_month",user_pay_month);
		sm.write(", user_term = "+user_term);
		sm.where("user_email",ss_user_email);
		
		//System.out.println("고객사 구독 신청 완료 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	/**
	 *  고객사 구독 환불
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void subscribeMinus(String ss_user_email) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		String user_grade 		= "N";
		int user_pay_month 	= 0;
		String user_term	 		= "now()";
		
		sm.write("UPDATE tbl_user ");
		sm.set("user_grade",user_grade);
		sm.set("user_pay_month",user_pay_month);
		sm.write(", user_term = "+user_term);
		sm.where("user_email",ss_user_email);
		
		//System.out.println("고객사 구독 환불 완료 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	/**
	 *  고객사 파일 다운로드 횟수 -1
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void fileDownCheck(String pcs_num) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE tbl_purchase ");
		sm.write("set down_remaining = down_remaining -1");
		sm.where("pcs_num",pcs_num);
		
		//System.out.println("고객사 파일 다운로드 횟수 -1 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	
	/**
	 *  제품 파일 다운로드 횟수 +1
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void fileDownCount(String product_num) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE tbl_product ");
		sm.write("set product_pcs_cnt = product_pcs_cnt +1");
		sm.where("product_num",product_num);
		
		//System.out.println("제품 파일 다운로드 횟수 +1 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
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
	 * 샘플 파일 다운을 위한 고객사 정보 등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void downinfoInsert(Properties props) throws BizException {
		SqlMaker sm 			= new SqlMaker();
		int di_num 		= getDinum()+1;
		
		sm.write("INSERT INTO tbl_downinfo ");
		sm.write("(di_num, di_fullname, di_email, di_tel, di_job, di_onzt, country_code, di_industry, di_content) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = di_num	+",'";
		strSql += props.getProperty("di_fullname", "")	+"','";
		strSql += props.getProperty("di_email", "")	+"','";
		strSql += props.getProperty("di_tel", "")	+"','";
		strSql += props.getProperty("di_job", "")	+"','";
		strSql += props.getProperty("di_onzt", "")	+"','";
		strSql += props.getProperty("country_code", "")	+"','";
		strSql += props.getProperty("di_industry", "")	+"','";
		strSql += props.getProperty("di_content", "")	+"'";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("샘플 파일 다운을 위한 고객사 정보 등록 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}
	
	
	/**
	 * 문의 게시물 멕스ID값 
	 * @return
	 */
	private int getDinum(){
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT MAX(di_num) AS max_seq FROM tbl_downinfo ");

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
	 * 임시 구독 신청
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void tempSubscription(Properties props, String ss_user_email) throws BizException {
		SqlMaker sm 	= new SqlMaker();
		int sst_num		= getSubscriptionnum()+1;
		
		sm.write("INSERT INTO tbl_subscription ");
		sm.write("(sst_num, user_email, sst_title, sst_content, sst_reg_date, product_num, sst_flag) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = sst_num +",'";
		strSql += ss_user_email	+"','";
		strSql += props.getProperty("sst_title", "")	+"','";
		strSql += props.getProperty("sst_content", "")	+"',";
		strSql += "now()"	+",";
		strSql += "null,'0'";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("임시 구독 신청 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}
	
	
	/**
	 * 구독신청
	 * @return
	 */
	private int getSubscriptionnum(){
		SqlMaker sm 		= new SqlMaker();
		
		sm.write(" SELECT MAX(sst_num) AS max_seq FROM tbl_subscription ");

		//System.out.println("구독신청 SQL=" + sm.getSql());
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
	 * 구독 신청 여부 확인
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public String subscriedCheck(String ss_user_email) throws BizException{
		SqlMaker sm 	= new SqlMaker();
		String result 		= "false";
		sm.write("	SELECT sst_num FROM tbl_subscription");
		sm.write("	WHERE user_email= '"+ss_user_email+"'");
		sm.write("	AND sst_flag NOT IN ('2','3')");
		
		//System.out.println(" 구독 신청 여부 확인 SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			if(rec.get("sst_num", "").equals("")){
				result 	= "true";
			}else{
				result 	= "false";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "false";
		}
		return result;
	}
	
	
	/**
	 * 구독 신청 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getSubscriptionInfo(String ss_user_email) throws BizException{
		SqlMaker sm 	= new SqlMaker();

		sm.write("	SELECT A.*, B.product_title, B.product_price, B.product_content, B.product_reg_date, B.category_code, ");
		sm.write("	B.subdivision_code, B.product_type, B.product_pcs_cnt, B.product_month, C.cart_num, C.cart_reg_date FROM ( ");
		sm.write("		SELECT * FROM tbl_subscription");
		sm.write("		WHERE user_email= '"+ss_user_email+"'");
		sm.write("		AND sst_flag NOT IN ('2','3')");
		sm.write(" ) A LEFT OUTER JOIN tbl_product B	");
		sm.write("	ON A.product_num = B.product_num");
		sm.write("	LEFT OUTER JOIN tbl_cart C");
		sm.write("	ON A.product_num = C.product_num");
		sm.write("	AND A.user_email = C.user_email");
		
		//System.out.println("구독 신청 정보 가져오기 SQL=" + sm.getSql());
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
	 *  구독 신청 취소
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void cancelSubscription(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE tbl_subscription ");
		sm.write("set sst_flag = '3'");
		sm.where("sst_num",props.getProperty("sst_num",""));
		
		//System.out.println("구독 신청 취소 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	/**
	 *  구독 신청 결제 완료
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void paySubscription(String product_num, String ss_user_email) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE tbl_subscription ");
		sm.write("set sst_flag = '2'");
		sm.where("product_num",product_num);
		sm.where("user_email",ss_user_email);
		
		//System.out.println("구독 신청 결제 완료 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	/**
	 * 답변 등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void replyInsert(Properties props, String ss_user_email) throws BizException {
		SqlMaker sm 			= new SqlMaker();
		int reply_num			= getReplynum()+1;
		int parents_num 		= 0;
		String parents_type		= props.getProperty("parents_type", "");
		String reply_content 	= props.getProperty("reply_content", "");
		
		reply_content			= StrUtil.insertReplace(reply_content);
		
		if(parents_type.equals("A")){
			parents_num = Integer.parseInt(props.getProperty("sst_num", "0"));
		}else{
			parents_num = Integer.parseInt(props.getProperty("order_num", "0"));
		}
		
		sm.write("INSERT INTO tbl_reply ");
		sm.write("(reply_num, parents_num, parents_type, reply_content, reply_writer, reply_step, reply_reg_date) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = reply_num 			+",";
		strSql += parents_num		+",'";
		strSql += parents_type		+"','";
		strSql += reply_content	+"','";
		strSql += ss_user_email	+"',";
		strSql += "'A',";
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
		sm.write("set sst_flag = '4'");
		sm.where("sst_num",props.getProperty("sst_num",""));
		
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
		sm.write("set order_flag = '4'");
		sm.where("order_num",props.getProperty("order_num",""));
		
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
	 * 답변 목록
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
	 * 고객사 주문 목록
	 * 조회할때 반드시 count() method 를 호출 하여 데이터의 숫자를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getOrderList(Properties props, String ss_user_email) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();

		String sortBy 				= props.getProperty("sortBy", "1");
		int pagesize 					= Integer.parseInt(props.getProperty("pagesize", "5"));
		
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(pagesize);
		int totalCnt = orderCount(props, ss_user_email);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		
		sm.write(" SELECT * FROM tbl_order");
		sm.write(" WHERE user_email = '"+ss_user_email+"'");
		sm.write(" AND order_flag != '3'");
		
		if(sortBy.equals("1")){
			sm.write(" ORDER BY order_reg_date desc");
		}else if(sortBy.equals("2")){
			sm.write(" ORDER BY order_reg_date asc");
		}else if(sortBy.equals("3")){
			sm.write(" ORDER BY order_title asc");
		}else if(sortBy.equals("4")){
			sm.write(" ORDER BY order_title desc");
		}
		
		sm.write(" limit "+first+", "+list.itemRange);
		
		//System.out.println("고객사 주문 목록 SQL=" + sm.getSql());

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
	 * 고객사 주문 보고서 Count
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int orderCount(Properties props, String ss_user_email) throws BizException {
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM tbl_order");
		sql.write("WHERE user_email = '"+ss_user_email+"'");
		sql.write("AND order_flag != '3'");
		
		//System.out.println("고객사 주문 보고서 count SQL=" + sql.getSql());
		
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
	 * 주문 내역 가져오기
	 * 
	 * @param props
	 * @return String
	 * @throws BizException
	 */
	public Record getOrder(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("	SELECT A.*, B.product_title, B.product_price, B.product_content, B.product_reg_date, B.category_code,");
		sm.write("	B.subdivision_code, B.product_type, B.product_pcs_cnt, B.product_month, C.cart_num, C.cart_reg_date FROM (");
		sm.write("		SELECT * FROM tbl_order");
		sm.write("		WHERE order_num = '"+props.getProperty("order_num", "")+"'");
		sm.write("	) A LEFT OUTER JOIN tbl_product B");
		sm.write("	ON A.product_num = B.product_num");
		sm.write("	LEFT OUTER JOIN tbl_cart C");
		sm.write("	ON A.product_num = C.product_num");
		sm.write("	AND A.user_email = C.user_email");
		
		//System.out.println("주문 내역 가져오기 SQL=" + sm.getSql());
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
	 * 보고서 주문 신청
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void orderRegister(Properties props, String ss_user_email) throws BizException {
		SqlMaker sm 	= new SqlMaker();
		int order_num		= getOrdernum()+1;
		
		sm.write("INSERT INTO tbl_order ");
		sm.write("(order_num, user_email, order_title, order_content, order_reg_date, product_num, order_flag) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = order_num +",'";
		strSql += ss_user_email	+"','";
		strSql += props.getProperty("order_title", "")	+"','";
		strSql += props.getProperty("order_content", "")	+"',";
		strSql += "now()"	+",";
		strSql += "null,'0'";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("보고서 주문 신청 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}
	
	
	/**
	 * 주문 신청 번호
	 * @return
	 */
	private int getOrdernum(){
		SqlMaker sm 		= new SqlMaker();
		
		sm.write(" SELECT MAX(order_num) AS max_seq FROM tbl_order ");

		//System.out.println("주문 신청 번호 SQL=" + sm.getSql());
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
	 *  주문 신청 취소
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void cancelOrder(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE tbl_order ");
		sm.write("set order_flag = '3'");
		sm.where("order_num",props.getProperty("order_num",""));
		
		//System.out.println("주문 신청 취소 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	/**
	 *  주문 신청 결제 완료
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void payOrder(String product_num, String ss_user_email) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE tbl_order");
		sm.write("set order_flag = '2'");
		sm.where("product_num",product_num);
		sm.where("user_email",ss_user_email);
		
		//System.out.println("주문 신청 결제 완료 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
	
	
	/**
	 * 방문 정보 가져오기
	 * 
	 * @param props
	 * @return String
	 * @throws BizException
	 */
	public String getVisit(String ss_user_ip) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		String visit_ip = "";
		
		sm.write("SELECT visit_ip FROM tbl_visit");
		sm.write("WHERE visit_ip = '"+ss_user_ip+"'");
		sm.write("AND visit_year = '"+DateUtil.getYYYY()+"'");
		sm.write("AND visit_month = '"+DateUtil.getMM()+"'");
		sm.write("AND visit_day = '"+DateUtil.getDD()+"'");
		
		//System.out.println("방문 정보 가져오기 SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			visit_ip = rec.get("visit_ip", "");
			return visit_ip;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return visit_ip;
		}
	}
	
	
	/**
	 * 방문 정보 등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void visitCnt(String ss_user_ip) throws BizException {
		SqlMaker sm 	= new SqlMaker();
		int visit_num		= getVisitum()+1;
		
		sm.write("INSERT INTO tbl_visit ");
		sm.write("(visit_num, visit_ip, visit_year, visit_month, visit_day) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = visit_num 				+",'";
		strSql += ss_user_ip			+"',";
		strSql += DateUtil.getYYYY()	+",";
		strSql += DateUtil.getMM()	+",";
		strSql += DateUtil.getDD();
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("방문 정보 등록 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}
	
	/**
	 * 방문 정보 번호
	 * @return
	 */
	private int getVisitum(){
		SqlMaker sm 		= new SqlMaker();
		
		sm.write(" SELECT MAX(visit_num) AS max_seq FROM tbl_visit ");

		//System.out.println("방문 정보 번호 SQL=" + sm.getSql());
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
	
	
	
////////////////////////////////////////////////////////////////////////////////
/**
* PUSH 정보 등록
* 
* @param props
* @throws BizException
* 
*/
public String setPushRegister(Properties props) throws BizException {
	SqlMaker sm = new SqlMaker();
	String result = props.getProperty("result");
	String com_id = props.getProperty("p_com_id");
	String gcm_id = props.getProperty("gcm_id");
	String device_id = props.getProperty("device_id");
	String device_type = props.getProperty("device_type");
	String hp = "";
	String user_id = "";
	String type = "0";
	
	//String sResult = result;
	//System.out.println(result);
	
	if("false".equals(result)) {
		Record rec = getUserLogin(props);
		//rec.isEmpty()
		if (rec != null && !rec.isEmpty()) {
			com_id = rec.get("com_id","");
			user_id = rec.get("user_id","");
			type = "1";
			result = "true";
		}
	}

	if(gcm_id == null || gcm_id.equals(""))
		return result;
	
	String[] dv = device_id.split("@");
	System.out.println("d:" + device_id);
	
	if( isGcmReg(com_id, user_id, type) == 0) {
		sm.write("INSERT INTO t_push_register ");
		sm.into("com_id",com_id);
		sm.into("user_id",user_id);
		sm.into("type",type);
		sm.into("device_type",device_type);
		sm.into("device_id",dv[0].trim());
		sm.into("hp",dv[1].trim());
		sm.into("gcm_id",gcm_id);
		sm.intoNumber("reg_date","now()");
		sm.intoNumber("upt_date","now()");
		
		System.out.println("setPushRegister 입력SQL=" + sm.getSql());
	
	} else {
		sm.write("UPDATE t_push_register ");
		sm.set("device_id",dv[0].trim());
		sm.set("hp",dv[1].trim());
		sm.set("gcm_id",gcm_id);
		sm.setNumber("upt_date","now()");
		sm.where("com_id",com_id);
		sm.where("user_id",user_id);
		sm.where("type",type);
		System.out.println("setPushRegister 수정SQL=" + sm.getSql());
	
	}
	
	try {
		DaoCom.executeUpdate(conn,  sm.getSql());
	
	} catch (Exception e) {
	// TODO Auto-generated catch block
		e.printStackTrace();
		throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
	}
	
	return result;

//else System.out.println("등록된 사용자ID !!");
}	

private Record getUserLogin(Properties props) throws BizException {
	SqlMaker sm = new SqlMaker();
	
	// 비밀번호 암호화 - 나중에 변경
	String user_pw = "";
	SHA256 s = new SHA256( props.getProperty("p_com_pw", "").getBytes() );
	BASE64Encoder Base64Encoder = new BASE64Encoder();	
	user_pw = Base64Encoder.encode(s.GetHashCode());
	//user_pw = props.getProperty("p_com_pw");
	
	
	sm.write("SELECT * FROM t_user ");
	sm.where("user_id",props.getProperty("p_com_id"));
	sm.where("user_pw",user_pw);
	
	System.out.println("회원정보SQL=" + sm.getSql());
	try {
		Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
		return rec;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new BizException("0000", "로그인 고객사 조회 오류");
	}		
}

/**
* GCM_ID 확인.
* 
* @param props
* @throws BizException
*/	
private int isGcmReg(String com_id, String user_id, String type) {
	SqlMaker sm = new SqlMaker();
	
	sm.write(" SELECT count(seq_no) as cnt FROM t_push_register ");
	sm.write(" WHERE ");
	sm.write(" com_id = '" + com_id + "'");
	sm.write(" AND user_id = '" + user_id + "'");
	sm.write(" AND type = '" + type + "'");
	
	//System.out.println("SQL=" + sm.getSql());
	try {
		Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
		Object obj = rec.get("cnt");
		return ParamUtil.getIntParameter(obj, 0);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return 0;
}


/**
* PUSH 목록
* 
* @param props
* @return
* @throws BizException
*/
public ListData getPushList(Properties props) throws BizException {
	SqlMaker sm = new SqlMaker();
	ListData list = new ListData();
	String com_id = props.getProperty("com_id", "");
	String com_type = props.getProperty("com_type","0");
	
	String user_id = props.getProperty("user_id","");
	String type = props.getProperty("type");
	
	
	if(com_type.equals("0")) {
		sm.write("	SELECT * FROM t_push_register ");
		sm.write("	WHERE user_id = ''");
		//sm.write("	AND user_id = '" + user_id + "'");
		sm.write("	AND type = '" + com_type + "'");		
		sm.write("	AND device_type = '0'");
	} else {
		sm.write("	SELECT * FROM t_push_register ");
		sm.write("	WHERE com_id = '" + com_id + "'");
		//sm.write("	AND user_id = '" + user_id + "'");
		sm.write("	AND type = '" + com_type + "'");
		sm.write("	AND device_type = '0'");
	}
		
	

	
	//System.out.println("PUSH 목록 SQL=" + sm.getSql());
	
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
* PUSH 목록
* 
* @param props
* @return
* @throws BizException
*/
public ListData getPushIOSList(Properties props) throws BizException {
	SqlMaker sm = new SqlMaker();
	ListData list = new ListData();
	String com_id = props.getProperty("com_id", "");
	String com_type = props.getProperty("com_type","0");
	
	String user_id = props.getProperty("user_id","");
	String type = props.getProperty("type");
	
	
	if(com_type.equals("0")) {
		sm.write("	SELECT * FROM t_push_register ");
		sm.write("	WHERE user_id = ''");
		//sm.write("	AND user_id = '" + user_id + "'");
		sm.write("	AND type = '" + com_type + "'");		
		sm.write("	AND device_type = '1'");
	} else {
		sm.write("	SELECT * FROM t_push_register ");
		sm.write("	WHERE com_id = '" + com_id + "'");
		//sm.write("	AND user_id = '" + user_id + "'");
		sm.write("	AND type = '" + com_type + "'");
		sm.write("	AND device_type = '1'");
	}
		
	

	
	//System.out.println("PUSH 목록 SQL=" + sm.getSql());
	
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
