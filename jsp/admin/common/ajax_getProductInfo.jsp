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

//String sProduct_title 		= "";
//String sCategory_id			= "";
//String sCategory_title		= "";
String sReg_datetime	 	= "";
//String sCom_type			= "";
//String sCom_id				= "";

String sResult_count 		= "";
			
String result	= "true";
StringBuffer str = new StringBuffer();
str.append("<?xml version='1.0' encoding='utf-8'?>");
str.append("<root>");

BizProduct biz = new BizProduct(request, response);
biz.getProductItem(sProduct_id);


Record row = (Record)request.getAttribute("productrecord");
if(row == null)
	sResult_count = "0";
else
	sResult_count = "1";	

str.append("<result_count>"+sResult_count+"</result_count>");

if (row != null){
	
	//for(int i=0; i < list.size(); i++)	{
	//	Record row 			= list.get(i);
		
		//sProduct_id 		= row.get("product_id", "");
		//sProduct_title 		= row.get("product_title", "");
		//sCategory_id		= row.get("category_id","");
		//sCategory_title		= row.get("category_title","");
		
		
		sReg_datetime		= row.get("reg_datetime", "");		
		sReg_datetime 		= sReg_datetime.substring(0,11);
		
		str.append("<item>");
		str.append("<product_id>"+row.get("product_id", "")+"</product_id>");
		str.append("<product_title>"+row.get("product_title", "")+"</product_title>");
		str.append("<category_id>"+row.get("category_id", "")+"</category_id>");
		str.append("<category_title>"+row.get("category_title", "")+"</category_title>");
		str.append("<reg_datetime>"+sReg_datetime+"</reg_datetime>");
		str.append("<price>"+row.get("price", "")+"</price>");
		str.append("<content>"+row.get("content", "")+"</content>");
		str.append("<etc>"+row.get("etc", "")+"</etc>");
		str.append("<com_id>"+session.getAttribute("SS_COM_ID")+"</com_id>");
		str.append("<com_type>"+session.getAttribute("SS_COM_TYPE")+"</com_type>");
		str.append("</item>");
	//}
}
//System.out.println(str);

str.append("</root>");
response.setContentType("text/xml;charset=utf-8");
response.getWriter().write(str.toString());



%>
