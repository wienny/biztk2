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
String sProduct_id			= "";
String sProduct_title 		= "";
String sOption_text			= "";
String sAmount	 			= "";
String sResult_datetime	 	= "";

String sResult_count 		= "";
			
String result	= "true";
StringBuffer str = new StringBuffer();
str.append("<?xml version='1.0' encoding='utf-8'?>");
str.append("<root>");


BizCompany biz = new BizCompany(request, response);
biz.getCompanyOrderList();


ListData list = (ListData)request.getAttribute("companyorderlist");
if (list == null) 
	list = new ListData();

str.append("<result_count>"+list.size()+"</result_count>");
if (list.size() !=0){
	
	for(int i=0; i < list.size(); i++)	{
		Record row 			= list.get(i);
		
		sProduct_id 			= row.get("product_id", "");
		sProduct_title 			= row.get("product_title", "");
		sOption_text			= row.get("option_text","");
		sAmount					= row.get("amount", "");
		sResult_datetime		= row.get("result_datetime", "");
		
		sResult_datetime 		= sResult_datetime.substring(0,11);
		
		str.append("<item>");
		str.append("<product_id>"+sProduct_id+"</product_id>");
		str.append("<product_title>"+sProduct_title+"</product_title>");
		str.append("<option_text>"+sOption_text+"</option_text>");
		str.append("<amount>"+sAmount+"</amount>");
		str.append("<result_datetime>"+sResult_datetime+"</result_datetime>");
		str.append("</item>");
	}
}
//System.out.println(str);

str.append("</root>");
response.setContentType("text/xml;charset=utf-8");
response.getWriter().write(str.toString());



%>
