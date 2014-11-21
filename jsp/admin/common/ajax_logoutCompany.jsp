<%@	page contentType="text/html;charset=utf-8" 
	import="java.io.*,
			nano.*,
			nano.dao.*,
			nano.biz.*,
			nano.util.*,
			java.util.*"
%>
<%



String result	= "true";
StringBuffer str = new StringBuffer();
str.append("<?xml version='1.0' encoding='utf-8'?>");
str.append("<root>");


session.removeAttribute("SS_COM_ID");
session.removeAttribute("SS_JOIN_ID");
session.removeAttribute("SS_COM_TYPE");
session.removeAttribute("SS_COM_NAME");

str.append("<result>" + result + "</result>");
str.append("</root>");

//System.out.println(str);
response.setContentType("text/xml;charset=utf-8");
response.getWriter().write(str.toString());
%>
