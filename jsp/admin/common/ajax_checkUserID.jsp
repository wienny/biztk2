<%@	page contentType="text/html;charset=utf-8" 
	import="java.io.*,
			nano.*,
			nano.dao.*,
			nano.biz.*,
			nano.util.*,
			java.util.*"
%>
<%

String user_id = request.getParameter("p_user_id");//input Box에서 보내는 파라미터 값

String result	= "true";
StringBuffer str = new StringBuffer();
str.append("<?xml version='1.0' encoding='utf-8'?>");
str.append("<root>");

String[] sql_injection = {"'", "script", "=", "%", "(", ")", "<", ">", "--", "insert", "update", "delete", "select"};
int nFind = 0;
for(int i = 0; i < sql_injection.length ; i++){
	if(user_id.indexOf(sql_injection[i]) > 0) {
		nFind = 1;
		break;
	}
}

if(nFind > 0)
	result = "false";
else {
	
	BizUser biz = new BizUser(request, response);
	biz.checkUserID();
	
	ListData list = (ListData)request.getAttribute("list");
	if (list == null) 
		list = new ListData();
	
	//System.out.println("######"+list.size());
			
	if (list.size() > 0){
		for(int i=0; i < list.size(); i++)	{
			Record row = list.get(i);
			String check_id = row.get("user_id").toString();//등록되어 있는 코드
			if(user_id.equals(check_id)){//코드가 같을 경우 result = false 리턴
				result = "false";	
			}
		}
	}
}

str.append("<result>" + result + "</result>");
str.append("<id>" + user_id + "</id>");
str.append("</root>");
response.setContentType("text/xml;charset=utf-8");
response.getWriter().write(str.toString());
%>
