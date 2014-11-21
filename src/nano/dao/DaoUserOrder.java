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


public class DaoUserOrder {
	private static Logger logger = Logger.getLogger(DaoUserOrder.class.getName());
	private Connection conn = null;
	SqlMaker sql = null;
	
	public DaoUserOrder(Connection conn){
		this.conn = conn;
		sql = new SqlMaker();
	}
	
	

	
	/**
	 * UserOrder 설정.
	 * 
	 * @param props
	 * @throws BizException
	 */
	public String setUserOrder(Properties props) throws BizException {
		String Result = "NO";
		SqlMaker sm = new SqlMaker();
		String jsonParam  = props.getProperty("order_set");

		JSONObject json = (JSONObject) JSONValue.parse(jsonParam);
	
		String order_id = "";
		String com_id = "";
		String user_id = "";
		
		String com_type = (String)json.get("com_type");
				
		if(com_type.equals("0") || com_type.equals("1")) {
			com_id = (String)json.get("com_id");
			user_id = "";
		}
		else if(com_type.equals("3")) {
			user_id = (String)json.get("com_id");			
			com_id = getComIdByUserId(user_id);
		}
		
		//System.out.println("com_id:"+com_id);
		//System.out.println("user_id:"+user_id);
		
		order_id = isUserOrder(json, com_id, user_id);
			
			
		
		if( order_id.equals("") ) {
			sm.write("INSERT INTO t_user_order ");
			sm.into("product_id",(String)json.get("product_id"));
			sm.into("com_id",com_id);
			sm.into("user_id",user_id);
			//sm.into("order_student_name",(String)json.get("order_student_name"));
			//sm.into("order_parent_name",(String)json.get("order_parent_name"));
			//sm.into("order_user_hp",(String)json.get("order_user_hp"));
			sm.into("option_id",(String)json.get("option_id"));
	//		sm.into("order_color",(String)json.get("order_color"));
			sm.into("amount",(String)json.get("amount"));
			sm.intoNumber("order_datetime","now()");
			Result = "INSERT";
			System.out.println("setUserOrder 입력SQL=" + sm.getSql());
		} else {
			sm.write("UPDATE t_user_order ");
			//sm.set("product_id",(String)json.get("product_id"));
			//sm.set("com_id",(String)json.get("com_id"));
			//sm.set("user_id",(String)json.get("user_id"));
			//sm.set("order_student_name",(String)json.get("order_student_name"));
			//sm.set("order_parent_name",(String)json.get("order_parent_name"));
			//sm.set("order_user_hp",(String)json.get("order_user_hp"));
			sm.set("option_id",(String)json.get("option_id"));
	//		sm.into("order_color",(String)json.get("order_color"));
			sm.set("amount",(String)json.get("amount"));
			sm.setNumber("order_datetime","now()");
			sm.where("order_id",order_id);
			Result = "UPDATE";
			System.out.println("setUserOrder 수정SQL=" + sm.getSql());
			
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
	 * UserOrder 확인.
	 * 
	 * @param props
	 * @throws BizException
	 */	
	private String isUserOrder(JSONObject json, String com_id, String user_id) {
		SqlMaker sm = new SqlMaker();
		
		String com_type = (String)json.get("com_type");
		
		sm.write(" SELECT order_id FROM t_user_order ");
		sm.write(" WHERE ");
		if(com_type.equals("0")) {
			sm.write(" com_id = '" + com_id + "'");
			sm.write(" AND product_id = '" + (String)json.get("product_id") + "'");
		} else {
			sm.write(" com_id = '" + com_id + "'");
			sm.write(" AND user_id = '" + user_id + "'");
			sm.write(" AND product_id = '" + (String)json.get("product_id") + "'");
		}
		
		System.out.println("SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			String obj = (String)rec.get("order_id");
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
	public ListData getProductInfo(Properties props) throws BizException{
		SqlMaker sm = new SqlMaker();
		ListData list = new ListData();
		sm.write(" SELECT * FROM t_product A");
		sm.write(" LEFT JOIN t_image B");
		sm.write(" ON A.product_id=B.product_id");
		
		System.out.println("Product정보SQL=" + sm.getSql());
		try {
			list = DaoCom.executeQueryAll(conn, list,  sm.getSql());
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("0000","getProductInfo 오류");
		}
	}	
	
	
	/**
	 * UserOrder 확인.
	 * 
	 * @param props
	 * @throws BizException
	 */	
	private String getComIdByUserId(String user_id) {
		SqlMaker sm = new SqlMaker();
		
		sm.write(" SELECT com_id FROM t_user ");
		sm.write(" WHERE ");
		sm.write(" user_id = '" + user_id + "'");
		//sm.write(" AND order_parent_name = '" + (String)json.get("order_parent_name") + "'");

		//System.out.println("SQL=" + sm.getSql());
		try {
			Record rec = DaoCom.executeSingleQuery(conn,  sm.getSql());
			String obj = (String)rec.get("com_id");
			return ParamUtil.getReqParameter(obj, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}	
}
