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
String sCom_id = request.getParameter("p_com_id");//input Box에서 보내는 파라미터 값
String sProduct_name = "";
String sOrder_size	 	= "";
String sAmount_num			= "";

String sResult_count 		= "";
			
String result	= "true";
StringBuffer str = new StringBuffer();
str.append("<?xml version='1.0' encoding='utf-8'?>");
str.append("<root>");


BizOrder biz = new BizOrder(request, response);
biz.setCompanyOrder();


// ListData list = (ListData)request.getAttribute("userorderbysizelist");
// if (list == null) 
// 	list = new ListData();

// str.append("<result_count>"+list.size()+"</result_count>");
// if (list.size() !=0){
	
// 	for(int i=0; i < list.size(); i++)	{
// 		Record row 			= list.get(i);
		
// 		sProduct_id 			= row.get("product_id", "");
// 		sProduct_name 			= row.get("product_name", "");
// 		sOrder_size 			= row.get("order_size", "");
// 		sAmount_num		= row.get("amount_num", "0");
		
// 		str.append("<item>");
// 		str.append("<product_id>"+sProduct_id+"</product_id>");
// 		str.append("<product_name>"+sProduct_name+"</product_name>");
// 		str.append("<order_size>"+sOrder_size+"</order_size>");
// 		str.append("<amount_num>"+sAmount_num+"</amount_num>");
// 		str.append("</item>");
// 	}
// }
//System.out.println(str);

str.append("</root>");
response.setContentType("text/xml;charset=utf-8");
response.getWriter().write(str.toString());



%>
