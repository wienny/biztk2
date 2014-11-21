package nano.util;


import java.util.*;

import javax.servlet.http.*;
 
import com.oreilly.servlet.*;

/*****************************************************************
 * @description : 폼 파라미터를 처리 하기위한 class
 * $Id: ParamUtil.java,v 1.4 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 10. 2	정수현		최초작성
 * 
 *****************************************************************/

public class ParamUtil {

	/**
	 * 파일 업로드 폼의 파라미터 값을 Properties 에 각 이름과 값을 담아 놓는다.
	 * 
	 * @param mr
	 * @param req
	 * @return
	 */
	public static Properties getParams(MultipartRequest mr, HttpServletRequest req) 
	{
		Properties params = new Properties();
		HttpSession session = req.getSession();
		Enumeration e = mr.getParameterNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();

			try {
				if (!mr.getParameter(key).equals("")) {
					//System.out.println(key + " =" + mr.getParameter(key));
					params.put(key, mr.getParameter(key));
					//params.setValue(key, new String((mr.getParameter(key)).getBytes("8859_1"), "UTF-8"));
				}
			} 
			catch (Exception exception) {
				System.out.println("getParams error!");
				exception.printStackTrace();
			}
		}
		e=mr.getFileNames();  // 폼의 이름 반환
		while (e.hasMoreElements()) {
	         String strName = (String) e.nextElement();
	         String fileName= mr.getFilesystemName(strName);
	         if (fileName != null) {
				System.out.println(strName + " =" + fileName);
				try {
		         	params.put(strName, fileName);
//	         	params.setValue(strName, new String (fileName.getBytes("8859_1"),"euc-kr" ));
				}catch (Exception ex){}
	         }
		}
		

		
		
		e = session.getAttributeNames() ;
		while (e.hasMoreElements()) {

			String key = (String) e.nextElement();
			try {
				if (session.getAttribute(key) instanceof String)
				{
					//if (!((String)session.getAttribute(key)).equals("")  && params.get(key).equals("")) {
					if (!((String)session.getAttribute(key)).equals("")) {
						params.put(key, (String)session.getAttribute(key));

						//params.setValue(key, new String(((String)session.getAttribute(key)).getBytes("8859_1"), "UTF-8"));
					}
				}
			} 
			catch (Exception exception) {
				System.out.println("getParams session value error!");
				exception.printStackTrace();
			}
		}

