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
String sOption_id				= "";
String sOption_text				= "";
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
		sOption_id					= row.get("option_id","");
		sOption_text					= row.get("option_text","");
		sAmount					= row.get("amount", "");
		sResult_datetime		= row.get("result_datetime", "");
		
		sResult_datetime 		= sResult_datetime.substring(0,11);
		
		str.append("<item>");
		str.append("<com_id>"+sCom_id+"</com_id>");
		str.append("<com_name>"+sCom_name+"</com_name>");
		str.append("<option_id>"+sOption_id+"</option_id>");
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
