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


String sResult_count 		= "";
			
String result	= "true";
StringBuffer str = new StringBuffer();
str.append("<?xml version='1.0' encoding='utf-8'?>");
str.append("<root>");


BizUser biz = new BizUser(request, response);
biz.getUserList();

ListData list = (ListData)request.getAttribute("userlist");
if (list == null) 
	list = new ListData();



str.append("<result_count>"+list.size()+"</result_count>");
if (list.size() !=0){
	
	for(int i=0; i < list.size(); i++)	{
		Record row 			= list.get(i);
		
		str.append("<item>");
		str.append("<user_id>"+row.get("user_id", "")+"</user_id>");
		str.append("<student_name>"+row.get("student_name", "")+"</student_name>");
		str.append("<student_hp>"+row.get("student_hp", "")+"</student_hp>");
		str.append("<parent_name>"+row.get("parent_name", "")+"</parent_name>");
		str.append("<parent_hp>"+row.get("parent_hp", "")+"</parent_hp>");		
		str.append("<com_id>"+row.get("com_id","")+"</com_id>");
		str.append("</item>");
	}
}
//System.out.println(str);

str.append("</root>");
response.setContentType("text/xml;charset=utf-8");
response.getWriter().write(str.toString());



%>
