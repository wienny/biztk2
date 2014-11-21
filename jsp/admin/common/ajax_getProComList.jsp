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
String sCategory_id			= "";
String sCategory_title 		= "";
String sReg_datetime	 	= "";
String sCom_type			= "";
String sCom_id				= "";

String sResult_count 		= "";
			
String result	= "true";
StringBuffer str = new StringBuffer();
str.append("<?xml version='1.0' encoding='utf-8'?>");
str.append("<root>");


BizProduct biz = new BizProduct(request, response);
biz.getProComList();


ListData list = (ListData)request.getAttribute("procomlist");
if (list == null) 
	list = new ListData();

str.append("<result_count>"+list.size()+"</result_count>");
if (list.size() !=0){
	
	for(int i=0; i < list.size(); i++)	{
		Record row 			= list.get(i);
		
		sProduct_id 		= row.get("product_id", "");
		sProduct_title 		= row.get("product_title", "");
		sCategory_id		= row.get("category_id","");
		sCategory_title		= row.get("category_title","");
		sReg_datetime		= row.get("reg_datetime", "");
		
		sReg_datetime 		= sReg_datetime.substring(0,11);
		
		str.append("<item>");
		str.append("<product_id>"+sProduct_id+"</product_id>");
		str.append("<product_title>"+sProduct_title+"</product_title>");
		str.append("<category_id>"+sCategory_id+"</category_id>");
		str.append("<category_title>"+sCategory_title+"</category_title>");
		str.append("<reg_datetime>"+sReg_datetime+"</reg_datetime>");
		str.append("<com_id>"+session.getAttribute("SS_COM_ID")+"</com_id>");
		str.append("<com_type>"+session.getAttribute("SS_COM_TYPE")+"</com_type>");
		str.append("</item>");
	}
}
//System.out.println(str);

str.append("</root>");
response.setContentType("text/xml;charset=utf-8");
response.getWriter().write(str.toString());



%>