		return params; 
	}		
	
	/**
	 * 일반적인 폼의 파라미터 값을 Properties 에 각 이름과 값을 담아 놓는다.
	 * 
	 * @param req : Servlet Request
	 * @return 파라미터 이름과 값
	 */
	public static Properties getParams(HttpServletRequest req) 
	{
		Properties params = new Properties();
		HttpSession session = req.getSession();
		Enumeration e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			try {
				if (!req.getParameter(key).equals("")) {
					//System.out.println(key + " =" + req.getParameter(key));
					//params.put(key, new String(req.getParameter(key).getBytes("8859_1"),"UTF-8"));
					params.put(key, req.getParameter(key));
					//params.setValue(key, new String((req.getParameter(key)).getBytes("8859_1"), "UTF-8"));
				}
			} 
			catch (Exception exception) {
				System.out.println("getParams error!");
				exception.printStackTrace();
			}
		}

		e = session.getAttributeNames() ;
		while (e.hasMoreElements()) {

			String key = (String) e.nextElement();
			try {
				if (session.getAttribute(key) instanceof String)
				{
					//if (!((String)session.getAttribute(key)).equals("")  && params.get(key).equals("")) {
					if (!((String)session.getAttribute(key)).equals("")) {
						params.put(key, (String)session.getAttribute(key));

						//params.setValue(key, new String(((String)session.getAttribute(key)).getBytes("8859_1"), "UTF-8"));
					}
				}
			} 
			catch (Exception exception) {
				System.out.println("getParams session value error!");
				exception.printStackTrace();
			}
		}

		return params; 
	}
	
	/*
	public static Record addProperties(Record rec, Properties props){
		Enumeration e = props.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			try {
				if (!props.getProperty(key).equals("")) {
					rec.put(key, props.getProperty(key));
				}
			} 
			catch (Exception exception) {
				System.out.println("addProperties error!");
				exception.printStackTrace();
			}
		}
		return rec;
	}
	*/
	
	/**
	 * 파라미터를 String 형태로 가져온다.
	 * 값이 Null 일 경우는 대체한다.
	 * 
	 * @param req
	 * @param ifNulltoReplace
	 * @return
	 */
	public static String getReqParameter(String req,String ifNulltoReplace ) {
		if ( req == null || req.equals("")) return ifNulltoReplace;
		else return req.trim();
	}
    
	/**
	 * 파라미터를 String 형태로 가져온다.
	 * Null 일경우는 "" 문자를 리턴한다.
	 * 
	 * @param req
	 * @return
	 */
	
	public static String getReqParameter(Object req ) {
		String value = (String)req; 
		if (value == null || value.equals("")) return "";
		else               return value.trim();
	}
    
	/**
	 * 파라미터 값을 int 형태로 가져온다.
	 * Null 일 경우 대체한다.
	 * 
	 * @param req
	 * @param ifNulltoReplace
	 * @return
	 */
	public static int getIntParameter(Object param,int ifNulltoReplace ) {
		try {
			String req = (String)param; 
			if ( req == null || req.equals("")) return ifNulltoReplace;
			else               return Integer.parseInt(req.toString());
		}catch(NumberFormatException e){
			return ifNulltoReplace;
		}
	}
    
	/**
	 * 파라미터 값을 int 형태로 가져온다.
	 * Null 일경우  0 을 리턴 한다.
	 * 
	 * @param req
	 * @return
	 */
	public static int getIntParameter(Object param ) {
		try {
			String req = (String)param;
			if (req == null || req.equals("")) return 0;
			else               return Integer.parseInt(req.toString());
		}catch(NumberFormatException e){
			return 0;
		}
	}

	public static String enc (String value){
		String result="";
		if (value==null) return result;
	  try {
		 result = new String(value.getBytes("8859_1"),"utf-8");
	  } catch (Exception e) {
		result = "";
		 e.printStackTrace();
	  }
		return result;		
	}

	public static String dec (String value){
		String result="";
		if (value==null) return result;
		try {
			result = new String(value.getBytes("utf-8"),"8859_1");
		} catch (Exception e) {
			result = "";
			e.printStackTrace();
		}
		return result;		
	}	
	
	/**
	 * session 의 값을 가져온다.
	 * 
	 * @param session : session
	 * @param name : session 이름
	 * @return session 값
	 */
	public static String getSessionValue(HttpSession session, String name) {
		String value= (String)session.getAttribute(name);
		if (value == null){
			value = "";
		}
		return value;
	}    
	
	/**
	 * 관리자 여부
	 * 
	 * @param session
	 * @return
	 */
	public static boolean isAdmin(HttpSession session){
		String value= (String)session.getAttribute("SS_MEM_LVL");
		boolean isAdmin = false;
		value = (value ==null)?"9": value;
		if (value.equals("1")){
			isAdmin =true;
		}
		if (value.equals("2")){
			isAdmin =true;
		}
		return isAdmin;
	}

	/**
	 * 새로고침 등의 처리로 중복 입력 방지를 위해 이전의 명령을 Check 한다.
	 * 
	 * @param session
	 * @param lastcmd
	 * @return true = 처리 가능
	 *         false = 처리 불가
	 */
	public static boolean checkCmd(HttpSession session, String lastcmd){
		String value = (String)session.getAttribute("lastcmd");
		if ((value != null) && (value.equals(lastcmd))){
			return false;
		}
		return true;
	}
	
	public static void setError(HttpServletRequest req, String errorMessage){
		req.setAttribute("errMessage", errorMessage);
	}
}
