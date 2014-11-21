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

	String jsonParam  = request.getParameter("order_set");
	JSONObject json = new JSONObject();

	if(jsonParam != null){
		BizUserOrder biz	= new BizUserOrder(request, response);
		json.put("RESULT_KEY", biz.setUserOrder());
	} else {
		json.put("RESULT_KEY", "NO");
	}

	response.setContentType("application/x-json; charset=UTF-8");
	response.getWriter().print(json);
%>