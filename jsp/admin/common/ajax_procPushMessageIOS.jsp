<%@	page contentType="text/html;charset=utf-8" 
	import="java.io.*,
			nano.*,
			nano.dao.*,
			nano.biz.*,
			nano.util.*,
			java.util.*,
			java.util.List,
			java.util.Arrays,
			java.util.ArrayList"
%>
<%@ page import="javapns.*" %>
<%@ page import="javapns.communication.ConnectionToAppleServer" %> 
<%@ page import="javapns.communication.exceptions.CommunicationException" %> 
<%@ page import="javapns.communication.exceptions.KeystoreException" %>

<%@ page import="javapns.devices.Device" %>  
<%@ page import="javapns.devices.implementations.basic.BasicDevice" %>  

<%@ page import="javapns.notification.AppleNotificationServerBasicImpl" %> 
<%@ page import="javapns.notification.PushNotificationManager" %> 
<%@ page import="javapns.notification.PushNotificationPayload" %>
<%@ page import="javapns.notification.PushedNotification" %> 
<%@ page import="javapns.notification.Payload" %> 
       

<%@ page import="java.lang.Object" %>    
<%@ page import="org.apache.commons.lang.StringUtils" %> 
<%@ page import="org.apache.log4j.BasicConfigurator" %> 
<%@ page import="org.json.*" %>
<%@ page import="org.json.JSONException" %>

<%

String com_id = request.getParameter("com_id");
String com_type = request.getParameter("com_type");
String product_id = request.getParameter("product_id");

   System.out.println( "==========================================" );
      System.out.println( "Setting up Push notification" );

	//APNS 기본정보
	 String   CERTIFICATE_PATH     = "D:/workspace/certificate/apnsprodution.p12";
	 String   CERTIFICATE_PWD  = "nano8585@";      // 푸시 인증서 비밀번호
	 String   APNS_DEV_HOST  = "gateway.sandbox.push.apple.com"; // 개발용 푸시 전송서버 
	 String   APNS_HOST   = "gateway.push.apple.com";  // 운영 푸시 전송서버
	 int      APNS_PORT   = 2195;      // 이건..개발용 운영용 나뉘는지 확인해보자
	 //PUSH 메시지
	 int   BADGE    = 1;       // App 아이콘 우측상단에 표시할 숫자
	 String  SOUND    = "default";      // 푸시 알림음
	
	//String com_id = request.getParameter("com_id");
	String user_id = request.getParameter("user_id");
	String type = request.getParameter("type");	// 0:company, 1:user
	String device_type = request.getParameter("device_type");	// 0:company, 1:user
	String msg = request.getParameter("msg");
	//String url = "http://www.naver.com";
	String url = "";
	
	
	System.out.println("com_id:" + com_id);
	System.out.println("user_id:" + user_id);
	System.out.println("type:" + type);
	System.out.println("message:" + msg);
	System.out.println("device_type:" + device_type);
	
	
	String result	= "true";
	StringBuffer str = new StringBuffer();
	str.append("<?xml version='1.0' encoding='utf-8'?>");
	str.append("<root>");		
	
	
	BizCompany biz = new BizCompany(request, response);
	biz.getPushIOSList();
	
	ListData list = (ListData)request.getAttribute("getPushIOSList");
	if (list == null) {
		list = new ListData();
		result = "false";
	}
			
	
	for(int i=0; i < list.size(); i++)	{
		Record row = list.get(i);
   		String reg_id = row.get("gcm_id","").trim();
   		System.out.println("gcm_id:"+reg_id);
	
		if(com_type.equals("0"))
			url = "http://113.130.69.101:8411/jsp/admin/product/product_info_m.jsp?com_id="+row.get("com_id","")+"&com_type=1&product_id="+product_id;
		else
			url = "http://113.130.69.101:8411/jsp/admin/product/product_info_m.jsp?com_id="+row.get("user_id","")+"&com_type=3&product_id="+product_id;
			   		
   		//url = "http://www.daum.net";
   		//msg = "Proposal Msg";
		
	     HashMap<String, Object> map_pushInfo = new HashMap<String, Object>();

	     String sender_nick = (String)map_pushInfo.get("sender_nick");
	     String msgtoken = (String)map_pushInfo.get("msg");
	     
	     try{
    	    // 3. 푸시 데이터 생성
    	    PushNotificationPayload payLoad = new PushNotificationPayload();
    	    
    	    JSONObject jo_body = new JSONObject();
    	    JSONObject jo_aps = new JSONObject();
    	    JSONArray ja = new JSONArray();
    	   
    	    jo_aps.put("alert",msg);
    	    jo_aps.put("url",url);
    	    jo_aps.put("badge",BADGE);
    	    jo_aps.put("sound",SOUND);
    	    jo_aps.put("content-available",1);
    	    
    	    jo_body.put("aps",jo_aps);
    	    jo_body.put("sender_nick",sender_nick);
    	    payLoad = payLoad.fromJSON(jo_body.toString());
    	    	    	    
    	    PushNotificationManager pushManager = new PushNotificationManager();
    	    pushManager.initializeConnection( new AppleNotificationServerBasicImpl(CERTIFICATE_PATH,CERTIFICATE_PWD,ConnectionToAppleServer.KEYSTORE_TYPE_PKCS12, APNS_HOST, APNS_PORT));
    	             
    	    List<PushedNotification> notifications = new ArrayList<PushedNotification>();
    	             
    	    // 싱글캐스트 푸시 전송
   	        Device device = new BasicDevice();
   	        
   	        device.setToken(reg_id);
   	        PushedNotification notification = pushManager.sendNotification(device, payLoad);
   	        notifications.add(notification);

   	        List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);
   	        List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);
   	              
   	        int failed = failedNotifications.size();
   	        int successful = successfulNotifications.size();
    	            
    	              
     	         }catch (Exception e) {
  	  
  	          e.printStackTrace();
  	         }
	}

str.append("</root>");
response.setContentType("text/xml;charset=utf-8");
response.getWriter().write(str.toString());
%>
