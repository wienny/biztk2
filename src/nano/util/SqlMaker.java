package nano.util;

/*****************************************************************
 * @description : SQL 문장을 만들기위한 class
 * 
 * write() : select * from , update table , delete from ..... 무조건적으로 추가
 * where() : where , and 등을 자동적으로 추가 하며 값이 null 이거나 "" 이어도 무조건으로 추가
 * whereIgnore() : where , and 등을 자동적으로 추가 하며 값이 null 이거나 "" 이면 조건에서 제외
 * 
 * $Id: SqlMaker.java,v 1.4 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 10. 2	정수현		최초작성
 *****************************************************************/


public class SqlMaker {
	private boolean isfirst = true;
	private boolean isfirstadd = true;
//	private boolean isfirstset = true;
	private StringBuffer sql;			// sql 부분
	private StringBuffer condition;		// 조건 부분
	private StringBuffer end;			// order by , group by 등  끝 부분

	private StringBuffer fs;	// Insert용 field 저장소.
	private StringBuffer vs;	// Insert용 value 저장소.
	private String	head;
	/** 
	 * Constructer
	 */	
	public SqlMaker()
	{
		sql = new StringBuffer();
		sql.append("\n");

		fs = new StringBuffer();
		vs = new StringBuffer();
		
		condition = new StringBuffer();
		condition.append("\n");

		end = new StringBuffer();
	}


	/** 
	 * reset query - 새로운 query 만들기전에 반드시 실행해야 함
	 */	
	public void reset()
	{
		isfirst = true;
		isfirstadd = true;
//		isfirstset = true;
		sql = new StringBuffer();
		sql.append("\n");
		
		condition = new StringBuffer();
		condition.append("\n");

		end = new StringBuffer();
	}


	/**
	 * 조건이 없는지
	 * @return
	 */
	public boolean isNoCondition(){
		return isfirst;	
	}
	
	/*=============================================================================
	 * 			select
	 ==============================================================================*/
	
	/**
	 * query 부분 무조건 추가
	 * @param value
	 */
	public void write(String value){
		sql.append("  ").append(value).append("\n");
	}

	/**
	 * query 부분 무조건 추가
	 * @param value
	 */
	public void write(int value){
		sql.append("  ").append(value).append("\n");
	}
	
	/*=============================================================================
	 * 			order by, group by
	 ==============================================================================*/
	
	/**
	 * query 마지막 부분 무조건 추가
	 * @param value
	 */
	public void append(String value){
		end.append("  ").append(value).append("\n");
	}

	/**
	 * query 마지막 부분 무조건 추가
	 * @param value
	 */
	public void append(String field, String value){
		end.append("  ").append(field).append("'").append(value).append("'").append("\n");
	}
	
	/*=============================================================================
	 * 			where
	 ==============================================================================*/
	
	/**
	 * where 문장 추가
	 * @param str
	 */
	/*
	public void where (String str){
		sql.append("	where ").append(str).append("\n");
		isfirst = false;
	}
    */
	
	/**
	 * null 이어도 조건 추가
	 * @param value
	 */
	public void where(String param)
	{
		String value = param;
		if (param == null){
			value = "";
		}
        if (isfirst){
        	condition.append(" where ");
            isfirst = false;
        } else {
        	condition.append(" and ");
        }
        condition.append(" ").append(value).append("\n");
    }

	
	/**
	 * 필드의 값이 없어도 "" 로 조건을 추가한다.
	 * 
	 * @param field
	 * @param param
	 */
	
	public void where(String field, String param)
	{
		String value = param;
		if (param == null){
			value = "";
		}
		if (isfirst){
			condition.append(" where ");
			isfirst = false;
		} else {
			condition.append(" and ");
		}
		condition.append(field).append("= '").append(value).append("'").append("\n");
	}

	/**
	 * 필드의 값이 없어도 "" 로 조건을 추가한다.
	 * 
	 * @param field
	 * @param param
	 */
	
