package nano.biz;


import nano.util.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.servlet.http.*;

import com.oreilly.servlet.*;


/*****************************************************************
 * @description : Business 로직을 작성하기위한 abstract class
 *                Biz 로직 작성시 이 클래스를 상속 받아 구현해야 한다.
 * $Id: Biz.java,v 1.2 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2012. 12. 15	정수현		최초작성
 * 
 *****************************************************************/


public abstract class Biz {
	protected HttpServletRequest req;
	protected MultipartRequest mr;
	protected HttpServletResponse res;
	protected int startPage = 1;
	protected Properties props;
	protected String message = "";
	
	/**
	 * 일반적인 (파일 업로드가 필요 없는) Biz Constructor 
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public Biz (HttpServletRequest req, HttpServletResponse res) 
	{
		this.req = req;
		this.res = res;
		props = ParamUtil.getParams(req);
//		this.params = ParamUtil.getParams(req);
//		startPage =  ParamUtil.getIntParameter(req.getParameter("cpage"),1);
		
	}

	/**
	 * 파일 업로드시 호출 되는 Biz Constructor
	 * 
	 * @param mr
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public Biz (MultipartRequest mr, HttpServletRequest req, HttpServletResponse res) 
	{
		this.mr = mr;
		this.req = req;
		
		this.res = res;
		this.props = ParamUtil.getParams(mr, req);
//		this.params = ParamUtil.getParams(mr, req);
//		startPage =  ParamUtil.getIntParameter(req.getParameter("page"),1);
		
	}

	public void addProperty(String name, String value){
		this.props.put(name,value);
	}
	
//	/**
//	 * 각 Biz 에서 구현해야 하는 Method
//	 * 
//	 * @param cmd
//	 * @return next url
//	 */
//	public abstract String doSelect(String cmd);
//	public abstract String doUpdate(String cmd);
//	public abstract String doInsert(String cmd);
//	public abstract String doDelete(String cmd);
//	public abstract String execute(String cmd);
	
	

}
