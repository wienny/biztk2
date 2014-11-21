<%@	page contentType="text/html;charset=utf-8" 
	import="java.io.*,
			nano.*,
			nano.dao.*,
			nano.biz.*,
			nano.util.*,
			java.util.*"
%>
<%



String sResult_count 		= "";

String result	= "true";
StringBuffer str = new StringBuffer();
str.append("<?xml version='1.0' encoding='utf-8'?>");
str.append("<root>");
	
	BizUser biz = new BizUser(request, response);
	biz.addUser();
	
	Record row = (Record)request.getAttribute("recordUser");

	if (row == null)
		row = new Record();
			
	str.append("<item>");
	str.append("<user_id>"+row.get("user_id").toString()+"</user_id>");
	str.append("<student_name>"+row.get("student_name").toString()+"</student_name>");
	str.append("<student_hp>"+row.get("student_hp").toString()+"</student_hp>");
	str.append("<parent_name>"+row.get("parent_name").toString()+"</parent_name>");
	str.append("<parent_hp>"+row.get("parent_hp").toString()+"</parent_hp>");
	str.append("<com_id>"+row.get("com_id").toString()+"</com_id>");

	str.append("</item>");

str.append("</root>");
response.setContentType("text/xml;charset=utf-8");
response.getWriter().write(str.toString());
%>