	public void whereInt(String field, String param)
	{
		String value = param;
		if (param == null){
			value = "";
		}
		if (isfirst){
			condition.append(" where ");
			isfirst = false;
		} else {
			condition.append(" and ");
		}
		condition.append(field).append("= ").append(value).append("\n");
	}
	
	/**
	 * add(String field, int param) 와 같음
	 * @param field
	 * @param param
	 */
	public void where(String field, int param)
	{
		if (isfirst){
			condition.append(" where ");
			isfirst = false;
		} else {
			condition.append(" and ");
		}
		condition.append(field).append("= ").append(param).append("\n");
	}

	/**
	 * like 등의 특별한 쿼리 작성
	 * null 아나 "" 이어도 조건을 추가한다.
	 * 
	 * @param field 필드명
	 * @param sign < , <= , > , >=
	 * @param param 비교될 값
	 * @param endstring " , '
	 */
	public void where(String field, String sign, String param, String endstring)
	{
		String value = param;
		if (param == null){
			value = "";
		}
		
		if (isfirst){
			condition.append(" where ");
			isfirst = false;
		} else {
			condition.append(" and ");
		}
		condition.append(field).append(sign).append(value).append(endstring).append("\n");;
	}
	

	/**
	 * null 이 아니면 추가
	 * @param value
	 */
	public void whereIgnore(String value)
	{
		if ((value !=null ) && (!value.equals(""))){
            if (isfirst){
            	condition.append(" where ");
                isfirst = false;
            } else {
            	condition.append(" and ");
            }
            condition.append(" ").append(value).append("\n");
        }
    }


	/**
	 * 필드의 값이 없으면 where 조건에서 뺀다.
	 * 
	 * @param field
	 * @param param
	 */
	
	public void whereIgnore(String field, String param)
	{
		if ((param !=null ) && (!param.equals(""))){
			if (isfirst){
				condition.append(" where ");
				isfirst = false;
			} else {
				condition.append(" and ");
			}
			condition.append(field).append("= '").append(param).append("'").append("\n");
		}
	}

	/**
	 * like 등의 특별한 쿼리 작성
	 * param 이 "" 이면 조건에서 뺀다.
	 * 
	 * @param field 필드명
	 * @param sign < , <= , > , >=
	 * @param param 비교될 값
	 * @param endstring " , '
	 */
	public void whereIgnore(String field, String sign, String param, String endstring)
	{
		if ((param !=null) && (!param.equals("") )){
			if (isfirst){
				condition.append(" where ");
				isfirst = false;
			} else {
				condition.append(" and ");
			}
			condition.append(field).append(sign).append(param).append(endstring).append("\n");
		}
	}
	
	/*=============================================================================
	 * 			insert
	 ==============================================================================*/
	
	/**
	 * null 이 아니면 추가함  INSERT 문에서 사용
	 * @param field
	 * @param param
	 */
	
	public void into(String field, String param )
	{
		if ((param !=null )){
			if (isfirstadd){
				isfirstadd = false;
				fs.append(" ( ");
				vs.append(" ( ");
				head = sql.toString();
			} else {
				fs.append(" , ");
				vs.append(" , ");
			}
			fs.append(field);
			vs.append("'"+param+"'");
			sql.setLength(0);//초기화
			sql.append(head).append(fs.toString()).append(" ) VALUES ").append(vs.toString()).append(" )\n");
		}
	}

	/**
	 * null 이 아니면 숫자 데이타  추가함  INSERT 문에서 사용
	 * @param field
	 * @param param
	 */
	public void intoNumber(String field, String param )
	{
		if ((param !=null )){
			if (param.equals("")){
				param = "0";
			}
			if (isfirstadd){
				isfirstadd = false;
				fs.append(" ( ");
				vs.append(" ( ");
			} else {
				fs.append(" , ");
				vs.append(" , ");
			}
			fs.append(field);
			vs.append(param);
			sql.setLength(0);//초기화
			sql.append(head).append(fs.toString()).append(" ) VALUES ").append(vs.toString()).append(" )\n");
		}
	}
	
