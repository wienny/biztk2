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
 * @description : 고객사  관련 로직 처리하기위한 class
 * $Id: BizAdmin.java,v 1.2 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 12. 20	정수현		최초작성
 * 
 *****************************************************************/
public class BizCompany extends Biz {
	private static Logger logger = Logger.getLogger(BizCompany.class.getName());
	private Connection conn = null;
	private HttpSession session;

	/**
	 * @param req
	 * @param res
	 */
	public BizCompany(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		session = req.getSession();
		// FIXME Auto-generated constructor stub
	}

	/**
	 * @param mr
	 * @param req
	 * @param res
	 */
	public BizCompany(MultipartRequest mr, HttpServletRequest req,
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
			DaoCompany dao = new DaoCompany(conn);
						
			logout();
			
			String sCom_id 		= "";
			String sCom_pw		= "";
			
			sCom_id = props.getProperty("p_com_id");
			sCom_pw = props.getProperty("p_com_pw");
			
			sCom_id = strUtil.sqlInjection(sCom_id);
			sCom_pw = strUtil.sqlInjection(sCom_pw);
			
			props.setProperty("p_com_id", sCom_id);
			props.setProperty("p_com_pw", sCom_pw);
			
			Record rec = dao.login(props);
			//System.out.println(rec.size());
			if (rec.size() == 0) {
				//ParamUtil.setError(req, "해당하는 ID/비밀번호 없습니다.");
				//logger.warn("비밀번호 오류 :" + props.getProperty("user_email") + " / " + props.getProperty("user_password"));
				return false;
			} else {
				req.setAttribute("company", rec);
				
				HttpSession session = req.getSession();
				session.setAttribute("SS_COM_ID", rec.get("com_id",""));
				session.setAttribute("SS_COM_TYPE", rec.get("com_type",""));
				session.setAttribute("SS_COM_NAME", rec.get("com_name",""));
								
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
		session.removeAttribute("SS_COM_ID");
		session.removeAttribute("SS_COM_JOIN");		
		//session.invalidate();
    }
    
    
    /**
     * 고객사 리스트
     * 
     * @return veiw 할 URL String
     */    
    public String getCompanyList(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoCompany dao = new DaoCompany(conn);

	    	ListData companylist = dao.getCompanyList(props);
			req.setAttribute("companylist", companylist);
			
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
    public String getCompanyItem(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoCompany dao = new DaoCompany(conn);
	    	
			Record rec = dao.getCompanyItem(props);
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
    public String getCompanyItem(String p_com_id){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoCompany dao = new DaoCompany(conn);
	    	
	    	props.setProperty("p_com_id", p_com_id);
			Record rec = dao.getCompanyItem(props);
			req.setAttribute("recC", rec);
	    	
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
     * 고객사 주문 리스트
     * 
     * @return veiw 할 URL String
     */    
    public String getCompanyOrderList(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoCompany dao = new DaoCompany(conn);

	    	ListData companyorderlist = dao.getCompanyOrderList(props);
			req.setAttribute("companyorderlist", companyorderlist);
			
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
     * 고객사 신규등록
     * 
     * @return 
     */    
    public String addCompany(){
    	String Message = "";
		try{
			StrUtil strUtil = new StrUtil();
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoCompany dao = new DaoCompany(conn);

			String sCom_id 		= "";
			String sCom_pw		= "";
			String sCom_email	= "";
			
			sCom_id = props.getProperty("p_com_rid");
			sCom_pw = props.getProperty("p_com_rpw");
			sCom_email = props.getProperty("p_com_email");
			
			sCom_id = strUtil.sqlInjection(sCom_id);
			sCom_pw = strUtil.sqlInjection(sCom_pw);
			sCom_email = strUtil.sqlInjection(sCom_email);
			
			props.setProperty("p_comr_id", sCom_id);
			props.setProperty("p_comr_pw", sCom_pw);
			props.setProperty("p_com_email", sCom_email);	    
	    	
	    	
	    	dao.addCompany(props);
	    	
	    	HttpSession session = req.getSession();
			//session.setAttribute("SS_COM_JOIN", props.getProperty("p_com_rid",""));
			//session.setAttribute("SS_COM_ID", props.getProperty("p_com_rid",""));
			//session.setAttribute("SS_COM_TYPE", props.getProperty("p_com_type",""));
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
    
    
    /**
     * 고객사 정보 변경
     * 
     * @return 
     */    
    public String updateCompany(){
    	String Message = "";
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
			DaoCompany dao = new DaoCompany(conn);

	    	dao.updateCompany(props);
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
    
    
    /**
     * 고객사 정보 삭제 - 레코드 실제 삭제
     * @return 
     * 
     */
    public String deleteCompany(){
    	String Message = "";
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoCompany dao = new DaoCompany(conn);
	    	
			dao.deleteCompany(props);	//구독 FLAG 변경
	    	//if(!req.getParameter("cart_num").equals("")){
	    	//	dao.removeCart(props);		//카트 삭제
	    	//}
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
    
    
    /**
     * 고객사 정보 삭제 - flag 변경
     * 필요할 경우
     * @return 
     * 
     */
    public String deleteStateCompany(){
    	String Message = "";
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoCompany dao = new DaoCompany(conn);
	    	
			dao.deleteStateCompany(props);	//구독 FLAG 변경
	    	//if(!req.getParameter("cart_num").equals("")){
	    	//	dao.removeCart(props);		//카트 삭제
	    	//}
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
    
    
    /**
     * 고객사 비밀번호 확인
     * 
     * @return 
     */
    
    public boolean pwCheckCompany(){
    	
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
			DaoCompany dao = new DaoCompany(conn);

			return dao.pwCheckCompany(props);
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
    
    
    
    
    
    /*
     * 일단 여기까지 2014-01-07 까지 기본 정의  
     */
    
    
    
    
    
	///////////////////////////////////////////////////////////////////////////////
	/**
	* PUSH REGISTER
	* 
	* @return 
	*/    
	public String setPushRegister(){
		String Message = "";
		try{
			StrUtil strUtil = new StrUtil();
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
			DaoCompany dao = new DaoCompany(conn);
			
			
			
			Message = dao.setPushRegister(props);
			
	
			//Message = "Complete.";
			conn.commit();
			} catch (BizException ex) {
			try {conn.rollback();} catch(Exception se){}
			Message = "false";
			
			} catch (SQLException ex) {
			try {conn.rollback();} catch(Exception se){}
			Message = "false";
			}finally{
			DaoCom.close(conn);
		}
		return Message;
	} 
	
	
	/**
	* PUSH GCM 리스트
	* 
	* @return veiw 할 URL String
	*/    
	public String getPushList(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
			DaoCompany dao = new DaoCompany(conn);
			
			ListData getPushList = dao.getPushList(props);
			req.setAttribute("getPushList", getPushList);
			
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
	* PUSH GCM 리스트
	* 
	* @return veiw 할 URL String
	*/    
	public String getPushIOSList(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
			DaoCompany dao = new DaoCompany(conn);
			
			ListData getPushIOSList = dao.getPushIOSList(props);
			req.setAttribute("getPushIOSList", getPushIOSList);
			
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
    

	    
}
