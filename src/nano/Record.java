package nano;


import java.util.*;

/*****************************************************************
 * @description : ListData 의 한 Record 저장하기위한 class
 * $Id: Record.java,v 1.1 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 12. 20	정수현		최초작성
 *****************************************************************/
public class Record extends Hashtable {
	private static final long serialVersionUID = 1L;
	
	public Object get(Object key){
		if (containsKey(key)) {
			return super.get(key);
		}
		//System.out.println("Warn.. " + key + " 값이 없습니다");
		return "";
		
	}
	
	public String get(String key, String defaultValue){
		String value = (String)get(key);
		if ( value == null || value.equals("")) {
			return defaultValue;
		}	else {
			return value;
		}
	}
}
