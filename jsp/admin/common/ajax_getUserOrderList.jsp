<%@	page contentType="text/html;charset=utf-8" 
	import="java.io.*,
			nano.*,
			nano.dao.*,
			nano.biz.*,
			nano.util.*,
			java.util.*"
%>
<%

String sProduct_id = request.getParameter("p_product_id");//input Box에서 보내는 파라미터 값
String sOrder_id			= "";
String sOrder_student_name 		= "";
String sOrder_parent_name		= "";
String sOption_text	 	= "";
String sAmount			= "";
String sOrder_datetime				= "";

String sResult_count 		= "";
			
String result	= "true";
StringBuffer str = new StringBuffer();
str.append("<?xml version='1.0' encoding='utf-8'?>");
str.append("<root>");


BizOrder biz = new BizOrder(request, response);
biz.getUserOrderList();


ListData list = (ListData)request.getAttribute("userorderlist");
if (list == null) 
	list = new ListData();

str.append("<result_count>"+list.size()+"</result_count>");
if (list.size() !=0){
	
	for(int i=0; i < list.size(); i++)	{
		Record row 			= list.get(i);
		
		sOrder_id 			= row.get("order_id", "");
		sOrder_student_name 			= row.get("student_name", "");
		sOrder_parent_name 			= row.get("parent_name", "");
		sOption_text			= row.get("option_text","");
		sAmount		= row.get("amount", "0");
		sOrder_datetime = row.get("order_datetime","");
		sOrder_datetime 		= sOrder_datetime.substring(0,11);
		
		str.append("<item>");
		str.append("<order_id>"+sOrder_id+"</order_id>");
		str.append("<student_name>"+sOrder_student_name+"</student_name>");
		str.append("<parent_name>"+sOrder_parent_name+"</parent_name>");
		str.append("<option_text>"+sOption_text+"</option_text>");
		str.append("<amount>"+sAmount+"</amount>");
		str.append("<order_datetime>"+sOrder_datetime+"</order_datetime>");
		str.append("</item>");
	}
}
//System.out.println(str);

str.append("</root>");
response.setContentType("text/xml;charset=utf-8");
response.getWriter().write(str.toString());



%>
