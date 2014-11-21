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
 * @description : 주문 관련 로직 처리하기위한 class
 * $Id: DaoUser.java,v 1.13 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 12. 20	정수현		최초작성
 * 
 *****************************************************************/


public class DaoOrder {
	private static Logger logger = Logger.getLogger(DaoOrder.class.getName());
	private Connection conn = null;
	SqlMaker sql = null;
	
	public DaoOrder(Connection conn){
		this.conn = conn;
		sql = new SqlMaker();
	}
	
	
	/**
	 * 주문 정보 조회
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
		
		
		sm.write("SELECT * FROM t_order ");
		sm.where("com_id",props.getProperty("p_com_id"));
		sm.where("com_pw",user_pw);
		
		//System.out.println("회원정보SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			//logger.debug("로그인 주문 정보:" + rec.toString());

			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000", "로그인 주문 조회 오류");
		}
	}

	
	/**
	 * 주문 목록조회를 위한  row count 를 계산한다.
	 * 
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public int getUserOrderCount(Properties props) throws BizException {
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM t_user_order");
		sql.write(" WHERE com_id='"+ props.getProperty("p_com_id") +"' ");
		sql.write(" AND product_id='"+ props.getProperty("p_product_id") +"' ");
		/*
		if(!search_keyword.equals("")){
			sql.write(" WHERE (");
			sql.write(" order_title like '%"+search_keyword+"%' OR order_content like '%"+search_keyword+"%'");
			sql.write(" )");
		}else{
			sql.write(" WHERE order_type = '"+menu_type+"'");
		}
		sql.write(" AND order_type in ('A','B','C')");
		
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
	 * 주문 목록을  Select 한다.
	 * 조회할때 반드시 getOrderCount() method 를 호출 하여 데이터의 총 rows를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getUserOrderList(Properties props) throws BizException {
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
		
		int totalCnt = getUserOrderCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		sm.write(" SELECT * FROM ");
		sm.write(" ( ");
		sm.write(" SELECT A.*, B.option_text FROM t_user_order A, t_option B ");
		sm.write(" WHERE A.com_id = '"+ props.getProperty("p_com_id") +"' ");
		sm.write(" AND A.product_id = '"+ props.getProperty("p_product_id") +"' ");
		sm.write(" AND A.option_id = B.option_id ");
		sm.write(" ) AA LEFT JOIN t_user B ON AA.user_id=B.user_id "); 
		
		
		
		/*
		if(!search_keyword.equals("")){
			sm.write(" WHERE (");
			sm.write(" order_title like '%"+search_keyword+"%' OR order_content like '%"+search_keyword+"%'");
			sm.write(" )");
		}else{
			sm.write(" WHERE order_type = '"+menu_type+"'");
		}
		sm.write(" AND order_type in ('A','B','C')");
		
		if(!category_code.equals("")){
			sm.write(" AND category_code = '"+category_code+"'");
		}
		if(!subdivision_code.equals("")){
			sm.write(" AND subdivision_code = '"+subdivision_code+"'");
		}
		
		if(sortBy.equals("1")){
			sm.write(" ORDER BY order_reg_date desc");
		}else if(sortBy.equals("2")){
			sm.write(" ORDER BY order_reg_date asc");
		}else if(sortBy.equals("3")){
			sm.write(" ORDER BY order_title asc");
		}else if(sortBy.equals("4")){
			sm.write(" ORDER BY order_title desc");
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
	 * 주문 목록을  Select 한다.
	 * 조회할때 반드시 getOrderCount() method 를 호출 하여 데이터의 총 rows를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getUserOrderBySizeList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
		/*
		String menu_type 			= props.getProperty("menu_type", "A");
		String category_code 		= props.getProperty("category_code", "");
		String subdivision_code 	= props.getProperty("subdivision_code", "");
		String sortBy 				= props.getProperty("sortBy", "1");
		
		String search_keyword 	= props.getProperty("search_keyword", "");
		*/
		/*
		int pagesize 				= Integer.parseInt(props.getProperty("pagesize", "5"));
		
		list.setCurrentPage(ParamUtil.getIntParameter(props.getProperty("cpage"), 1));
		list.setItemRange(pagesize);
		
		int totalCnt = getUserOrderCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		*/
		
		sm.write(" SELECT A.product_id, B.product_title, C.option_text, sum(amount) as amount_num ");
		sm.write(" FROM t_user_order A, t_product B, t_option C ");
		sm.write(" WHERE A.com_id='"+ props.getProperty("p_com_id") +"' ");
		sm.write(" AND A.product_id='"+ props.getProperty("p_product_id") +"' ");
		sm.write(" AND B.product_id='"+ props.getProperty("p_product_id") +"' ");
		sm.write(" AND A.option_id=C.option_id ");
		
