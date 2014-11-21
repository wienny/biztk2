package nano.util;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
 
import org.apache.log4j.*;

/*****************************************************************
 * @description : 로그 처리를 하기위한 Servlet
 * 		WAS 시작시 자동으로 실행하며 (web.xml에서 지정)
 *      로그파일 위치, 로그형태 등 을 설정한 환경파일을 읽는다.
 *        
 * $Id: Log4jInit.java,v 1.2 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 10. 2	정수현		최초작성
 * 
 *****************************************************************/

public class Log4jInit extends HttpServlet {
	private static final long serialVersionUID = 1L;

//  private static final String CONTENT_TYPE = "text/html; charset=EUC-KR";
  //Initialize global variables
  public void init() throws ServletException {
      String prefix =  getServletContext().getRealPath("/");
       String file = getInitParameter("log4j-init-file");
       // if the log4j-init-file is not set, then no point in trying
       if(file != null) {
         PropertyConfigurator.configure(prefix+file);
       }//end if
  }//end init
  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  }
  //Process the HTTP Post request
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  }
  //Clean up resources
  public void destroy() {
  }
}
