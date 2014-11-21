package nano.util;

/**
 * 이 클래스는 <strong>공통 유틸리티 Method</strong>를 제공하는 클래스이다.
 * <BR>
 * @author 	Samsung SDS / Information Service Team
 * @version	1.0
 * @since 2003.10.23
 */
	
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class CommonUtil {
	   /** 
		* String 클래스에 값이 존재하는지를 체크 
		* @param  String Empty인지 체크할 String
		* @return boolean(true : null 또는 빈 스트링객체, false : true 이외의 경우)
		*/
	   public static boolean isEmptyString(String arg1) {
		  if(arg1 == null || arg1.length() == 0) return true;
		  else return false;
	   }

	   /** 
		* String의 값이 null인경우 Empty String을 리턴 
		* @param  String Source String
		* @return arg1이 null인 경우 Empty String을 리턴
		*/
	   public static String getNotNullString(String arg1) {
		  if(arg1 == null) return "";
		  else return arg1;
	   }

	   /** Object의 Length를 Return한다.
		*  String 또는 StringBuffer의 경우 : length() 리턴
		*  List 또는 Map의 경우 size() 리턴
		*  Array의 경우 length 리턴
		*  null 인경우 0을 리턴
		*  그 이외의 경우 1을 리턴
		*/
	   public static int getLength(Object obj) throws Exception {
		  if(obj == null) {
			 return 0;
		  }
		  else if(obj instanceof String || obj instanceof StringBuffer) {
			 return ((String)obj).length();
		  }
		  else if(obj instanceof List || obj instanceof Map) {
			 return ((List)obj).size();
		  }
		  else if(obj.getClass().isArray()) {
			 return Array.getLength(obj);
		  }
		  return 1;
	   }

	   /**
		* 현재날짜를 지정된 포멧에 맞게 String 형태로 리턴해준다
		* @param argFormat java.lang.String
		* @return String
		* @exception TException
		* Example
		*      Format Pattern                         Result
		*     --------------                         -------
		*     "yyyy.MM.dd G 'at' HH:mm:ss z"    ->>  1996.07.10 AD at 15:08:56 PDT
		*     "EEE, MMM d, ''yy"                ->>  Wed, July 10, '96
		*     "h:mm a"                          ->>  12:08 PM
		*     "hh 'o''clock' a, zzzz"           ->>  12 o'clock PM, Pacific Daylight Time
		*     "K:mm a, z"                       ->>  0:00 PM, PST
		*     "yyyyy.MMMMM.dd GGG HH:mm aaa"    ->>  1996.July.10 AD 12:08 PM
		*/
	   public static String getDateFormat(String argFormat) throws Exception {
		  if(argFormat == null || argFormat.length() == 0) throw new Exception("날짜 포멧설정 오류");

		  String returnStr = null;
		  DateFormat df = new SimpleDateFormat(argFormat);
		  Calendar cal = Calendar.getInstance();
		  returnStr = df.format(cal.getTime());

		  return returnStr;
	   }
	   
	   /**
	    * long 타입으로 반환되는 값은 1970년1월1일00:00:00을 시작으로하는 millisecond단위 값
	    *
	    * @return
	    */
	   public static long getMilliseconds() {
//		millisecond 로 변환 
		Calendar c1 = Calendar.getInstance(); 
 		return c1.getTime().getTime(); 
	  }

	   /** 현재날짜를 yyyyMMdd Format으로 리턴 */
	   public static String getDate() throws Exception {
		  return getDateFormat("yyyyMMdd");
	   }

	   /** 현재날짜를 yyyy-MM-dd Format으로 리턴 */
	   public static String getDashedDate() throws Exception {
		  return getDateFormat("yyyy-MM-dd");
	   }

	   /** yyyyMMdd 형태의 String을 yyyy-MM-dd Format으로 리턴 */
	   public static String getDashedDate(String strDate) {
		  if(strDate == null || strDate.length() < 8) return strDate;
		  return strDate.substring(0,4)+"-"+strDate.substring(4,6)+"-"+strDate.substring(6,8);
	   }

		/** 현재날짜시간을 yyyyMMddhhmmss Format으로 리턴 */
	   public static String getDateTime() throws Exception {
		  return getDateFormat("yyyyMMddHHmmss");
	   }
	   
	   /** 현재날짜시간을 milliseconds 로 리턴 */
	   
	   /** '-'로 구분된 전화번호를 '-'를 제외한 4개의 String Array로 리턴한다.
		*  Token이 부족한 경우 뒤쪽으로 정렬하여 Array를 채운다.
		*/
	   public static String[] split4PhoneNumber(String strphone) {
		  int fieldlen = 4;
		  String[] rtnStr = new String[fieldlen];
		  if(strphone != null) {
			 StringTokenizer st = new StringTokenizer(strphone, "-");
			 int count = fieldlen-st.countTokens();
			 while(st.hasMoreTokens()) {
				rtnStr[count] = st.nextToken();
				count++;
			 }
		  }
		  return rtnStr;
	   }

	   /** StringBuffer에 있는 값중 New Line에 해당되는 값을 지운다
		* @param  java.lang.StringBuffer
		* @return void
		* @exception Exception
		*/
	   public static void removeNewLine(StringBuffer sb) throws Exception {
		  if(sb == null) throw new Exception("StringBuffer 파라미터 오류(Null)");

		  char ch;
		  int i, sblen = sb.length();
		  for(i=0;i<sblen;i++) {
			 if((ch = sb.charAt(i)) == '\n' || ch == '\r') {
				sb.deleteCharAt(i);
				i--;
				sblen--;
			 }
			 else if(ch == '"') {
				sb.setCharAt(i, '\'');
			 }
		  }
	   }
   
	   /**
		* String에 있는 값중 New Line에 해당되는 값을 지운다
		* @param  String
		* @return String
		*/
	   public static String removeNewLine(String s)
	   {
		  StringBuffer strBf = new StringBuffer(s);
		  String s1  = new String();
		  try
		  {
			   removeNewLine(strBf);
			  s1 = strBf.toString();
		  }catch(Exception e)
		  {
			  System.out.println("String Conversion Error");
		  }
		  return s1;
	   }

	   /**
		* String에 있는 값중 (',", ,-,_)에 해당되는 값을 지운다
		* @param  String
		* @return String
		*/
	   public static String removeSearchSpacialChars(String s)
	   {
		  if(s != null) {
		   StringBuffer sb = new StringBuffer(s);
			 char ch;
			 int i, sblen = sb.length();
			 for(i=0;i<sblen;i++) {
				if((ch = sb.charAt(i)) == '\'' || 
					ch == '-' ||
					ch == '"' ||
					ch == ' ' ||
					ch == '_') {
				   sb.deleteCharAt(i);
				   i--;
				   sblen--;
				}
			 }
		   return sb.toString();
		}
		return "";
	   }


	   /** 
		* StringBuffer에 있는 값중 New Line에 해당되는 값 <BR>로 변경
		* @param java.lang.StringBuffer
		* @return void
		* @exception Exception
		*/
	   public static void convertNewLine(StringBuffer sb) throws Exception {
		  if(sb == null) throw new Exception("StringBuffer 파라미터 오류(Null)");

		  char ch;
		  int i, sblen = sb.length();
		  for(i=0;i<sblen;i++) {
			 if((ch = sb.charAt(i)) == '\n') {
				sb.deleteCharAt(i);
				sb.insert(i, "<br>");
				i+=3;
				sblen+=3;
			 }
			 else if(ch == '\r') {
				sb.deleteCharAt(i);
				i--;
				sblen--;
			 }
			 else if(ch == '"') {
				sb.setCharAt(i, '\'');
			 }
		  }
	   }

	   /** 
		* str에 있는 값중 Double Quote(") 앞에 Escape문자(\)를 추가
		* @param  String
		* @return String
		*/
	   public static String convertDoubleQt(String str) {
		  if(str == null)
			 return str;

		  char ch;
		  StringBuffer stb = new StringBuffer();
		  stb.append(str);

		  int i, sblen = stb.length();
		  for(i=0;i<sblen;i++) {
			 if((ch = stb.charAt(i)) == '\"')
			 {
				stb.insert(i, "\\");
				i+=1;
				sblen+=1;
			 }
		  }
		  return stb.toString();
	   }

	   /** 
		* str에 있는 값중 Single Quote(') 앞에 Escape문자(\)를 추가
		* @param  String
		* @return String
		*/
	   public static String convertSingleQt(String str) {
		  if(str == null)
			 return str;

		  char ch;
		  StringBuffer stb = new StringBuffer();
		  stb.append(str);

		  int i, sblen = stb.length();
		  for(i=0;i<sblen;i++) {
			 if((ch = stb.charAt(i)) == '\'')
			 {
				stb.insert(i, "\'");
				i+=1;
				sblen+=1;
			 }
		  }
		  return stb.toString();
	   }
	   /** 
		* Source String에 지정된 길이를 만족할때까지 특정문자로 채워서 리턴
		* @param srcString  String
		* @param dstLength  int
		* @param appendchar char
		* @return String
		*/
	   public static String Rpad(String srcString, int dstLength, char appendchar) {
		  int srcLength = (srcString == null)?0:srcString.length();
		  int appendLength = dstLength - srcLength;
		  if(appendLength > 0) {
			 int i;
			 char [] newchar = new char[dstLength];
			 srcString.getChars(0, srcLength, newchar, 0);
			 for(i=0;i<appendLength;i++) {
				newchar[srcLength+i] = appendchar;
			 }
			 return new String(newchar);
		  }
		  else return srcString;
	   }

	   /**
	   * \n, \r 등을 <br>테그로 변경시킨다.<br>
	   * @param comment  변경시킬 문자열
	   * @return String  변경된 문자열
	   */
	   public static String toBr(String comment) {
		  if( comment==null || comment.equals("") || comment.equals(null) ) {
			 return "";
		  } else {
			 int length = comment.length();
			 StringBuffer buffer = new StringBuffer();
			 for(int i=0;i<length;++i) {
				String comp = comment.substring(i,i+1);
				if ("\r".compareTo(comp) == 0) {
				   comp = comment.substring(++i,i+1);
				   if ("\n".compareTo(comp) == 0) buffer.append("<br>\r");
				   else buffer.append("\r");
				}
				buffer.append(comp);
			 }
			 return buffer.toString();
		  }
	   }





	   /** 문자열에서 <script> 테그를 제거하여 리턴  */
	   public static String convertContents(String s)
	   {
		StringBuffer strBf = new StringBuffer(s);
		String s1  = new String();
		try
		{
			convertNewLine(strBf);
			s1 = strBf.toString();
			String tmp = s1.toLowerCase();
			if(tmp.indexOf("<script") >= 0) 	
			   s1 = replaceIgnoreCase(s1, "<script", "&lt;script");
			if(tmp.indexOf("</script") >= 0)  
			   s1 = replaceIgnoreCase(s1, "</script", "&lt;/script");
			if(tmp.indexOf("< /script") >= 0) 
			   s1 = replaceIgnoreCase(s1, "</ script", "&lt;/script");
		}catch(Exception e)
		{
			System.out.println("String Conversion Error");
		}
		return s1;
	   }
	   /** <textarea>에 표시하기 위한 값으로 변경하여 리턴  */
	   public static void convertNewLine_Textarea(StringBuffer sb) throws Exception {
			 if(sb == null) throw new Exception("StringBuffer 파라미터 오류(Null)");

			 char ch;
			 int i, sblen = sb.length();
			 for(i=0;i<sblen;i++) {
				if((ch = sb.charAt(i)) == '\n') {
				   sb.deleteCharAt(i);
				   sb.insert(i, "\\n");
				   i+=1;
				   sblen+=1;
				}
				else if(ch == '\r') {
				   sb.deleteCharAt(i);
				   i--;
				   sblen--;
				}
				else if(ch == '"') {
				   sb.setCharAt(i, '\'');
				}
			 }
	   }
	   /** 
		* 문자열 특정 문자열을 대치한 후 리턴  
		* @param String Source String
		* @param String 변경전 문자열(FROM)
		* @param String 변경후 문자열(TO)
		* @return String 
		*/
	   public static String replaceIgnoreCase(String source, String pattern, String replace) 
	   { 
		   int sIndex = 0;
		   int eIndex = 0;
		   String sourceTemp = null;
		   StringBuffer result = new StringBuffer();    
		   sourceTemp = source.toUpperCase();
		   while ((eIndex = sourceTemp.indexOf(pattern.toUpperCase(), sIndex)) >= 0) 
		   { 
			   result.append(source.substring(sIndex, eIndex)); 
			   result.append(replace); 
			   sIndex = eIndex + pattern.length(); 
		   } 
		   result.append(source.substring(sIndex)); 
		   return result.toString(); 
	   } 

	   /**
		* Request 객체에 포함된 쿠키중 name과 일치하는 한개를 String으로 리턴해준다
		*/
	   public static String getCookie(javax.servlet.http.HttpServletRequest request, String name) throws Exception {
		  if(request == null) throw new Exception("HttpServletRequest 파라미터가 Null 입니다");
		  if(name == null) throw new Exception("쿠키명 파라미터가 Null 입니다");

		String value = "";
		javax.servlet.http.Cookie[] cook = request.getCookies();
   	
		if(cook != null) {
		  for(int i=0;i < cook.length; i++) { 
			 if(cook[i].getName().equals(name)) {
				value = cook[i].getValue();
				break;
			 }
		  }
		}
   		
		return value;
	   }
   
	   /** 
		* StringBuffer를 일정한 길이(len) 단위로 줄바꿈
		* @param java.lang.StringBuffer
		* @return void
		* @exception Exception
		*/
	   public static void autoLine(StringBuffer sb, int len) throws Exception {
		  if(sb == null) throw new Exception("StringBuffer 파라미터 오류(Null)");

		  char ch;
		  int i, sblen = sb.length();
		  for(i=0; i<sblen; i++) {
			 if((i % len) == 0) {
				sb.insert(i, "<br>");
				i+=3;
				sblen+=3;
			 }
		  }
	   }   

	   /** 
		* 2개의 ArrayList를 int값으로 크기 비교할 Method에 의해서 비교한후 
		* 새로 순서를 정렬하여 두개의 ArrayList를 합한다.
		* @param ArrayList 첫번째 정렬대상 ArrayList
		* @param ArrayList 두번째 정렬대상 ArrayList
		* @param Object ArrayList에 존재하는 비교대상 class
		* @param String 크기 비교할 Method명
		* @return 새로 정렬되여 한개로 합해진 ArrayList
		* @exception Exception
		*/
	   public static ArrayList mergeSortArrayList(ArrayList arr1, ArrayList arr2, Object objVO, String methodName) {
		  ArrayList prvlist = null;
		  if(arr1 == null || arr1.size() == 0) {
			 prvlist = arr2;
		  }
		  else if(arr2 == null || arr2.size() == 0){
			 prvlist = arr1;
		  }
		  else {
			 boolean blastloop = false;
			 int j=0, k, arr2seq;
			 int arr2size = arr2.size();
			 int arr1size = arr1.size();
			 int arr1seq = getObjSeqValue(arr1.get(0), methodName);
			 for(int i=0;i<arr2size && !blastloop;i++) {
				arr2seq = getObjSeqValue(arr2.get(i), methodName);
				while(arr2seq > arr1seq) {
				   arr2.add(i++, arr1.get(j++));
				   arr2size++;
				   if(j<arr1size) {
					  arr1seq = getObjSeqValue(arr1.get(j), methodName);
				   }
				   else {
					  blastloop = true;
					  break;
				   }
				}
			 }
			 for(k=j;k<arr1size;k++) {
				arr2.add(arr1.get(k));
			 }
			 prvlist = arr2;
		  }
		  return prvlist;
	   }
	   /**
		* Object에서 지정된 Method를 호출하여 그 결과를 return한다.
		* 단, 결과는 int 값인 경우로 한정한다.
		* @param  Object 값을 호출할 Object
		* @param  String Object에서 호출할 Method명
		* @return 호출된 결과 값 int
		*/
	   public static int getObjSeqValue(Object obj, String methodName) {
		  int iRtn = -1;
		  try {		  	
			 iRtn = ((Integer)(obj.getClass().getMethod(methodName, new Class[]{}).invoke(obj, new Object[]{}))).intValue();
		  }
		  catch(Exception e) {}
		  return iRtn;
	   }
	   
	public static String itemReplace(String s, String search, String replace)  
	{
            
		StringBuffer s2 = new StringBuffer();     
		int i = 0;     
		int j = 0;
		int sl = search.length();
 
		while (j > -1) {
			j = s.indexOf(search,i);         
			if (j > -1)  {
				s2.append (s.substring(i,j)+replace);              
				i = j + sl;
			}         
		}     
		s2.append(s.substring(i,s.length()));
		return s2.toString();
	}
	
	/**
	   * parameter로 넘어온 string변수가 null이면
	   * 공백없는 string값을 넘긴다.
	   <br> Usage : UtilFormat.replaceNull(value);
   
		 @param value nulltrim 을 하고자 하는 value
		 @return String nulltrim 된 value
	   */     
	public static String replaceNull(String value) 
	{
		if(value == null || value.equals("")) 
			return (new String(""));
		else return(value.trim());
	}

  /**
   * parameter로 넘어온 long변수가 null이면
   * 공백없는 string값을 넘긴다.
	 @param value nulltrim 을 하고자 하는 value
	 @return long nulltrim 된 value
   */        
		public static long replaceNull(long value) 
		{
			Long tmpLong = new Long(value);
			if(tmpLong == null) 
				return (0);
			else return(value);
		}
   
	  /**
   * parameter로 넘어온 long변수가 null이면
   * 공백없는 string값을 넘긴다.
	 @param value nulltrim 을 하고자 하는 value
	 @return long nulltrim 된 value
   */        
		public static int replaceNull(int value) 
		{
			Integer tmpInt = new Integer(value);
			if(tmpInt == null) 
				return (0);
			else return(value);
		}
   
	  /**
   * parameter로 넘어온 double변수가 null이면
   * 공백없는 string값을 넘긴다.
	 @param value nulltrim 을 하고자 하는 value
	 @return double nulltrim 된 value
   */   
	public static double replaceNull(double value) 
	{
		Double tmpDouble = new Double(value);
		if(tmpDouble == null) 
			return (0);
		else return(value);
	}
	
	/*
	 * 2014-03-26 이후 추가.
	 * 
	 */
	
	
	/**
	 * 체크박스 체크(change 이벤트) 시 값이 default 'on' 으로 오는 경우 1로 값 바꿔줌.
	 * @param checkbox value 값.
	 * @return 조건에 의해 변경된 값.
	 */	
    public static String getCheckBoxValue(String str, String type) {
    	if(str.equals("on"))
    		str = "1";
    	
    	return str;
    }	
	

}