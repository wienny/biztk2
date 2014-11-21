<%@	page contentType="text/html;charset=utf-8" 
	import="java.io.*,
			nano.*,
			nano.dao.*,
			nano.biz.*,
			nano.util.*,
			java.util.*"
%>
<%

//String site_id = request.getParameter("p_site_id");//input Box에서 보내는 파라미터 값
String sCom_id				= "";
String sCom_name 			= "";
String sCom_type			= "";
String sCom_email	 		= "";

String sResult_count 		= "";
			
String result	= "true";
StringBuffer str = new StringBuffer();
str.append("<?xml version='1.0' encoding='utf-8'?>");
str.append("<root>");


BizCompany biz = new BizCompany(request, response);
biz.getCompanyList();

ListData list = (ListData)request.getAttribute("companylist");
if (list == null) 
	list = new ListData();

str.append("<result_count>"+list.size()+"</result_count>");
if (list.size() !=0){
	
	for(int i=0; i < list.size(); i++)	{
		Record row 			= list.get(i);
		
		sCom_id 			= row.get("com_id", "");
		sCom_name 			= row.get("com_name", "");
		sCom_type			= row.get("com_type","");
		sCom_email			= row.get("com_email", "");
		
		str.append("<item>");
		str.append("<com_id>"+sCom_id+"</com_id>");
		str.append("<com_name>"+sCom_name+"</com_name>");
		str.append("<com_type>"+sCom_type+"</com_type>");
		str.append("<com_email>"+sCom_email+"</com_email>");
		str.append("</item>");
	}
}
//System.out.println(str);

str.append("</root>");
response.setContentType("text/xml;charset=utf-8");
response.getWriter().write(str.toString());



%>
