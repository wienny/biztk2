package nano.biz;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nano.BizException;
import nano.ListData;
import nano.Record;
import nano.dao.*;
//import nano.dao.DaoAdmin;
//import nano.dao.DaoUser;
//import nano.dao.DaoUser;
//import nano.dao.DaoCom;
import nano.util.MailUtil;
import nano.util.ParamUtil;
import nano.util.StrUtil;

import org.apache.log4j.Logger;


import com.oreilly.servlet.MultipartRequest;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Parameter;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;



/*****************************************************************
 * @description : 사용자  관련 로직 처리하기위한 class
 * $Id: BizAdmin.java,v 1.2 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 12. 20	정수현		최초작성
 * 
 *****************************************************************/
public class BizUser extends Biz {
	private static Logger logger = Logger.getLogger(BizCompany.class.getName());
	private Connection conn = null;
	private HttpSession session;

	/**
	 * @param req
	 * @param res
	 */
	public BizUser(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		session = req.getSession();
		// FIXME Auto-generated constructor stub
	}

	/**
	 * @param mr
	 * @param req
	 * @param res
	 */
	public BizUser(MultipartRequest mr, HttpServletRequest req,
			HttpServletResponse res) {
		super(mr, req, res);
		session = req.getSession();
		// FIXME Auto-generated constructor stub
	}

	/**
	 * 고객사 로그인 
	 * 
	 * @return
	 */
    public boolean login(){
		try{
			conn = DaoCom.getConnection();
			StrUtil strUtil = new StrUtil();
			DaoUser dao = new DaoUser(conn);
						
			logout();
			
			String sUser_id 		= "";
			String sUser_pw		= "";
			
			sUser_id = props.getProperty("p_user_id");
			sUser_pw = props.getProperty("p_user_pw");
			
			sUser_id = strUtil.sqlInjection(sUser_id);
			sUser_pw = strUtil.sqlInjection(sUser_pw);
			
			props.setProperty("p_user_id", sUser_id);
			props.setProperty("p_user_pw", sUser_pw);
			
			Record rec = dao.login(props);
			//System.out.println(rec.size());
			if (rec.size() == 0) {
				//ParamUtil.setError(req, "해당하는 ID/비밀번호 없습니다.");
				//logger.warn("비밀번호 오류 :" + props.getProperty("user_email") + " / " + props.getProperty("user_password"));
				return false;
			} else {
				req.setAttribute("useritem", rec);
				
				HttpSession session = req.getSession();
				session.setAttribute("SS_USER_ID", rec.get("user_id",""));
				//session.setAttribute("SS_USER_TYPE", rec.get("com_type",""));
								
				return true;
			}
		} catch (BizException ex) {
			try {conn.rollback();} catch(Exception se){}
			req.setAttribute("errMessage", ex.getMessage());
		} catch (Exception ex) {
			try {conn.rollback();} catch(Exception se){}
			req.setAttribute("errMessage", ex.getMessage());
		}finally{
			DaoCom.close(conn);
		}
		return true;
    }
    
