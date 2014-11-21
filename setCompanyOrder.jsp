<%@	page contentType="text/html;charset=utf-8" trimDirectiveWhitespaces="true" 
	import="java.io.*,
			java.util.*,
			nano.*,
			nano.biz.*,
			nano.util.*,
			nano.ListData"
%>
<%@page import="org.json.simple.JSONObject"%>
<%

	String jsonParam  = request.getParameter("message_set");
//System.out.println("json:" +jsonParam);
	JSONObject json = new JSONObject();

	if(jsonParam != null){
		BizCompanyOrder biz	= new BizCompanyOrder(request, response);
		json.put("RESULT_KEY", biz.setCompanyMessage());
	} else {
		json.put("RESULT_KEY", "NO");
	}

	response.setContentType("application/x-json; charset=UTF-8");
	response.getWriter().print(json);
%>