	/**
	 * null 이 아니면 숫자 데이타  추가함  INSERT 문에서 사용
	 * @param field
	 * @param param
	 */
	public void intoDate(String field, String param )
	{
		if ((param !=null )){
			if (param.equals("")){
				param = "0";
			}
			if (isfirstadd){
				isfirstadd = false;
				fs.append(" ( ");
				vs.append(" ( ");
			} else {
				fs.append(" , ");
				vs.append(" , ");
			}
			fs.append(field);
			vs.append( "= TO_DATE('"+param+"')" );
			sql.setLength(0);//초기화
			sql.append(fs.toString() + " ) VALUES " + vs.toString() + " )\n");
			
		}
	}

	/**
	 * null 이 아니면 숫자 데이타  추가함  INSERT 문에서 사용
	 * @param field
	 * @param param
	 */
	public void intoFunc(String field, String param )
	{
		if ((param !=null )){
			if (param.equals("")){
				param = "0";
			}
			if (isfirstadd){
				isfirstadd = false;
				fs.append(" ( ");
				vs.append(" ( ");
			} else {
				fs.append(" , ");
				vs.append(" , ");
			}
			fs.append(field);
			vs.append(param);
			sql.setLength(0);//초기화
			sql.append(head).append(fs.toString()).append(" ) VALUES ").append(vs.toString()).append(" )\n");
			
		}
	}
	
	
	
	/**
	 * null 이 아니면 추가함  INSERT 문에서 사용
	 * @param field
	 * @param param
	 */
	public void into(String field, int param)
	{
		if (isfirstadd){
			isfirstadd = false;
			fs.append(" ( ");
			vs.append(" ( ");
		} else {
			fs.append(" , ");
			vs.append(" , ");
		}
		fs.append(field);
		vs.append(param);
		sql.setLength(0);//초기화
		sql.append(head).append(fs.toString()).append(" ) VALUES ").append(vs.toString()).append(" )\n");
	}

	
	/*=============================================================================
	 * 			update
	 ==============================================================================*/
	
	/**
	 * null 이 아니면 추가함  UPDATE 문에서 사용
	 * @param field
	 * @param param
	 */
	
	public void set(String field, String param )
	{
		if ((param !=null )){
			if (isfirstadd){
				isfirstadd = false;
				sql.append(" set ");
			} else {
				sql.append(" , ");
			}
			sql.append(field).append("= '").append(param).append("'").append("\n");
		}
	}

	/**
	 * null 이 아니면 숫자 데이타  추가함  UPDATE 문에서 사용
	 * @param field
	 * @param param
	 */
	public void setNumber(String field, String param )
	{
		if ((param !=null )){
			if (param.equals("")){
				param = "0";
			}
			if (isfirstadd){
				isfirstadd = false;
				sql.append(" set ");
			} else {
				sql.append(" , ");
			}
			sql.append(field).append("= ").append(param).append("\n");
		}
	}
	
	/**
	 * null 이 아니면 숫자 데이타  추가함  UPDATE 문에서 사용
	 * @param field
	 * @param param
	 */
	public void setDate(String field, String param )
	{
		if ((param !=null )){
			if (param.equals("")){
				param = "0";
			}
			if (isfirstadd){
				isfirstadd = false;
				sql.append(" ");
			} else {
				sql.append(" , ");
			}
			sql.append(field).append("= TO_DATE('").append(param).append("')\n");
		}
	}

	/**
	 * null 이 아니면 추가함  UPDATE 문에서 사용
	 * @param field
	 * @param param
	 */
	public void set(String field, int param)
	{
		if (isfirstadd){
			isfirstadd = false;
			sql.append(" ");
		} else {
			sql.append(" , ");
		}
		sql.append(field).append("= ").append(param).append("\n");
	}

