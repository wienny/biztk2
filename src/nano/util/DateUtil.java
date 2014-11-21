package nano.util;

import java.text.*;
import java.util.*;


 /**
 * @description : 날짜 관련 함수 class 
 *
 *  <pre>
 *  ${Id}
 *  ${Revision}
 *
 *  1. 화일 이름  :    DateUtil.java
 *  2. 작성자     :    한성확
 *  3. 프로그램 변경 내역
 *      1) 2007. 10. 30 / 최초 작성
 *
 *  추가설명 :
 *
 *  </pre>
 */
public class DateUtil {

	public static final String DDMT = "-";  // 날짜 구분 문자
	public static final String MDMT = "/";  // 날짜 구분 문자
	public static final String HDMT = ":";  // 시간 구분 문자

	/**
	 * 현재 연도를 4자리로 구한다. ex) "2006"
	 * 
	 * @return
	 */
	public static int getYear() {
		return getNumberByPattern("yyyy");
	}

	/**
	 * 이번달을 숫자 형태로 구한다.
	 * 
	 * @return
	 */
	public static int getMonth() {
		return getNumberByPattern("MM");
	}
	
	
	/**
	 * 오늘 날자를 숫자형태로 구한다.
	 * 
	 * @return
	 */
	public static int getDay() {
		return getNumberByPattern("dd");
	}
	/**
	 * 오늘 날자를 숫자형태로 구한다.
	 * 
	 * @return
	 */
	public static int getToday() {
		return getNumberByPattern("yyyyMMdd");
	}
	
	public static int getTomonth() {
		return getNumberByPattern("yyyyMM");
	}
	
