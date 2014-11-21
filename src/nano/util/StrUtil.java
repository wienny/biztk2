package nano.util;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.*;

import nano.dao.DaoAdmin;

import org.apache.log4j.Logger;
/*****************************************************************
 * @description : 문자열 처리 하기위한 class
 * $Id: StrUtil.java,v 1.1 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 10. 2	정수현		최초작성
 * 
 *****************************************************************/

public class StrUtil {
	private static Logger logger = Logger.getLogger(DaoAdmin.class.getName());
	
	public static final String replyImage = "<img src='/image/re.gif' border=0>";
	
	/**
	 * 게시판 내용보기에서 내용 출력을 위한 부분(줄바꿈을 <br>로 바꿈)
	 * @param contents 출력 부분
	 * @return 줄바꿈이 처리된 String
	 */
    public static String nl2br(String contents)
    {
		if(contents.indexOf('\n') < 0)
			return contents;		
		StringBuffer buffer = new StringBuffer();
    	StringTokenizer st = new StringTokenizer(contents, "\n");
		while(st.hasMoreTokens()) {	
			buffer.append(st.nextToken()+ "<br>");
		}	
		return buffer.toString();	
    }
    /**
     * 직위에 따른 명칭.
     * @param code
     * @return
     */
    public static String group(String code){
		String value = "";
		if (code ==null){
			return value;
		}
		if (code.equals("0")){
			value = "대표이사";
		} else if (code.equals("1")){
			value = "이사";
		} else if (code.equals("2")){
			value = "부자";
		} else if (code.equals("3")){
			value = "차장";
		} else if (code.equals("4")){
			value = "과장";
		} else if (code.equals("5")){
			value = "대리";
		} else if (code.equals("6")){
			value = "사원";
		}else{
			value = "관리자";
		}
		return value;
    }
    
