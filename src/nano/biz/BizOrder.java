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
 * @description : 주문  관련 로직 처리하기위한 class
 * $Id: BizOrder.java,v 1.0 2014/04/04 14:33:00 $
 * $Revision: 1.0 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 12. 20	정수현		최초작성
 * 2014. 04. 04	조용욱		참조생성
 *****************************************************************/
public class BizOrder extends Biz {
	private static Logger logger = Logger.getLogger(BizOrder.class.getName());
	private Connection conn = null;
	private HttpSession session;

	/**
	 * @param req
	 * @param res
	 */
	public BizOrder(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		session = req.getSession();
		// FIXME Auto-generated constructor stub
	}

	/**
	 * @param mr
	 * @param req
	 * @param res
	 */
	public BizOrder(MultipartRequest mr, HttpServletRequest req,
			HttpServletResponse res) {
		super(mr, req, res);
		session = req.getSession();
		// FIXME Auto-generated constructor stub
	}

    
    
    /**
     * 사용자주문 리스트 - 파라미터 업체(학원) 아이디, 제품아이디
     * 
     * @return veiw 할 URL String
     */    
    public String getUserOrderList(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoOrder dao = new DaoOrder(conn);

	    	String sCom_id = session.getAttribute("SS_COM_ID").toString();
	    	props.setProperty("p_com_id", sCom_id);
	    	
	    	ListData userorderlist = dao.getUserOrderList(props);
			req.setAttribute("userorderlist", userorderlist);
			
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
     * 업체주문 리스트 - 파라미터 업체(학원) 아이디, 제품아이디
     * 
     * @return veiw 할 URL String
     */    
    public String getUserOrderBySizeList(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoOrder dao = new DaoOrder(conn);

	    	ListData userorderbysizelist = dao.getUserOrderBySizeList(props);
			req.setAttribute("userorderbysizelist", userorderbysizelist);
			
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
     * 업체주문 리스트 - 파라미터 업체(학원) 아이디, 제품아이디
     * 
     * @return veiw 할 URL String
     */    
    public String getCompanyOrderBySizeList(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoOrder dao = new DaoOrder(conn);

	    	ListData companyorderbysizelist = dao.getCompanyOrderBySizeList(props);
			req.setAttribute("companyorderbysizelist", companyorderbysizelist);
			
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
     * 업체주문 확정 - 파라미터 업체(학원) 아이디, 제품아이디
     * 
     * @return veiw 할 URL String
     */    
    public String setCompanyOrder(){
    	String Message = "";
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoOrder dao = new DaoOrder(conn);
	    	
	    	dao.moveCompanyOrder(props);
	    	//conn.commit();
	    	
	    	dao.deleteCompanyOrder(props);
	    	//conn.commit();
	    	
	    	dao.setCompanyOrder(props);
//	    	ListData userorderbysizelist = dao.getUserOrderBySizeList(props);
//			req.setAttribute("userorderbysizelist", userorderbysizelist);
			
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
     * 주문 상세 정보 가져오기
     * @param 주문아이디 
     * @return
     */
    public String getOrderItem(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoOrder dao = new DaoOrder(conn);
	    	
			Record rec = dao.getOrderItem(props);
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
     * 사용자주문 신규등록
     * @param 학생이름, 주문자이름, 사이즈, 수량
     * @return 
     */    
    public String addUserOrder(){
    	String Message = "";
		try{
			StrUtil strUtil = new StrUtil();
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoOrder dao = new DaoOrder(conn);

			String sOrder_student_name 		= "";
			String sOrder_parent_name		= "";
			String sSize	= "";
			String sAmount	= "";
			
			sOrder_student_name = props.getProperty("p_order_student_name");
			sOrder_parent_name = props.getProperty("p_order_parent_name");
			sSize = props.getProperty("p_size");
			sAmount = props.getProperty("p_amount");
			
			sOrder_student_name = strUtil.sqlInjection(sOrder_student_name);
			sOrder_parent_name = strUtil.sqlInjection(sOrder_parent_name);
			sSize = strUtil.sqlInjection(sSize);
			sAmount = strUtil.sqlInjection(sAmount);
			
			props.setProperty("p_order_student_name", sOrder_student_name);
			props.setProperty("p_order_parent_name", sOrder_parent_name);
			props.setProperty("p_size", sSize);	    
			props.setProperty("p_amount", sAmount);
	    	
	    	
	    	dao.addUserOrder(props);

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
     * 주문 정보 변경
     * 
     * @return 
     */    
    public String updateUserOrder(){
    	String Message = "";
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
			DaoOrder dao = new DaoOrder(conn);

	    	dao.updateUserOrder(props);
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
     * 주문 정보 삭제 - 레코드 실제 삭제
     * @return 
     * 
     */
    public String deleteOrder(){
    	String Message = "";
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoOrder dao = new DaoOrder(conn);
	    	
			dao.deleteOrder(props);	//구독 FLAG 변경
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
     * 주문 정보 삭제 - flag 변경
     * 필요할 경우
     * @return 
     * 
     */
    public String deleteStateOrder(){
    	String Message = "";
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoOrder dao = new DaoOrder(conn);
	    	
			dao.deleteStateOrder(props);	//구독 FLAG 변경
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
    
    
    
    
    
    
    
    
    /*
     * 일단 여기까지 2014-04-07 까지 기본 정의  
     */
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

	    
}
