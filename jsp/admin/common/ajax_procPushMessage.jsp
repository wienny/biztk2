<%@	page contentType="text/html;charset=utf-8" 
	import="java.io.*,
			nano.*,
			nano.dao.*,
			nano.biz.*,
			nano.util.*,
			java.util.*"
%>
<%@ page import="com.google.android.gcm.server.*" %>
<%

/*

http://113.130.69.111:8099/jsp/admin/common/ajax_procPushMessage.jsp?
		com_id=v1&
		user_id=u1&
		type=1&
		msg=pushpushtesttest123455&
		title=aaaaaaaaa
*/

	String com_id = request.getParameter("com_id");
	String com_type = request.getParameter("com_type");
	String product_id = request.getParameter("product_id");
	
	//System.out.println(com_type);
	//String url_com_type = String.valueOf(Integer.parseInt(com_type)+1);


	String user_id = request.getParameter("user_id");
	String type = request.getParameter("type");	// 0:company, 1:user
	String title = request.getParameter("title");	
	String msg = request.getParameter("msg");	
	//String url = request.getParameter("url");
	String APIKEY = "AIzaSyBNR6GGIBC-uptYAm6u6F_71q0escKfIRo";
//	String regid = "APA91bF1c7YwkR3yQTN1B6GuJWsCd1v4xFp4kc8aH9xIqWLCTH2zUAi9rXCJVL3lf6O0PBNGNYcEQ3RiMcQq6YBnOgTL1VR1IputgIPnPc3C8a3xALc1xUbGimjR3feg91FhwEWofwyqhqbh-3SVmXow0WYSu7SXMA";
	//String url = "http://www.daum.net";
	String url = "";	
	//http://mepsh.co.kr:8411/jsp/admin/product/product_info.jsp?com_id=nano&com_type=0&product_id=pac001
	
	
//	ArrayList<String> regid = new ArrayList<String>();
	Sender sender = null;
	
	//System.out.println("com_id:" + com_id);
	//System.out.println("user_id:" + user_id);
	//System.out.println("type:" + type);
	//System.out.println("message:" + msg);
		
	String result	= "true";
	StringBuffer str = new StringBuffer();
	str.append("<?xml version='1.0' encoding='utf-8'?>");
	str.append("<root>");
	
	BizCompany biz = new BizCompany(request, response);
	biz.getPushList();

	ListData list = (ListData)request.getAttribute("getPushList");
	if (list == null) {
		list = new ListData();
		result = "false";
	}
	
	
	for(int i=0; i < list.size(); i++)	{
		Record row = list.get(i);
   		String reg_id = row.get("gcm_id","").trim();
   		//System.out.println("gcm_id:"+reg_id);
   		//System.out.println("com_id:"+row.get("com_id",""));
//   		regid.add(row.get("gcm_id","").trim());
	
		if(com_type.equals("0"))
			url = "http://113.130.69.101:8411/jsp/admin/product/product_info_m.jsp?com_id="+row.get("com_id","")+"&com_type=1&product_id="+product_id;
		else
			url = "http://113.130.69.101:8411/jsp/admin/product/product_info_m.jsp?com_id="+row.get("user_id","")+"&com_type=3&product_id="+product_id;
		
		System.out.println(url);
		
	    try {
	        String sendTlt = title;
	        String sendMsg = msg;
	
	        sender = new Sender(APIKEY);
	        
	        
	
	        Message message = new Message.Builder()
	        .collapseKey(String.valueOf(Math.random() % 100 + 1))
	        .addData("title", sendTlt)
	        .addData("message", sendMsg)
	        .addData("url", url)
	       // .delayWhileIdle(false) // 기기 활성화시 보여줌.
	       // .timeToLive(3) // 초, 단말기가 비활성화 상태일때 메시지 유효시간 
	        .build();
	
	        Result result2 = sender.send(message, reg_id, 3);
	        //MulticastResult result2 = sender.send(message, regid, 3); 
	
	        if (result2.getMessageId() != null) {
	            System.out.println("["+i+"]"+"Send success");
	        } else {
	            String error = result2.getErrorCodeName();
	            System.out.println("["+i+"]"+"Send fail : " + error);
	        }
	
	    } catch (Exception e) {
	     	e.printStackTrace();
	    }
 
	}
	
	
	str.append("</root>");
	response.setContentType("text/xml;charset=utf-8");
	response.getWriter().write(str.toString());
%>