  //입력값 체크(sqlInjection 공격 대비)
  //"'", "script", "=", "%", "(", ")", "<", ">", "--", "insert", "update", "delete"
  	public String sqlInjection(String str){
  		String[] sql_injection = {"'", "script", "=", "%", "(", ")", "<", ">", "--", "insert", "update", "delete"};
  		ArrayList<String> li = new ArrayList<String>();
  		for(int i = 0; i < sql_injection.length ; i++){
  			str = str.replace(sql_injection[i],"");
  		}
  		return str;
  	}
  	
  	
  	//UTF형식으로 변환
	public String toUTF(String str) {
        String convStr = null;
        try {
            if(str == null)
                return "";

            // 현재문자열을 8859_1형식으로 읽어내어 KSC5601형식으로 변환
            convStr = new String(str.getBytes("ISO8859_1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return convStr;
    }
    /**
     * 숫자 문자열을 1,234,567 형태로 만듬
     * @param value
     * @return
     */
	public static String format(String value)
	{
		if ((value == null) || (value.equals(""))){
			return "0";
		}

		DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance();
		Long longvalue;
		try {
			longvalue = new Long(value);
		} catch (Exception e){
			return value;
		}
		
		df.applyPattern("#,###");
		return(df.format(longvalue));
	}
	
	/**
	 * len 길이로 앞에 0 를 채운다
	 * 
	 * @param value
	 * @param len
	 * @return
	 */
	public static String fillZero(int value, int len){
		String retValue = "";
		retValue = Integer.toString(value);
		int sl = retValue.length();
		for (int i = len; i > sl; i--){
			retValue = "0" + retValue;
		}
		return retValue;
	}
	
	/**
	 * 답변형 게시판 내용을 줄마다 맨 앞에 ">" 를 삽입한다  
	 * 
	 * @param strValue
	 * @return
	 */
	public static String getReply(String strValue){
		StringBuffer sb = new StringBuffer();
		StringTokenizer sTokenizer = new StringTokenizer(strValue,"\n"); 
        while (sTokenizer.hasMoreTokens()) { 
            sb.append("> ").append((String) sTokenizer.nextToken()).append("\n"); 
        }
        return sb.toString();
	}
	
	/**
	 * 게시판 답변글 제목에 레벨에 맞게 Indent 하여 리턴한다
	 * 
	 * @param strValue
	 * @return
	 */
	public static String getReplyTitle(Object strValue){
		StringBuffer sb = new StringBuffer();
		int lvl = 0;
		if (strValue == null) return "";
		
		try {
			lvl = Integer.parseInt((String)strValue);
		} catch (NumberFormatException e) {
			//ignore
		}
		//System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLL="+ lvl);
		for (int i = 0; i < lvl; i++) {
			sb.append("&nbsp;&nbsp;");
		}
		if (lvl > 0 ){ 
			sb.append(replyImage);
		}
		return sb.toString();
	}
	
	/**
	 * Controler 의 cmd Index 값을 구한다.
	 * 
	 * @param cl
	 * @param cmd
	 * @return
	 */
	public static int getCmdIndex(String [][] cl , String cmd){
		
		for (int i =0 ; i < cl.length; i++){
			if(cmd.equals(cl[i][0])){
				return i; 
			}
		}
		return -1;
	}



	    // 8859_1을 KSC5601로 변환
	    public String toKorean(String str) {
	        String convStr = null;
	        try {
	            if(str == null)
	                return "";

	            // 현재문자열을 8859_1형식으로 읽어내어 KSC5601형식으로 변환
	            convStr = new String(str.getBytes("8859_1"),"KSC5601");
	        } catch (UnsupportedEncodingException e) {
	        }
	        return convStr;
	    }

		// 8859_1을 KSC5601로 변환
	    public String toEUC(String str) {
	        String convStr = null;
	        try {
	            if(str == null)
	                return "";

	            // 현재문자열을 8859_1형식으로 읽어내어 KSC5601형식으로 변환
	            convStr = new String(str.getBytes("8859_1"),"KSC5601");
	        } catch (UnsupportedEncodingException e) {
	        }
	        return convStr;
	    }


	    // KSC5601을 8859_1로 변환
	    public String fromKorean(String str) {
	        String convStr = null;
	        try {
	            if(str == null)
	                return "";

	            // 현재문자열을 KSC5601형식으로 읽어내어 8859_1형식으로 변환
	            convStr = new String(str.getBytes("KSC5601"),"8859_1");
	        } catch (UnsupportedEncodingException e) {
	        }
	        return convStr;
	    }

	    // Null을 ""로 변환
	    public String checkNull(String str) {
	        String strTmp;
	        if (str == null)
	            strTmp = "";
	        else
	            strTmp = str;
	        return strTmp;
	    }


	    // Null을 0로 변환
	    public String checkNull2(String str) {
	        String strTmp;
	        if (str == null)
	            strTmp = "0";
	        else
	            strTmp = str;
	        return strTmp;
	    }
	    
	    
	    // Null을 0로 변환
	    public String checkNull2(Object str) {
	        String strTmp;
	        if (str == null)
	            strTmp = "0";
	        else
	            strTmp = str.toString();
	        return strTmp;
	    }	    


	    // 공백을 &nbsp;로 변환
	    public String null2nbsp(String str) {
	        String strTmp;
	        if (str == null || str.equals(""))
	            strTmp = "&nbsp;";
	        else
	            strTmp = str;
	        return strTmp;
	    }
	    
	    public String textQuot(String str){
	    	
	    	str = str.replaceAll("\\\"", "\'");
	    	str = str.replaceAll("'", "\'");
	    	return str;
	    }

	    // TextArea에서 입력받은 따옴표 앞에 \ 추가
	    public String addslash(String comment)
	    {
	        if(comment == null){
	            return "";
	        }

	        int length = comment.length();
	        StringBuffer buffer = new StringBuffer();
	        for (int i = 0; i < length; ++i)
	        {
	            String comp = comment.substring(i, i+1);
	            if ("'".compareTo(comp) == 0)
	            {
	                buffer.append("\'");
	            }
	            if ("\\\"".compareTo(comp) == 0)
	            {
	            	buffer.append("&quot;");
	            }
	            buffer.append(comp);
	        }
	        return buffer.toString();
	    }
	    
	    // TextArea에서 입력받은 따옴표 앞에 \ 추가
	    public String addslash2(String comment)
	    {
	        if(comment == null){
	            return "";
	        }

	        int length = comment.length();
	        StringBuffer buffer = new StringBuffer();

	        for (int i = 0; i < length; ++i)
	        {
	            String comp = comment.substring(i, i+1);
	            if ("\\\"".compareTo(comp) == 0)
	            {
	                buffer.append("&quot;");
//&nbsp; 
	            }

	            buffer.append(comp);
	        }
	        return buffer.toString();
	    }


	    // TextArea에서 입력받은 < or > 를 특수문자로 변환
	    public String html2spec(String comment)
	    {
	        if(comment == null){
	            return "";
	        }

	        int length = comment.length();
	        StringBuffer buffer = new StringBuffer();

	        for (int i = 0; i < length; ++i)
	        {
	            String comp = comment.substring(i, i+1);
	            if ("<@".compareTo(comp) == 0)
	                buffer.append("&lt;@");
	            else if ("<script".compareTo(comp) == 0)
	                buffer.append("&lt;script");
	            else
	                buffer.append(comp);
	        }
	        return buffer.toString();
	    }

	    public static String spaceadd(String addstr) 
	    { 
	        if(addstr == null){ 
	            return ""; 
	        } 

	        int length = addstr.length(); 
	        StringBuffer buffer = new StringBuffer(); 

	        for (int k = 0; k < length; ++k){ 
	            String comp = addstr.substring(k, k+1); 

	            if (" ".compareTo(comp) == 0){ 
	                buffer.append("&nbsp;"); 
	            } 

	            buffer.append(comp); 
	        } 
	        return buffer.toString(); 
	    } 
	    
	    public String strReplace(String strcoment) {  //쌍따음표 리턴
	    	StringTokenizer str = new StringTokenizer(strcoment);
	    	
	    	while (str.hasMoreTokens()) {
	    		strcoment += str.nextToken();
			}
	    	return strcoment;
		}
	    
	    
	 // 문자열에서 태그 삭제
	    public String tagDel(String s)
	    {
	    	System.out.println(" 시작 "+s);
			int text_start 		= 0;
			int text_end 			= 0;
			String parenthesis 	= "";
			
			for(int i=0; i<s.length(); i++ ){	
				if(s.indexOf("<") >= 0){	//태그가 있는경우
					text_start 	= s.indexOf("<");
					text_end	= s.indexOf(">");
					
					if(s != ""||s != null){
						if(text_start != -1&&text_end != -1){
							if(s.length()-text_end>text_start){
								parenthesis = s.substring(text_start, s.length()-text_end);
								s = s.replace(parenthesis, "");
							}else{
								break;
							}
					}}
				}else{break;}//태그가 없을 경우
			}
			System.out.println(" 결과 "+s);
	    	return s;
	    }
	    
	    //문자열 뒤집기
	  public static String reverseString(String s) {
	    return ( new StringBuffer(s) ).reverse().toString();
	  }
	  
	  public static String insertReplace(String str){
	    	
		  str = str.replaceAll("'", "\'");
		  str = str.replaceAll("\"", "\\\"");
	    	return str;
	  }
	  
	  
	  public static String codeReplace(String str){
	    	
		  str = str.replaceAll("'", "''");
		  str = str.replaceAll("\"", "&quot;");
	    	return str;
	  }
	  
	  
	  //HTML  코드로 변환해 준다
	  public static String strTohtml(String s) {
		  if(CommonUtil.isEmptyString(s.trim())){
			  s = "";
		  }else{
			  s = s.trim();
			  s = s.replace("&", "&amp;");
			  s = s.replace("#", "&#35;");
			  s = s.replace("<", "&lt;");
			  s = s.replace(">", "&gt;");
			  s = s.replace("%", "&#37;");
			  s = s.replace("\"", "&quot;");
			  s = s.replace("'", "&39;");
			  s = s.replace(" ", "&nbsp;");
			  s = s.replace("\n", "<br>");
		  }
		  return s;
	  }
	  
	//HTML코드를 문장으로 변환해 준다
	  public static String htmlTostr(String s) {
		  if(CommonUtil.isEmptyString(s.trim())){
			  s = "";
		  }else{
			  s = s.replace("&amp;", "&");
			  s = s.replace("&#35;", "#");
			  s = s.replace("&lt;", "<");
			  s = s.replace("&gt;", ">");
			  s = s.replace("&#37;", "%");
			  s = s.replace("&quot;", "\"");
			  s = s.replace("&39;", "'");
			  s = s.replace("&nbsp;", " ");
			  s = s.replace("<br>", "\n");
		  }
		  return s;
	  }
	  
	  
	  
	  
	  
	  /******************************************************
	   * 이 아래의 function들은 조용욱 빌더 개발하면서 추가한 사항
	   * 소스 Merge 시 공지 필요..반드시!!!! 
	   *******************************************************/
	  
	  /******************************************************
	   * Exp		: sqlInjection 공격 대응을 위한 문자열 검사_
	   *  			  1. 불온한 문자가 Param 문자열에 포함되었는지 검사하고 위반여부를 리턴
	   * @param		: 1. str - 검사할 문자열
	   * @return	: Boolean - 불온 문자 발견 시 false, 정상문자열의 경우 true
	   * @author	: 조용욱
	   * @since		: 2014-03-05
	   */
	  public Boolean isSqlInjection(String str){	  		
		  String[] sql_injection = {"'", "script", "=", "%", "(", ")", "<", ">", "--", "select", "insert", "update", "delete"};
		  str = str.toLowerCase();
		  for(int i = 0; i < sql_injection.length ; i++){
	  		if(str.indexOf(sql_injection[i]) > 0)
	  			return false;
	 		}
	 		return true;
	  }	  
	  
	  /*****************************************************
	   * Exp		: 영문대문자 확인_
	   * 			  1. 공백 제거 후 문자열 길이 0일 경우 확인
	   *  			  2. 영문대문자, 영문소문자, 숫자 Range 여부
	   *  			  3. 특수문자 = {'','','','','','',''} 내의 문자 여부
	   *  			  4. 이외의 문자가 있는 경우 해당위치 리턴
	   * @param		: 1. str - 검사할 문자열
	   * @return	: Boolean - 영문대문자로 이뤄진 경우 true, 아닌경우 false
	   * @author	: 조용욱
	   * @since		: 2014-03-05
	   */
	  public Boolean checkEngBig(String str){
		  str = str.trim();
		  //if(str.length() == 0) return -1;^
	      Pattern p = Pattern.compile("[A-Z]+");
	      Matcher m = p.matcher(str);
	      
	      if(m.find()) return true;
	      else return false;
	  }	  	  
	
	  
	  
}