	/**
	 * 오늘 날자를 숫자형태로 구한다.
	 * 
	 * @return
	 */
	public static int getTime() {
		return getNumberByPattern("HHmmss");
	}
	
	
	/**
	 * 날짜 표시 패턴을 받아 숫자형태로 구한다. 
	 * 
	 * @param pattern
	 * @return
	 */
	public static int getNumberByPattern(String pattern) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (pattern, java.util.Locale.KOREA);
		String dateString = formatter.format(new java.util.Date());
		return Integer.parseInt(dateString);
	}

	
	/**
	 * 현재 연도를 4자리로 구한다 ex) "2006"
	 * 
	 * @return
	 */
	public static String getYYYY() {
		return getStrByPattern("yyyy");
	}

	/**
	 * 현재 연도를 2자리로 구한다 ex) "06"
	 * 
	 * @return
	 */
	public static String getYY() {
		return getStrByPattern("yy");
	}

	public static String getMM() {
		return getStrByPattern("MM");
	}

	public static String getDD() {
		return getStrByPattern("dd");
	}
	
	public static String getYYYYMMDDHHMMSS() {
		return getStrByPattern("yyyyMMddHHmmss");
	}

	public static String getYYYYMMDD() {
		return getStrByPattern("yyyyMMdd");
	}

	public static String getYYYYMM() {
		return getStrByPattern("yyyyMM");
	}

	public static String getDate() {
		return getStrByPattern("yyyy/MM/dd HH:mm:ss");
	}
	/**
	 * 날짜 표시 패턴을 받아 문자열로 구한다.
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getStrByPattern(String pattern) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (pattern, java.util.Locale.KOREA);
		String dateString = formatter.format(new java.util.Date());
		return dateString;
	}
	

    /**
     * 현재를 기준으로 몇일 후(전) 계산 <br>
     * (주의)입력날짜는 구분자가 있는 string형  <br>
     * @param str
     * @param term
     * @return
     */
    public static String getTarget_Date(String str, int term)
    {
        SimpleDateFormat fmt= new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.KOREA);

        if ((str == null) || (str.length() !=10)){
        	return "";
        }
        
        int year = Integer.valueOf(str.substring(0,4)).intValue();
        int month = Integer.valueOf(str.substring(5,7)).intValue();
        int day = Integer.valueOf(str.substring(8,10)).intValue();

        java.util.Calendar aCal = java.util.Calendar.getInstance();

    	aCal.set(year,month-1,day+term);
        return fmt.format(aCal.getTime());
    }
	

    /**
     * 
     * @param year
     * @param month
     * @param day
     * @return 
     */
	public static int getFirstMonth(int year, int month, int day) {
		Calendar temp_date = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		
		temp_date = Calendar.getInstance();
		temp_date.set(Calendar.YEAR, year);
		temp_date.set(Calendar.MONTH, month-1);
		temp_date.set(Calendar.DATE, 1);
		
		cal = temp_date;
//		int Year  = cal.get(Calendar.YEAR);
//		int Month = cal.get(Calendar.MONTH)+1;
//		int Day   = cal.get(Calendar.DATE);
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}
	
	/**
	 * 년월을 입력받아 날자를 리턴
	 * @param year
	 * @param month
	 * @return Day
	 */
	public static int getDaysInMonth(int year, int month) {
		Calendar temp_date = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		
		
		temp_date = Calendar.getInstance();
		temp_date.set(Calendar.YEAR, year);
		temp_date.set(Calendar.MONTH,month);
		temp_date.set(Calendar.DATE,0);
		
		cal = temp_date;
		int Year  = cal.get(Calendar.YEAR);
		int Month = cal.get(Calendar.MONTH);
		int Day   = cal.get(Calendar.DATE);
		
		return Day;
	}

	
	
	/**
	 *  날짜와 표시포멧을 받아 문자열 형태로 구한다. ("yyyy-MM-dd HH:mm:ss")
	 *  
	 */
	public static String getDateFormat(Date d, String format){
		SimpleDateFormat fmt = new SimpleDateFormat(format); 
		if (d == null){
			return "";
		}
		return fmt.format(d);
		
	}
	
	
	/**
	 *  날짜 포멧의 문자열을 문자열 형태로 구한다. ("yyyy-MM-dd HH:mm:ss" --> "yyyyMMddHHmmss" )
	 *  
	 */
	public static String getTextdate(String dateText){
		String sValue = (String)dateText;
		StringBuffer sb = new StringBuffer();
		if (sValue == null) {
			return "";
		}

		sValue = sValue.replace(":", "");
		sValue = sValue.replace("-", "");
		sValue = sValue.replace(" ", "");
		sValue = sValue.replace(".", "");
		
		if(dateText.length() > 14){
			sValue = sValue.substring(0, 14);
		}
		sb.append(sValue);
			
		return sb.toString();
	}
	
	/**
	 * 시간 포함된 문자열을 받아 날짜만 리턴한다. "yyyyMMddHHmmss" --> "yyyy-MM-dd"
	 * @param org
	 * @return
	 */
	public static String time2Date(Object org){
		String sValue = (String)org;
		StringBuffer sb = new StringBuffer();
		if (org == null) {
			return "";
		}

		switch(sValue.length()){
			case 8:
				sb.append(sValue.substring(0,4)).append(DDMT);
				sb.append(sValue.substring(4,6)).append(DDMT);
				sb.append(sValue.substring(6,8));
				break;
			case 10:
				sb.append(sValue.substring(0,4)).append(DDMT);
				sb.append(sValue.substring(4,6)).append(DDMT);
				sb.append(sValue.substring(6,8)).append(" ");
				sb.append(sValue.substring(8,10));
				break;
			case 12:
				sb.append(sValue.substring(0,4)).append(DDMT);
				sb.append(sValue.substring(4,6)).append(DDMT);
				sb.append(sValue.substring(6,8)).append(" ");
				sb.append(sValue.substring(8,10)).append(HDMT);
				sb.append(sValue.substring(10,12));
				break;
			case 14:
				sb.append(sValue.substring(0,4)).append(DDMT);
				sb.append(sValue.substring(4,6)).append(DDMT);
				sb.append(sValue.substring(6,8)).append(" ");
				sb.append(sValue.substring(8,10)).append(HDMT);
				sb.append(sValue.substring(10,12)).append(HDMT);
				sb.append(sValue.substring(12,14));
				break;
			
			default :
				sb.append(sValue);
				break;
		}
		return sb.toString();
	}
	
	/**
	 * 시간 포함된 문자열을 받아 년/월/일 리턴한다. "yyyyMMddHHmmss" --> "yyyy-MM-dd"
	 * @param org
	 * @return
	 */	
	public static String time2YYYYMMDD(Object org){
		String sValue = (String)org;
		StringBuffer sb = new StringBuffer();
		if (org == null) {
			return "";
		}

		switch(sValue.length()){

		case 10:
			sb.append(sValue.substring(0,4)).append(DDMT);
			sb.append(sValue.substring(4,6));
			break;
		case 12:
			sb.append(sValue.substring(0,4)).append(DDMT);
			sb.append(sValue.substring(4,6)).append(DDMT);
			sb.append(sValue.substring(6,8));
			break;
		case 14:
			sb.append(sValue.substring(0,4)).append(DDMT);
			sb.append(sValue.substring(4,6)).append(DDMT);
			sb.append(sValue.substring(6,8));
		break;
			
		default :
			sb.append(sValue);
			break;
	}
	return sb.toString();
}
	
	/**
	 * 시간 포함된 문자열을 받아 월/일만 리턴한다. "yyyyMMddHHmmss" --> "MM/dd"
	 * @param org
	 * @return
	 */		
	public static String time2MMDD(Object org){
		String sValue = (String)org;
		StringBuffer sb = new StringBuffer();
		if (org == null) {
			return "";
		}

		if (sValue.length() >7){
			sb.append(sValue.substring(4,6)).append(MDMT);
			sb.append(sValue.substring(6,8));
			
		}
		return sb.toString();
		
	}
	
	/**
	 * 시간 포함된 문자열을 받아 월/일만 리턴한다. "yyyyMMddHHmmss" --> "MM/dd"
	 * @param org
	 * @return
	 */		
	public static String time2DD(Object org){
		String sValue = (String)org;
		StringBuffer sb = new StringBuffer();
		if (org == null) {
			return "";
		}

		if (sValue.length() >7){
			//sb.append(sValue.substring(4,6)).append(MDMT);
			sb.append(sValue.substring(6,8));
			
		}
		return sb.toString();
		
	}
	
	
	public static String yearSelect(String name){
		StringBuffer sb = new StringBuffer();
		sb.append("\n<select name=\"" + name + "\" size=\"1\">\n");
		for (int i=2006 ;i < (DateUtil.getYear()+1); i++) {
        	sb.append("  <option value=\"" + i + "\">" + i + "</option>\n");    
		}
        sb.append("</select>\n");  
		return sb.toString();
	}

	public static String monthSelect(String name){
		StringBuffer sb = new StringBuffer();
		sb.append("\n<select name=\"" + name + "\" size=\"1\">\n");
		for (int i=1 ;i <= 12; i++) {
        	sb.append("  <option value=\"" + StrUtil.fillZero(i, 2) + "\">" + i + " 월 </option>\n");    
		}
        sb.append("</select>\n");  
		return sb.toString();
	}
	

	
	/**
	 * 문자형 날짜 값을 받아 DATE형태로 구한다. 
	 * 
	 * @param pattern
	 * @return
	 */
	public static void stringTodate(String textDate){
		  try {
		   // 입력할 날짜의 문자열이 yyyy-MM-dd 형식이므로 해당 형식으로 포매터를 생성한다.
		   java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   //SimpleDateFormat.parse()메소드를 통해 Date객체를 생성한다.
		   //SimpleDateFormat.parse()메소드는 입력한 문자열 형식의 날짜가
		   //포맷과 다를경우 java.text.ParseException을 발생한다.
		   java.util.Date date = format.parse(textDate);

		   //위에서 만든 date객체가 정말 7월22일인지 확인 해보자.
		   java.text.SimpleDateFormat format1 = new java.text.SimpleDateFormat("yyyy년MM월dd일 HH시mm분ss초");
		   String dateString = format1.format(date);
		   //Date객체의 날자를 확인한다..  결과 : 2007년07월22일 00시00분00초
		   System.out.println(dateString);
		  } catch (java.text.ParseException ex) {
		   ex.printStackTrace();
		   System.out.println(ex);
		  }
		  
	}
	
	/**
	 * 문자형 날짜 값을 받아 남은 일자를 return. 
	 * 
	 * @param pattern
	 * @return String
	 */
	public static int startToendDate(String txtDate){
		  try {
			   Date todate = new Date();
			   
			   java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
			   java.util.Date eDate = format.parse(txtDate);
			   
			   Calendar sCal = Calendar.getInstance();
			   Calendar eCal = Calendar.getInstance();
			   
			   sCal.setTime(todate);
			   eCal.setTime(eDate);
			   
			   int count = 0;
	
			   while (!sCal.after(eCal)) {
				   count++;
				   sCal.add(Calendar.DATE, 1);
			   }
			   //System.out.println("count>>"+count);
			   return count;
		   
		  } catch (java.text.ParseException ex) {
		   ex.printStackTrace();
		  }
		return 0;
		  
	}
	
	/**
	 * DATE형 날짜 값을 받아 해외 Format 으로 return.
	 * 
	 * @param pattern
	 * @return String
	 */
	public static String foreignDate(String reg_date){
		StringBuffer sb = new StringBuffer();
		int check_month = 0;
		
		
		  try {
			  if (reg_date == "") {
					return "";
			  }
			  check_month = Integer.parseInt(reg_date.substring(5, 7));
			  
			  
			  switch(check_month){

				case 1:
					sb.append("Jan").append(" ");
					break;
				case 2:
					sb.append("Feb").append(" ");
					break;
				case 3:
					sb.append("Mar").append(" ");
					break;
				case 4:
					sb.append("Apr").append(" ");
					break;
				case 5:
					sb.append("May").append(" ");
					break;
				case 6:
					sb.append("Jun").append(" ");
					break;
				case 7:
					sb.append("Jul").append(" ");
					break;
				case 8:
					sb.append("Aug").append(" ");
					break;
				case 9:
					sb.append("Sep").append(" ");
					break;
				case 10:
					sb.append("Oct").append(" ");
					break;
				case 11:
					sb.append("Nov").append(" ");
					break;
				case 12:
					sb.append("Dec").append(" ");
					break;
				default :
					sb.append(check_month);
					break; 
			  	}
			sb.append(reg_date.substring(0, 4));
		  } catch (Exception ex) {
		   ex.printStackTrace();
		  }
		return sb.toString();
	}
	
	
	
}

