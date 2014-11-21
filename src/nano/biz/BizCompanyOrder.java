package nano.biz;


import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nano.BizException;
import nano.ListData;
import nano.Record;
import nano.dao.DaoAdmin;
import nano.dao.DaoCom;
import nano.dao.DaoUserOrder;
import nano.dao.DaoCompanyOrder;
import nano.util.MailUtil;
import nano.util.ParamUtil;

import org.apache.log4j.Logger;

import com.oreilly.servlet.MultipartRequest;



/*****************************************************************
 * @description : 메인페이지 class
 * $Id: BizMain.java,v 1.2 2014/03/24 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2014. 03. 24	김희삼		최초작성
 * 
 *****************************************************************/
/**
 * @author glory
 *
 */
/**
 * @author glory
 *
 */
/**
 * @author glory
 *
 */
public class BizCompanyOrder extends Biz {
	private static Logger logger = Logger.getLogger(BizUserOrder.class.getName());
	private Connection conn = null;
	private HttpSession session;
	/**
	 * @param req
	 * @param res
	 */
	public BizCompanyOrder(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		session = req.getSession();
		// FIXME Auto-generated constructor stub
	}

	/**
	 * @param mr
	 * @param req
	 * @param res
	 */
	public BizCompanyOrder(MultipartRequest mr, HttpServletRequest req,
			HttpServletResponse res) {
		super(mr, req, res);
		session = req.getSession();
		// FIXME Auto-generated constructor stub
	}
	
    /**
   	 * 사용자 주문 등록/수정
   	 *
   	 */
   	public String setCompanyMessage() {
   		String Message = "";
   		try{
   			conn = DaoCom.getConnection();
   			conn.setAutoCommit(false);
   	    	DaoCompanyOrder dao = new DaoCompanyOrder(conn);
   	    	
   	    	Message = dao.setCompanyMessage(props);

			conn.commit();
   		} catch (BizException ex) {
   			try {conn.rollback();} catch(Exception se){}
   			Message =  ex.getMessage();
   		} catch (SQLException ex) {
   			try {conn.rollback();} catch(Exception se){}
   			Message =  ex.getMessage();
   			
   		}finally{
   			DaoCom.close(conn);
   		}
   		return Message;
   	}	
 
    /**
   	 * product 정보 조회
   	 *
   	 */
    public String getExplainInfo(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
			DaoCompanyOrder dao = new DaoCompanyOrder(conn);
	    	
			Record rec = dao.getExplainInfo(props);
			req.setAttribute("getExplainInfo", rec);
	    	
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
   	 * 이메일 전송
   	 *
   	 */
   	public String setCompanyEmail() {
   		String Message = "";
   		try{
   			conn = DaoCom.getConnection();
   			conn.setAutoCommit(false);
   	    	DaoCompanyOrder dao = new DaoCompanyOrder(conn);
   	    	
   	    	Message = dao.setCompanyEmail(props);

			conn.commit();
   		} catch (SQLException ex) {
   			try {conn.rollback();} catch(Exception se){}
   			Message =  ex.getMessage();
   			
   		}finally{
   			DaoCom.close(conn);
   		}
   		return Message;
   	}
   	
    /**
   	 * Company 명
   	 *
   	 */
   	public String getCompanyName() {
   		String Message = "";
   		try{
   			conn = DaoCom.getConnection();
   			conn.setAutoCommit(false);
   	    	DaoCompanyOrder dao = new DaoCompanyOrder(conn);
   	    	
   	    	Message = dao.getCompanyName(props);

			conn.commit();
   		} catch (SQLException ex) {
   			try {conn.rollback();} catch(Exception se){}
   			Message =  ex.getMessage();
   			
   		}finally{
   			DaoCom.close(conn);
   		}
   		return Message;
   	}
   	   	   	
}