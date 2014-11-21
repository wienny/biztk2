
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.io.*, java.util.*" %>
<%
	session.setAttribute("lastcmd", "Exception"); 


	Throwable tw = (Throwable)request.getAttribute("javax.servlet.error.exception"); 
	StackTraceElement [] ste = new StackTraceElement[20];
	if (tw != null){
		ste = tw.getStackTrace();
	}

	
	/*

	javax.servlet.error.status_code: 에러 상태 코드를 말해 주는 정수이다. 
	
	javax.servlet.error.exception_type: 에러가 생기게 된 예외 형을 지적해 주는 클래스 인스턴스이다. 
	javax.servlet.error.message: 예외 메시지를 말해주는 스트링이며, 예외 컨스트럭터로 보내어 진다.
	javax.servlet.error.exception: 실제 예외가 없어지면 버릴 수 있는 객체이다. 
	javax.servlet.error.request_uri: 문제를 일으킨 리소스의 URI를 말해주는 스트링이다. 
	*/
%>

<html>
<head>
<title>KGBK</title>
</head>

<body bgcolor="#FFFFFF">




  <table width="800" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td height="2" align="left">
        <table width="541" border="0" cellspacing="0" cellpadding="0" height="3">
          <tr>
            <td height="8"> <B>오류가 발생하였습니다.</B> </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td align="right" valign="top">
        <table width="100%" border="0" cellspacing="1" cellpadding="5" bgcolor="CECECE" bordercolor="CECECE">
          <col width=15%>
          <col width=85%>
          <tr>
            <td bgcolor="#ACACFF" height="3"></td>
            <td bgcolor="#DBDBDB" height="3"></td>
          </tr>
          <tr>
            <td bgcolor="#EAF1F2"" align="right">상태코드</td>
            <td bgcolor="#FFFFFF" >
              <%= request.getAttribute("javax.servlet.error.status_code")%>
            </td>
          </tr>
          <tr>
            <td bgcolor="#EAF1F2"" align="right">접근경로</td>
            <td bgcolor="#FFFFFF" >
              <%= request.getAttribute("javax.servlet.error.request_uri")%>
            </td>
          </tr>
          <tr>
            <td bgcolor="#EAF1F2"" align="right">오류종류</td>
            <td bgcolor="#FFFFFF" >
              <%= request.getAttribute("javax.servlet.error.exception_type")%>
            </td>
          </tr>
          <tr>
            <td bgcolor="#EAF1F2"" align="right">오류메세지</td>
            <td bgcolor="#FFFFFF" >
              <%= request.getAttribute("javax.servlet.error.message")%>
            </td>
          </tr>
          <tr>
            <td bgcolor="#EAF1F2"" align="right">오류 추적정보</td>
            <td bgcolor="#FFFFFF" >
			<%
				for(int i=0; i< 10; i++){
					if (ste [i]==null){
						out.print("&nbsp;");
					} else {
						out.print(ste [i].toString());
					}
					out.println("<br>");
				}
			%>

            </td>
          </tr>
          <tr>
            <td bgcolor="#DBDBDB" height="1"></td>
            <td bgcolor="#DBDBDB" height="1"></td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td align=center valign=middle height=30>
        [<a href="javascript:history.back()"> 뒤로가기 </a>]
      </td>
    </tr>
  </table>
</body>
</html>

