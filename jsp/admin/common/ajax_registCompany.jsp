<%@	page contentType="text/html;charset=utf-8" 
	import="java.io.*,
			nano.*,
			nano.dao.*,
			nano.biz.*,
			nano.util.*,
			java.util.*"
%>
<%

String com_id = request.getParameter("p_com_rid");//input Box에서 보내는 파라미터 값
String com_pw = request.getParameter("p_com_rpw");//input Box에서 보내는 파라미터 값
String com_name = request.getParameter("p_com_rname");//input Box에서 보내는 파라미터 값
String com_type = request.getParameter("p_com_type");;

//System.out.println(com_id);

String result	= "true";
StringBuffer str = new StringBuffer();
str.append("<?xml version='1.0' encoding='utf-8'?>");
str.append("<root>");


String[] sql_injection = {"'", "script", "=", "%", "(", ")", "<", ">", "--", "insert", "update", "delete", "select"};
int nFind = -1;
for(int i = 0; i < sql_injection.length ; i++){
	if(com_id.toLowerCase().indexOf(sql_injection[i]) > -1 || com_pw.toLowerCase().indexOf(sql_injection[i]) > -1 || com_name.toLowerCase().indexOf(sql_injection[i]) > -1) {
		nFind = 1;
		break;
	}
}


if(nFind > 0)
	result = "false";
else {
	
	BizCompany biz = new BizCompany(request, response);
	if(biz.addCompany().equals("Complete.")) {
		//Record row = (Record)request.getAttribute("company");
		//com_type = row.get("com_type", "");		
		result = "true";
	}
	else
		result = "false";
		
}

str.append("<result>" + result + "</result>");
str.append("<com_type>" + com_type + "</com_type>");
str.append("</root>");
response.setContentType("text/xml;charset=utf-8");
response.getWriter().write(str.toString());
%>