	/**
	 * null 이 아니면 추가함  UPDATE 문에서 사용
	 * param값에 여러 함수적용시에 사용
	 * @param field
	 * @param param
	 */
	public void set(String field, String str1,String param , String str2)
	{
		if ((param !=null )){
			if (isfirstadd){
				isfirstadd = false;
				sql.append(" ");
			} else {
				sql.append(" , ");
			}
			sql.append(field).append("= ").append(str1).append(param).append(str2).append("\n");
		}
	}

 

	
	/**
	 * query 문장을 리턴
	 * 
	 * @return sql 문장
	 */
	public String getSql()
	{
		return sql.toString() + condition.toString() + end.toString();
	}


	/**
	 * query 문장을 리턴
	 * 
	 * @return sql 문장
	 */
	@Override
	public String toString(){
		return sql.toString() + condition.toString() + end.toString();
	}
	
	
	public String getCondition(){
		return condition.toString() ;
	}
	
	/*  ==================== or  =========================== */

	/**
	 * @param field
	 * @param condition
	 * @param param
	 * @param endstring
	 */
	/*
	public void appendor(String field, String condition, String param, String endstring)
	{
		if ((param !=null ) && (!param.equals(""))){
			if (isfirst){
				sql.append(" where ");
				isfirst = false;
			} else {
				sql.append(" or ");
			}
			sql.append(field).append(condition).append(param).append(endstring);
		}
	}
*/
	/**
	 * @param field
	 * @param param
	 */
	/*
	public void appendor(String field, String param)
	{
		if ((param !=null ) && (!param.equals(""))){
			if (isfirst){
				sql.append(" where ");
				isfirst = false;
			} else {
				sql.append(" or ");
			}
			sql.append(field).append("= '").append(param).append("'");
		}
	}
*/
	/**
	 * @param field
	 * @param param
	 */
	/*
	public void setor(String field, String param)
	{
		if (isfirst){
			sql.append(" where ");
			isfirst = false;
		} else {
			sql.append(" or ");
		}
		sql.append(field).append("= '").append(param).append("'");
	}
*/
	
	/**
	 * @param field
	 * @param param
	 */
/*	
	public void appendor(String field, int param)
	{
		if (isfirst){
			sql.append(" where ");
			isfirst = false;
		} else {
			sql.append(" or ");
		}
		sql.append(field).append("= ").append(param);
	}
*/

	/**
	 * @param value
	 */
/*	
	public void appendor(String value)
	{
		if (isfirst){
			sql.append(" where ");
			isfirst = false;
		} else {
			sql.append(" or ");
		}
		sql.append(" ").append(value);
	}
*/	

	/**
	 * 
	 * 파라미터 값이 null 이어도 "" 로 대체하여 조건에 추가함
	 * @param field
	 * @param value
	 */
	/*
	public void set(String field, String value){

		String str = value;
		if (str == null){
			str = "";
		}
		
		
		if (isfirst){
			sql.append(" where ");
			isfirst = false;
		} else {
			sql.append(" and ");
		}
		sql.append(field).append("= '").append(str).append("'").append("\n");

//		if (isfirstset){
//			isfirstset = false;
//		} else {
//			sql.append(" , \n");
//		}
//		sql.append(field).append("= \'").append(value).append("\'");
	}
	*/

	/**
	 * 파라미터 값이 null 이어도 "" 로 대체하여 조건에 추가함
	 * @param field
	 * @param value
	 */
	/*
	public void set(String field, int value){

		if (isfirst){
			sql.append(" where ");
			isfirst = false;
		} else {
			sql.append(" and ");
		}
		sql.append(field).append("= ").append(value).append("\n");
	}
	*/
	/**
	 * 파라미터 값이 null 이어도 "" 로 대체하여 조건에 추가함
	 * @param field
	 * @param value
	 */
	/*
	public void set(String field, String value, boolean addString){

		String str = value;
		if (str == null){
			str = "";
		}
		
		
		if (isfirst){
			sql.append(" where ");
			isfirst = false;
		} else {
			sql.append(" and ");
		}
		if (addString){
			sql.append(field).append("= '").append(str).append("'").append("\n");
		}else {
			sql.append(field).append("= ").append(str).append("\n");
		}

	}
    */


}
