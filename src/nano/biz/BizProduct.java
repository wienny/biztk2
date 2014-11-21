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
 * @description : 상품  관련 로직 처리하기위한 class
 * $Id: BizProduct.java,v 1.0 2014/04/04 14:33:00 $
 * $Revision: 1.0 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 12. 20	정수현		최초작성
 * 2014. 04. 04	조용욱		참조생성
 *****************************************************************/
public class BizProduct extends Biz {
	private static Logger logger = Logger.getLogger(BizProduct.class.getName());
	private Connection conn = null;
	private HttpSession session;

	/**
	 * @param req
	 * @param res
	 */
	public BizProduct(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		session = req.getSession();
		// FIXME Auto-generated constructor stub
	}

	/**
	 * @param mr
	 * @param req
	 * @param res
	 */
	public BizProduct(MultipartRequest mr, HttpServletRequest req,
			HttpServletResponse res) {
		super(mr, req, res);
		session = req.getSession();
		// FIXME Auto-generated constructor stub
	}
	
	
	
    /**
     * 상품 리스트
     * 
     * @return veiw 할 URL String
     */    
    public String getPackageList(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoProduct dao = new DaoProduct(conn);

	    	ListData packagelist = dao.getPackageList(props);
			req.setAttribute("packagelist", packagelist);
			
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
     * 개별제품 리스트
     * 
     * @return veiw 할 URL String
     */    
    public String getProductList(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoProduct dao = new DaoProduct(conn);

	    	props.setProperty("p_com_type", session.getAttribute("SS_COM_TYPE").toString());
	    	props.setProperty("p_com_id", session.getAttribute("SS_COM_ID").toString());
	    	
	    	ListData productlist = dao.getProductList(props);
			req.setAttribute("productlist", productlist);
			
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
     * 상품 상세 정보 가져오기
     * @return 
     * 
     */
    public String getProductItem(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoProduct dao = new DaoProduct(conn);
	    	
			Record rec = dao.getProductItem(props);
			req.setAttribute("productrecord", rec);
	    	
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
     * 상품 상세 정보 가져오기
     * @return 
     * 
     */
    public String getProductItem(String p_product_id){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoProduct dao = new DaoProduct(conn);
	    	
	    	props.setProperty("p_product_id", p_product_id);
			Record rec = dao.getProductItem(props);
			req.setAttribute("productrecord", rec);
	    	
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
     * 개별제품 리스트
     * 
     * @return veiw 할 URL String
     */    
    public String getProComList(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoProduct dao = new DaoProduct(conn);

	    	props.setProperty("p_com_type", session.getAttribute("SS_COM_TYPE").toString());
	    	props.setProperty("p_com_id", session.getAttribute("SS_COM_ID").toString());
	    	
	    	ListData procomlist = dao.getProComList(props);
			req.setAttribute("procomlist", procomlist);
			
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
     * 상품 주문 리스트
     * 
     * @return veiw 할 URL String
     */    
    public String getProductOrderList(){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoProduct dao = new DaoProduct(conn);

	    	ListData productorderlist = dao.getProductOrderList(props);
			req.setAttribute("productorderlist", productorderlist);
			
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
     * 상품 신규등록
     * 
     * @return 
     */    
    public String addProduct(){
    	String Message = "";
		try{
			StrUtil strUtil = new StrUtil();
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoProduct dao = new DaoProduct(conn);

			String sCom_id 		= "";
			String sCom_pw		= "";
			String sCom_email	= "";
			
			sCom_id = props.getProperty("p_com_id");
			sCom_pw = props.getProperty("p_com_pw");
			sCom_email = props.getProperty("p_com_email");
			
			sCom_id = strUtil.sqlInjection(sCom_id);
			sCom_pw = strUtil.sqlInjection(sCom_pw);
			sCom_email = strUtil.sqlInjection(sCom_email);
			
			props.setProperty("p_com_id", sCom_id);
			props.setProperty("p_com_pw", sCom_pw);
			props.setProperty("p_com_email", sCom_email);	    
	    	
	    	
	    	dao.addProduct(props);
	    	
	    	HttpSession session = req.getSession();
			session.setAttribute("SS_COM_JOIN", props.getProperty("p_com_id",""));
			session.setAttribute("SS_COM_ID", props.getProperty("p_com_id",""));
			session.setAttribute("SS_COM_TYPE", props.getProperty("p_com_type",""));
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
     * 상품 정보 변경
     * 
     * @return 
     */    
    public String updateProduct(){
    	String Message = "";
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
			DaoProduct dao = new DaoProduct(conn);

	    	dao.updateProduct(props);
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
     * 상품 정보 삭제 - 레코드 실제 삭제
     * @return 
     * 
     */
    public String deleteProduct(){
    	String Message = "";
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoProduct dao = new DaoProduct(conn);
	    	
			dao.deleteProduct(props);	//구독 FLAG 변경
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
     * 상품 정보 삭제 - flag 변경
     * 필요할 경우
     * @return 
     * 
     */
    public String deleteStateProduct(){
    	String Message = "";
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoProduct dao = new DaoProduct(conn);
	    	
			dao.deleteStateProduct(props);	//구독 FLAG 변경
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
     * 상품 비밀번호 확인
     * 
     * @return 
     */
    
    public boolean pwCheckProduct(){
    	
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
			DaoProduct dao = new DaoProduct(conn);

			return dao.pwCheckProduct(props);
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
    
    
    
    
    
    /**
     * 상품 상세 정보 가져오기
     * @return 
     * 
     */
    public String getProductSubInfo(String p_product_id){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoProduct dao = new DaoProduct(conn);
	    	
	    	props.setProperty("p_product_id", p_product_id);
			ListData list = dao.getProductSubInfo(props);
			req.setAttribute("productsublist", list);
	    	
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
     * 상품 상세 정보 가져오기
     * @return 
     * 
     */
    public String getProductImgInfo(String p_product_id){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoProduct dao = new DaoProduct(conn);
	    	
	    	props.setProperty("p_product_id", p_product_id);
			ListData list = dao.getProductImgInfo(props);
			req.setAttribute("productimglist", list);
	    	
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
     * 상품 상세 정보 가져오기
     * @return 
     * 
     */
    public String getProductOptionInfo(String p_product_id){
		try{
			conn = DaoCom.getConnection();
			conn.setAutoCommit(false);
	    	DaoProduct dao = new DaoProduct(conn);
	    	
	    	props.setProperty("p_product_id", p_product_id);
			ListData list = dao.getProductOptionInfo(props);
			req.setAttribute("productoptionlist", list);
	    	
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
