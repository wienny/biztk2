package nano.dao;


import nano.*;
import nano.util.*;

import java.sql.*;
import java.util.*;

import org.apache.log4j.*;

import sun.misc.BASE64Encoder;

import KISA.SHA256;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class DaoCompanyOrder {
	private static Logger logger = Logger.getLogger(DaoUserOrder.class.getName());
	private Connection conn = null;
	SqlMaker sql = null;
	
	public DaoCompanyOrder(Connection conn){
		this.conn = conn;
		sql = new SqlMaker();
	}
	
	

	/**
	 * CompanyMessage 설정.
	 * 
	 * @param props
	 * @throws BizException
	 */
	public String setCompanyMessage(Properties props) throws BizException {
		String Result = "NO";
		SqlMaker sm = new SqlMaker();
		String jsonParam  = props.getProperty("message_set");

		JSONObject json = (JSONObject) JSONValue.parse(jsonParam);
	
		String explain_id = isCompanyMessage(json);
		if( explain_id.equals("") ) {
			sm.write("INSERT INTO t_explain ");
			sm.into("product_id",(String)json.get("product_id"));
			sm.into("com_id",(String)json.get("com_id"));
			sm.into("com_product_title",(String)json.get("com_product_title"));
			sm.into("com_product_detail",(String)json.get("com_product_detail"));
			Result = "INSERT";
			System.out.println("setCompanyMessage 입력SQL=" + sm.getSql());
		} else {
			sm.write("UPDATE t_explain ");
			sm.set("product_id",(String)json.get("product_id"));
			sm.set("com_id",(String)json.get("com_id"));
			sm.set("com_product_title",(String)json.get("com_product_title"));
			sm.set("com_product_detail",(String)json.get("com_product_detail"));
			sm.where("explain_id",explain_id);
			Result = "UPDATE";
			System.out.println("setCompanyMessage 수정SQL=" + sm.getSql());
			
		}
		try {
			DaoCom.executeUpdate(conn,  sm.getSql());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000",Result+" 오류");
		}
		
		return Result;
	}

	/**
	 * CompanyMessage 확인.
	 * 
	 * @param props
	 * @throws BizException
	 */	
	private String isCompanyMessage(JSONObject json) {
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT explain_id FROM t_explain ");
		sm.write(" WHERE ");
		sm.write(" product_id = '" + (String)json.get("product_id") + "'");
		sm.write(" AND com_id = '" + (String)json.get("com_id") + "'");

		System.out.println("SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			String obj = (String)rec.get("explain_id");
			return ParamUtil.getReqParameter(obj, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Product 정보 조회
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getExplainInfo(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		sm.write(" SELECT * FROM t_explain ");
		sm.where("product_id",props.getProperty("product_id"));
		sm.where("com_id",props.getProperty("com_id"));
		
		System.out.println("getExplainInfo 정보SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","getExplainInfo 오류");
		}
	}
	
	/**
	 * CompanyMessage 설정.
	 * 
	 * @param props
	 * @throws BizException
	 */
	public String setCompanyEmail(Properties props) {
		String Result = "OK";
		SqlMaker sm = new SqlMaker();
	
		ListData list = getCompanyEmail();
		String[] email_list = new String[list.size()];
		
		for(int i=0; i < list.size(); i++)	{
			Record row = list.get(i);
			email_list[i] = row.get("com_email").toString();
		}

		Record rec = getProductInfo(props);
		if(rec == null) {
			rec = new Record();
		}
		
		MailUtil mail = new MailUtil();	
		mail.setCompanyEmail(email_list, rec, props);
		return Result;
	}
	
	
	/**
	 * Company(도장) email 조회
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public ListData getCompanyEmail() {
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		sm.write(" SELECT * FROM t_company ");
		sm.write(" WHERE com_type='1' ");
		
		System.out.println("getCompanyEmail 정보SQL=" + sm.getSql());
		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	
	
	/**
	 * Company명 조회
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public String getCompanyName(Properties props) {
		SqlMaker sm = new SqlMaker();
		String com_id = props.getProperty("com_id");
		sm.write(" SELECT com_name FROM t_company ");
		sm.write(" WHERE com_id='"+ com_id + "'");
		
		System.out.println("getCompanyName 정보SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			return rec.get("com_name","").toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}		


	/**
	 * Product 정보 조회
	 * 
	 * @param props
	 * @return
	 * @throws BizException
	 */
	public Record getProductInfo(Properties props) {
		SqlMaker sm = new SqlMaker();
		String product_id = props.getProperty("product_id");
		sm.write(" SELECT * FROM t_product ");
		sm.write(" WHERE product_id='" + product_id + "'");
		
		System.out.println("getProductInfo 정보SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn, sm.getSql());
			return rec;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}		
	
}
