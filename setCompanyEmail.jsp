<%@	page contentType="text/html;charset=utf-8" trimDirectiveWhitespaces="true" 
	import="java.io.*,
			java.util.*,
			nano.*,
			nano.biz.*,
			nano.util.*,
			nano.ListData"
%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONValue"%>
<%

	String jsonParam  = request.getParameter("email_set");
	
	JSONObject json = new JSONObject();

	if(jsonParam != null){
		BizCompanyOrder biz	= new BizCompanyOrder(request, response);
		JSONObject jsonObj = (JSONObject) JSONValue.parse(jsonParam);
		String link_url = "http://" + request.getServerName();
		if(request.getServerPort() != 80) {
			link_url += ":" + request.getServerPort();
		}
		
//		link_url += ("/productView.jsp?product_id=" + (String)jsonObj.get("product_id") + "&com_type=1");
		link_url += "/index.jsp";
		biz.addProperty("product_id", (String)jsonObj.get("product_id"));
		biz.addProperty("link_url", link_url);
		biz.addProperty("product_title", (String)jsonObj.get("product_title"));
		biz.addProperty("product_detail", (String)jsonObj.get("product_detail"));
		biz.addProperty("email_image_name", (String)jsonObj.get("email_image_name"));
		
		json.put("RESULT_KEY", biz.setCompanyEmail());
	} else {
		json.put("RESULT_KEY", "NO");
	}

	response.setContentType("application/x-json; charset=UTF-8");
	response.getWriter().print(json);
%>