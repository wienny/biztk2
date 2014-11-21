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
String sCom_id			= "";
String sCom_name 		= "";
String sOrder_size				= "";
String sAmount	 			= "";
String sResult_datetime	 	= "";

String sResult_count 		= "";
			
String result	= "true";
StringBuffer str = new StringBuffer();
str.append("<?xml version='1.0' encoding='utf-8'?>");
str.append("<root>");


BizProduct biz = new BizProduct(request, response);
biz.getProductOrderList();


ListData list = (ListData)request.getAttribute("productorderlist");
if (list == null) 
	list = new ListData();

str.append("<result_count>"+list.size()+"</result_count>");
if (list.size() !=0){
	
	for(int i=0; i < list.size(); i++)	{
		Record row 			= list.get(i);
		
		sCom_id 				= row.get("com_id", "");
		sCom_name	 			= row.get("com_name", "");
		sOrder_size					= row.get("order_size","");
		sAmount					= row.get("amount", "");
		sResult_datetime		= row.get("result_datetime", "");
		
		sResult_datetime 		= sResult_datetime.substring(0,11);
		
		str.append("<item>");
		str.append("<com_id>"+sCom_id+"</com_id>");
		str.append("<com_name>"+sCom_name+"</com_name>");
		str.append("<order_size>"+sOrder_size+"</order_size>");
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