    /**
     * 로그아웃 처리
     *
     */
    public void logout(){
		HttpSession session = req.getSession();
		session.removeAttribute("SS_USER_ID");
		//session.removeAttribute("SS_COM_JOIN");		
		//session.invalidate();
    }
    
    
    /**
     * 고객사 리스트
     * 
     * @return veiw 할 URL String
     */    
    public String getUserList(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoUser dao = new DaoUser(conn);

	    	String sCom_id = session.getAttribute("SS_COM_ID").toString();
	    	props.setProperty("p_com_id", sCom_id);
	    	
	    	ListData userlist = dao.getUserList(props);
			req.setAttribute("userlist", userlist);
			
			conn.commit();
			return null;
		} catch (BizException ex) {
			try {conn.rollback();} catch(Exception se){}
			req.setAttribute("errMessage", ex.getMessage());
			
			return null;
		} catch (SQLException ex) {
			try {conn.rollback();} catch(Exception se){}
			req.setAttribute("errMessage", ex.getMessage());
			
			return null;
		}finally{
			DaoCom.close(conn);
		}

    }
    
    
    
    /**
     * 고객사 상세 정보 가져오기
     * @return 
     * 
     */
    public String getUserItem(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoUser dao = new DaoUser(conn);
	    	
			Record rec = dao.getUserItem(props);
			req.setAttribute("record", rec);
	    	
			conn.commit();
			return null;
		} catch (BizException ex) {
			try {conn.rollback();} catch(Exception se){}
			req.setAttribute("errMessage", ex.getMessage());
			
			return null;
		} catch (SQLException ex) {
			try {conn.rollback();} catch(Exception se){}
			req.setAttribute("errMessage", ex.getMessage());
			
			return null;
		}finally{
			DaoCom.close(conn);
		}
    } 
    
    
    /**
     * 고객사 상세 정보 가져오기
     * @return 
     * 
     */
    public String getUserItem(String p_user_id){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoUser dao = new DaoUser(conn);
	    	
	    	props.setProperty("p_user_id", p_user_id);
			Record rec = dao.getUserItem(props);
			req.setAttribute("recU", rec);
	    	
			conn.commit();
			return null;
		} catch (BizException ex) {
			try {conn.rollback();} catch(Exception se){}
			req.setAttribute("errMessage", ex.getMessage());
			
			return null;
		} catch (SQLException ex) {
			try {conn.rollback();} catch(Exception se){}
			req.setAttribute("errMessage", ex.getMessage());
			
			return null;
		}finally{
			DaoCom.close(conn);
		}
    }     
    
    

    
    
    
    
    /*
     * 일단 여기까지 2014-01-07 까지 기본 정의  
     */
    
    
	/**
     * 관원 아이디값 확인
     *
     */
	public String checkUserID() throws Exception{
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
			DaoUser dao = new DaoUser(conn);

			
			
			
	    	ListData list = dao.checkUserID(props);
			req.setAttribute("list", list);
			req.setAttribute("params", props);
			
			conn.commit();
			return null;
		} catch (BizException ex) {
			try {conn.rollback();} catch(Exception se){}
			req.setAttribute("errMessage", ex.getMessage());
			
			return null;
		} catch (SQLException ex) {
			try {conn.rollback();} catch(Exception se){}
			req.setAttribute("errMessage", ex.getMessage());
			
			return null;
		}finally{
			DaoCom.close(conn);
		}
	}    
    
    
	/**
     * 사이트 아이디값 확인
     *
     */
	public Boolean checkUserID(String sUserID) throws Exception{
		Boolean bResult = false;
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
			DaoUser dao = new DaoUser(conn);

			props.setProperty("p_user_id", sUserID);
						
	    	Record rec = dao.checkUserIDparams(props);
			//req.setAttribute("list", list);
			//req.setAttribute("params", props);
	    	if(rec == null)
	    		bResult = false;
	    	else
	    		bResult = true;
			
			conn.commit();
			return bResult;
		} catch (BizException ex) {
			try {conn.rollback();} catch(Exception se){}
			req.setAttribute("errMessage", ex.getMessage());
			
			return false;
		} catch (SQLException ex) {
			try {conn.rollback();} catch(Exception se){}
			req.setAttribute("errMessage", ex.getMessage());
			
			return false;
		}finally{
			DaoCom.close(conn);
		}
	}          
    
    
	/**
     * 사용자정보 신규등록
     * 
     * @return 
     */    
    public String addUser(){
    	String Message = "";
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoUser dao = new DaoUser(conn);

	    	dao.addUser(props);
	    	
	    	Record recUser = dao.getUser(props);
	    	req.setAttribute("recordUser", recUser);
			
			
			Message = "Complete.";
			conn.commit();
		} catch (BizException ex) {
			try {conn.rollback();} catch(Exception se){}
			Message = "system Error";
			
		} catch (SQLException ex) {
			try {conn.rollback();} catch(Exception se){}
			Message = "system Error";
		}finally{
			DaoCom.close(conn);
		}
		return Message;
    }     
    
    
    
    
    
    
    
    
    
    

	    
}
