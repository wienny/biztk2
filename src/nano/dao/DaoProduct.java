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
 * @description : 상품 관련 로직 처리하기위한 class
 * $Id: DaoUser.java,v 1.13 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 12. 20	정수현		최초작성
 * 
 *****************************************************************/


public class DaoProduct {
	private static Logger logger = Logger.getLogger(DaoProduct.class.getName());
	private Connection conn = null;
	SqlMaker sql = null;
	
	public DaoProduct(Connection conn){
		this.conn = conn;
		sql = new SqlMaker();
	}
	
	
	/**
	 * 상품 정보 조회
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
		
		
		sm.write("SELECT * FROM t_product ");
		sm.where("com_id",props.getProperty("p_com_id"));
		sm.where("com_pw",user_pw);
		
		//System.out.println("회원정보SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			//logger.debug("로그인 상품 정보:" + rec.toString());

			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000", "로그인 상품 조회 오류");
		}
	}

	
	/**
	 * 상품 목록조회를 위한  row count 를 계산한다.
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int getTableCount(Properties props, String table, String sWhere) throws BizException {
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM " + table);
		sql.write(sWhere);
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
	 * 상품 목록을  Select 한다.
	 * 조회할때 반드시 getProductCount() method 를 호출 하여 데이터의 총 rows를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getPackageList(Properties props) throws BizException {
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
		
		int totalCnt = getTableCount(props, "t_package", "");//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		
		sm.write(" SELECT * FROM t_package ");
		//sm.write(" WHERE com_type='"+ props.getProperty("p_com_type") +"' ");
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
	 * 상품 목록을  Select 한다.
	 * 조회할때 반드시 getProductCount() method 를 호출 하여 데이터의 총 rows를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getProductList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		String sWhere = "";
		String sWhere_this = "";
		String sCom_type = "";
		String sCom_id = "";
		
		sCom_type = props.getProperty("p_com_type", "");
		sCom_id = props.getProperty("p_com_id", "");
		if(sCom_type.equals("2")) {
			sWhere = " WHERE com_id='"+sCom_id+"' ";
			sWhere_this = " WHERE A.category_id = B.category_id AND A.com_id='"+sCom_id+"' ";
		} else {
			sWhere_this = " WHERE A.category_id = B.category_id ";
		}		
		
		
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
		
		int totalCnt = getTableCount(props, "t_product", sWhere);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		sm.write(" SELECT A.*, B.category_title FROM t_product A, t_category B ");
		sm.write(sWhere_this);	
		sm.write(" ORDER BY reg_datetime DESC ");
		
		//sm.write(" SELECT * FROM t_product ");
		//sm.write(sWhere);
		//sm.write(" WHERE com_type='"+ props.getProperty("p_com_type") +"' ");
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
	 * 상품 상세 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getProductItem(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String product_id = props.getProperty("p_product_id");
		
		sm.write("SELECT A.*, B.category_title FROM t_product A, t_category B ");
		sm.write("WHERE A.product_id='" + product_id + "' ");
		sm.write("AND A.category_id=B.category_id ");
		
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
	 * 상품 목록을  Select 한다.
	 * 조회할때 반드시 getProductCount() method 를 호출 하여 데이터의 총 rows를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getProComList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		String sWhere = "";
		String sWhere_this = "";
		String sCom_type = "";
		String sCom_id = "";
		
		sCom_type = props.getProperty("p_com_type", "");
		sCom_id = props.getProperty("p_com_id", "");
		if(sCom_type.equals("1")) {
			sWhere = " WHERE state='1' ";		
			sWhere_this = " WHERE A.category_id = B.category_id AND A.state='1' ";
		} else {
			sWhere_this = " WHERE A.category_id = B.category_id ";
		}
		
		
		
		
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
		
		int totalCnt = getTableCount(props, "t_product", sWhere);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		
		sm.write(" SELECT A.*, B.category_title FROM t_product A, t_category B ");
		sm.write(sWhere_this);
		//sm.write(" WHERE com_type='"+ props.getProperty("p_com_type") +"' ");
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
	 * 상품 주문목록조회를 위한  row count 를 계산한다.
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int getProductOrderCount(Properties props) throws BizException {
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM t_company_order");
		sql.write(" WHERE product_id='"+ props.getProperty("p_product_id") +"' ");
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
	 * 상품 주문 목록을  Select 한다.
	 * 조회할때 반드시 getProductOrderCount() method 를 호출 하여 데이터의 총 rows를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getProductOrderList(Properties props) throws BizException {
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
		
		//int totalCnt = getProductOrderCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		//list.setTotalPage(totalCnt);
		//int first = list.itemRange * (list.getCurrentPage()-1);
		
		
		sm.write(" SELECT A.*, B.com_name, C.option_text FROM t_company_order A, t_company B, t_option C ");
		sm.write(" WHERE A.product_id='"+ props.getProperty("p_product_id") +"' ");
		sm.write(" AND A.com_id = B.com_id ");
		sm.write(" AND A.option_id = C.option_id ");
		
		sm.write(" ORDER BY A.result_datetime ");
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
	 * 상품 신규등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void addProduct(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		// 비밀번호 암호화
		String user_pw = "";
		SHA256 s = new SHA256( props.getProperty("p_com_pw", "").getBytes() );
		BASE64Encoder Base64Encoder = new BASE64Encoder();	
		user_pw = Base64Encoder.encode(s.GetHashCode());
		//user_pw = props.getProperty("p_com_pw", "");
		
		sm.write("INSERT INTO t_product ");
		sm.write("(com_id, com_pw, com_email, com_name, reg_datetime, upt_datetime) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = "'";
		strSql += props.getProperty("p_com_id", "")	+"','";
		strSql += user_pw	+"','";
		strSql += props.getProperty("p_com_email", "")	+"','";
		strSql += props.getProperty("p_com_name", "")	+"',";
		strSql += "now(),";
		strSql += "now()";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("상품 등록 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}	
	
	
	/**
	 *  상품 정보 변경
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void updateProduct(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		String user_pw 			= "";
	    // 비밀번호 암호화
		//SHA256 s = new SHA256( props.getProperty("user_password").getBytes() );
		//BASE64Encoder Base64Encoder = new BASE64Encoder();	
		//user_pw = Base64Encoder.encode(s.GetHashCode());
		user_pw = props.getProperty("p_com_pw", "");
	 		
		sm.write("UPDATE t_product ");
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
		
		//System.out.println("상품 정보 변경 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
		
	
	/**
	 * 상품 삭제
	 * 
	 * @param props
	 * @return void
	 * @throws BizException
	 */
	public void deleteProduct(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("DELETE FROM t_product WHERE com_id = '" + props.getProperty("p_com_id") + "' ");
		
		//System.out.println("상품 비번확인 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 상품 삭제 - 상태 update로 데이터는 유지..
	 * 
	 * @param props
	 * @return void
	 * @throws BizException
	 */
	public void deleteStateProduct(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE t_product SET use_yn = 'N' ");
		
		//System.out.println("상품 비번확인 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
	/**
	 * 상품 비번확인
	 * 
	 * @param props
	 * @return String
	 * @throws BizException
	 */
	public boolean pwCheckProduct(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		String user_pw 			= "";
	    // 비밀번호 암호화
		//SHA256 s = new SHA256( props.getProperty("current_password","").getBytes() );
		//BASE64Encoder Base64Encoder = new BASE64Encoder();	
		//user_pw = Base64Encoder.encode(s.GetHashCode());
		user_pw = props.getProperty("p_com_pw", "");	 		
		
		sm.write("SELECT com_id FROM t_product");
		sm.write("WHERE com_id = '"+props.getProperty("p_com_id", "")+"'");
		sm.write("AND com_pw = '"+user_pw+"'");
		
		//System.out.println("상품 비번확인 SQL=" + sm.getSql());
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
	 * 상품 상세 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getProductSubInfo(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		String product_id = props.getProperty("p_product_id");
		
		sm.write("SELECT * FROM t_product_sub ");
		sm.write("WHERE product_id='" + product_id + "' ");
		//sm.write("AND A.category_id=B.category_id ");
		
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
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}		
	
	
	
	/**
	 * 상품 상세 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getProductImgInfo(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		String product_id = props.getProperty("p_product_id");
		
		sm.write("SELECT * FROM t_image ");
		sm.write("WHERE product_id='" + product_id + "' ");
		//sm.write("AND A.category_id=B.category_id ");
		
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
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}		
	
	/**
	 * 상품 상세 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getProductOptionInfo(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		String product_id = props.getProperty("p_product_id");
		
		sm.write("SELECT * FROM t_option ");
		sm.write("WHERE product_id='" + product_id + "' ");
		//sm.write("AND A.category_id=B.category_id ");
		
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
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}	

	
}
