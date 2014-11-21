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


public class DaoUser {
	private static Logger logger = Logger.getLogger(DaoUser.class.getName());
	private Connection conn = null;
	SqlMaker sql = null;
	
	public DaoUser(Connection conn){
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
		SHA256 s = new SHA256( props.getProperty("p_user_pw", "").getBytes() );
		BASE64Encoder Base64Encoder = new BASE64Encoder();	
		user_pw = Base64Encoder.encode(s.GetHashCode());
		//user_pw = props.getProperty("p_com_pw");
		
		
		sm.write("SELECT * FROM t_user ");
		sm.where("user_id",props.getProperty("p_user_id"));
		sm.where("user_pw",user_pw);
		
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
	public int getUserCount(Properties props) throws BizException {
		
		sql.reset();
		sql.write("SELECT count(*) as row_count FROM t_user");
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
	 * 고객사 목록을  Select 한다.
	 * 조회할때 반드시 getCompanyCount() method 를 호출 하여 데이터의 총 rows를 알아야 한다.
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getUserList(Properties props) throws BizException {
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
		
		int totalCnt = getUserCount(props);//게시물 총 카운터(몇개의 게시물인지 확인하는 쿼리)
		list.setTotalPage(totalCnt);
		int first = list.itemRange * (list.getCurrentPage()-1);
		
		
		sm.write(" SELECT * FROM t_user ");
		sm.write(" WHERE com_id='"+ props.getProperty("p_com_id") +"' ");
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
	public Record getUserItem(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String user_id = props.getProperty("p_user_id");
		
		sm.write("SELECT * FROM t_user ");
		sm.write("WHERE user_id='" + user_id + "' ");
		
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
	 *  t_user 중복 체크 아이디 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData checkUserID(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		ListData list = new ListData();
		
		sm.write(" SELECT user_id from t_user WHERE user_id='"+props.getProperty("p_user_id")+"'");
	
		//System.out.println("중복 체크 SQL=" + sm.getSql());
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
	 *  t_site 중복 체크 아이디 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record checkUserIDparams(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT user_id from t_user WHERE user_id='"+props.getProperty("p_user_id")+"'");
	
		//System.out.println("중복 체크 SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			//throw new BizException("0000","select 오류");
			
		}
	}		
	
	
	/**
	 * 사용자정보 신규등록
	 * 
	 * @param props
	 * @throws BizException
	 * 
	 */
	public void addUser(Properties props) throws BizException {
		SqlMaker sm = new SqlMaker();
		
		String strSql;

		// 비밀번호 암호화 - 나중에 변경
		String user_pw = "";
		SHA256 s = new SHA256( props.getProperty("p_user_pw", "").getBytes() );
		BASE64Encoder Base64Encoder = new BASE64Encoder();	
		user_pw = Base64Encoder.encode(s.GetHashCode()); 
		
	
		
		sm.write("INSERT INTO t_user ");
		sm.write("(com_id, user_id, user_pw, student_name, student_hp, parent_name, parent_hp) ");
		sm.write(" VALUES (");
		
		
		
		strSql = "'";
		strSql += props.getProperty("p_com_id", "")	+"','";
		strSql += props.getProperty("p_user_id", "")	+"','";
		strSql += user_pw	+"','";
		strSql += props.getProperty("p_student_name", "")	+"','";
		strSql += props.getProperty("p_student_hp", "")	+"','";
		strSql += props.getProperty("p_student_name", "")	+" 관원 부모님','";
		strSql += props.getProperty("p_parent_hp", "")	+"'  ";
		
		sm.write(strSql);
		sm.write(")");
		
		//System.out.println("사이트정보 등록 SQL=" + sm.getSql());
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","insert 오류:" + e.getMessage().replace("\n"," "));
		}
	}	
	
	
	/**
	 * 사이트정보 상세 정보 가져오기
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getUser(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		String user_id = props.getProperty("p_user_id");
		
		sm.write("SELECT * FROM t_user ");
		sm.write("WHERE user_id='" + user_id + "' ");
		
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
	

	
}
