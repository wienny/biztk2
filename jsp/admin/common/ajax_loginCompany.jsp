<%@	page contentType="text/html;charset=utf-8" 
	import="java.io.*,
			nano.*,
			nano.dao.*,
			nano.biz.*,
			nano.util.*,
			java.util.*"
%>
<%

String com_id = request.getParameter("p_com_id");//input Box에서 보내는 파라미터 값
String com_pw = request.getParameter("p_com_pw");//input Box에서 보내는 파라미터 값
String com_type = "";
String com_name = "";


String result	= "true";
StringBuffer str = new StringBuffer();
str.append("<?xml version='1.0' encoding='utf-8'?>");
str.append("<root>");


String[] sql_injection = {"'", "script", "=", "%", "(", ")", "<", ">", "--", "insert", "update", "delete", "select"};
int nFind = -1;
for(int i = 0; i < sql_injection.length ; i++){
	if(com_id.toLowerCase().indexOf(sql_injection[i]) > -1 || com_pw.toLowerCase().indexOf(sql_injection[i]) > -1) {
		nFind = 1;
		break;
	}
}


if(nFind > 0)
	result = "false";
else {
	
	BizCompany biz = new BizCompany(request, response);
	if( biz.login()) {
		Record row = (Record)request.getAttribute("company");
		com_type = row.get("com_type", "");
		com_name = row.get("com_name", "");
		result = "true";
	} else {
		result = "false";
		com_type = "3";
	}
	//System.out.println(result);
	
	//if(result.equals("false")) {
	biz.addProperty("result", result);	
	result = biz.setPushRegister();	
	
	if(result.equals("true")){
		//HttpSession session = req.getSession();
		session.setAttribute("SS_COM_ID", com_id);
		session.setAttribute("SS_COM_TYPE", com_type);		
		session.setAttribute("SS_COM_NAME", com_name);
	}
		
	
	//}
	
}

str.append("<result>" + result + "</result>");
str.append("<com_type>" + com_type + "</com_type>");
str.append("</root>");

System.out.println(str);
response.setContentType("text/xml;charset=utf-8");
response.getWriter().write(str.toString());
%>