		sm.write(" GROUP BY A.option_id ");
		
		/*
		if(!search_keyword.equals("")){
			sm.write(" WHERE (");
			sm.write(" order_title like '%"+search_keyword+"%' OR order_content like '%"+search_keyword+"%'");
			sm.write(" )");
		}else{
			sm.write(" WHERE order_type = '"+menu_type+"'");
		}
		sm.write(" AND order_type in ('A','B','C')");
		
		if(!category_code.equals("")){
			sm.write(" AND category_code = '"+category_code+"'");
		}
		if(!subdivision_code.equals("")){
			sm.write(" AND subdivision_code = '"+subdivision_code+"'");
		}
		
		if(sortBy.equals("1")){
			sm.write(" ORDER BY order_reg_date desc");
		}else if(sortBy.equals("2")){
			sm.write(" ORDER BY order_reg_date asc");
		}else if(sortBy.equals("3")){
			sm.write(" ORDER BY order_title asc");
		}else if(sortBy.equals("4")){
			sm.write(" ORDER BY order_title desc");
		}
		*/
		
		//sm.write(" limit "+first+", "+list.itemRange);
		
		System.out.println("보고서 목록 SQL=" + sm.getSql());

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
	 * 주문 목록을  Select 한다.
	 * 조회할때 반드시 getOrderCount() method 를 호출 하여 데이터의 총 rows를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getCompanyOrderBySizeList(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		
	
		sm.write(" SELECT A.product_id, B.product_name, A.order_size, sum(amount) as amount_num ");
		sm.write(" FROM t_company_order A, t_product B ");
		sm.write(" WHERE A.product_id='"+ props.getProperty("p_product_id") +"' ");
		sm.write(" AND B.product_id='"+ props.getProperty("p_product_id") +"' ");
		
		sm.write(" GROUP BY CAST(A.order_size as unsigned) ");
		

		
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
	 * 주문 상세 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getOrderItem(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String order_id = props.getProperty("p_order_id");
		
		sm.write("SELECT * FROM t_user_order ");
		sm.write("WHERE order_id='" + order_id + "' ");
		
		/*
		sm.write("SELECT A.*, B.file_name, B.file_num, C.category_name, D.subdivision_name FROM (");
		sm.write("		SELECT * FROM tbl_order");
		sm.write("		WHERE order_num= '"+order_num+"'");
		sm.write(") A LEFT OUTER JOIN tbl_file B ");
		sm.write("ON A.order_num = B.order_num");
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
	 * 주문 주문목록조회를 위한  row count 를 계산한다.
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
		sql.write(" AND product_id='"+ props.getProperty("p_product_id") +"' ");
		/*
		if(!search_keyword.equals("")){
			sql.write(" WHERE (");
			sql.write(" order_title like '%"+search_keyword+"%' OR order_content like '%"+search_keyword+"%'");
			sql.write(" )");
		}else{
			sql.write(" WHERE order_type = '"+menu_type+"'");
		}
		sql.write(" AND order_type in ('A','B','C')");
		
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
	 * 주문 주문 목록을  Select 한다.
	 * 조회할때 반드시 getOrderOrderCount() method 를 호출 하여 데이터의 총 rows를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getCompanyOrderList(Properties props) throws BizException {
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
		
		int totalCnt = getCompanyOrderCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		
		sm.write(" SELECT A FROM t_company_order ");
		sm.write(" WHERE com_id='"+ props.getProperty("p_com_id") +"' ");
		sm.write(" AND product_id='"+ props.getProperty("p_product_id") +"' ");
		
		sm.write(" ORDER BY order_size ");
		/*
		if(!search_keyword.equals("")){
			sm.write(" WHERE (");
			sm.write(" order_title like '%"+search_keyword+"%' OR order_content like '%"+search_keyword+"%'");
			sm.write(" )");
		}else{
			sm.write(" WHERE order_type = '"+menu_type+"'");
		}
		sm.write(" AND order_type in ('A','B','C')");
		
		if(!category_code.equals("")){
			sm.write(" AND category_code = '"+category_code+"'");
		}
		if(!subdivision_code.equals("")){
			sm.write(" AND subdivision_code = '"+subdivision_code+"'");
		}
		
		if(sortBy.equals("1")){
			sm.write(" ORDER BY order_reg_date desc");
		}else if(sortBy.equals("2")){
			sm.write(" ORDER BY order_reg_date asc");
		}else if(sortBy.equals("3")){
			sm.write(" ORDER BY order_title asc");
		}else if(sortBy.equals("4")){
			sm.write(" ORDER BY order_title desc");
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
	 * 주문 신규등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void addUserOrder(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		// 비밀번호 암호화
		//String user_pw = "";
		//SHA256 s = new SHA256( props.getProperty("p_com_pw", "").getBytes() );
		//BASE64Encoder Base64Encoder = new BASE64Encoder();	
		//user_pw = Base64Encoder.encode(s.GetHashCode());
		//user_pw = props.getProperty("p_com_pw", "");
		
		sm.write("INSERT INTO t_user_order ");
		sm.write("(product_id, com_id, order_student_name, order_parent_name, order_size, amount, order_datetime) ");
		sm.write(" VALUES (");
		String strSql;
		
		strSql = "'";
		strSql += props.getProperty("p_product_id", "")	+"','";
		strSql += props.getProperty("p_com_id", "")	+"','";
		strSql += props.getProperty("p_order_student_name", "")	+"','";
		strSql += props.getProperty("p_order_parent_name", "")	+"','";
		strSql += props.getProperty("p_order_size", "")	+"','";
		strSql += props.getProperty("p_amount", "")	+"',";
		strSql += "now()";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("주문 등록 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}	
	
	
	/**
	 *  주문 정보 변경
	 * 
	 * @param props
	 * @throws BizException
	 */
	public void updateUserOrder(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		//String user_pw 			= "";
	    // 비밀번호 암호화
		//SHA256 s = new SHA256( props.getProperty("user_password").getBytes() );
		//BASE64Encoder Base64Encoder = new BASE64Encoder();	
		//user_pw = Base64Encoder.encode(s.GetHashCode());
		//user_pw = props.getProperty("p_com_pw", "");
	 		
		sm.write("UPDATE t_order ");
		sm.set("order_student_name",props.getProperty("p_order_student_name"));
		sm.set("order_parent_name",props.getProperty("p_order_parent_name"));
		sm.set("order_size",props.getProperty("p_order_size"));
		sm.set("amount",props.getProperty("amount"));
		sm.set("order_datetime","now()");		
		
		sm.where("order_id",props.getProperty("p_order_id"));
		
		//System.out.println("주문 정보 변경 SQL=" + sm.getSql());

		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","update 오류");
		}
	}
		
	
	/**
	 * 주문 삭제
	 * 
	 * @param props
	 * @return void
	 * @throws BizException
	 */
	public void deleteOrder(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("DELETE FROM t_user_order WHERE order_id = '" + props.getProperty("p_order_id") + "' ");
		
		//System.out.println("주문 비번확인 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 주문 목록을  Select 한다.
	 * 조회할때 반드시 getOrderCount() method 를 호출 하여 데이터의 총 rows를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public void setCompanyOrder(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		String sProduct_id = props.getProperty("p_product_id", "");
		String sCom_id = props.getProperty("p_com_id", "");
	
		sm.write("INSERT INTO t_company_order ");
		sm.write("(product_id, com_id, order_size, amount, result_datetime, option_id) ");
		sm.write(" SELECT '"+sProduct_id+"','"+sCom_id+"',order_size,sum(amount),now(), option_id");
		sm.write(" FROM t_user_order ");
		sm.write(" WHERE product_id='"+sProduct_id+"' ");
		sm.write(" AND com_id='"+sCom_id+"' ");
		sm.write(" GROUP BY option_id ");
		
		//System.out.println("주문 등록 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}		
	
	
	
	
	/**
	 * 업체주문정보 temp 테이블로 복사
	 * 
	 * @param props
	 * @return void
	 * @throws BizException
	 */
	public void moveCompanyOrder(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("INSERT INTO t_company_order_temp  ");
		sm.write(" SELECT * FROM t_company_order ");
		sm.write(" WHERE product_id = '" + props.getProperty("p_product_id") + "' ");
		sm.write(" AND com_id = '" + props.getProperty("p_com_id") + "' ");
		
		//System.out.println("주문 비번확인 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	/**
	 * 업체주문 삭제
	 * 
	 * @param props
	 * @return void
	 * @throws BizException
	 */
	public void deleteCompanyOrder(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("DELETE FROM t_company_order ");
		sm.write(" WHERE product_id = '" + props.getProperty("p_product_id") + "' ");
		sm.write(" AND com_id = '" + props.getProperty("p_com_id") + "' ");
		
		//System.out.println("주문 비번확인 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
	
	
	/**
	 * 주문 삭제 - 상태 update로 데이터는 유지..
	 * 
	 * @param props
	 * @return void
	 * @throws BizException
	 */
	public void deleteStateOrder(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		
		sm.write("UPDATE t_user_order SET stats = 'n' WHERE order_id = '" + props.getProperty("p_order_id") + "' ");
		
		//System.out.println("주문 비번확인 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
	
	
	
    /*
     * 일단 여기까지 2014-04-07 까지 기본 정의  
     */	
	
	
	
	
	
	
	
	
	
	

	
}
