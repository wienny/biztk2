<%@	page contentType="text/html;charset=utf-8" 
	import="java.io.*,
			java.net.*"

%>
<%

/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
*/


        URL ocu = null;
        try {
        	//URL Object 객체 생성
            ocu = new URL("http://113.130.69.101:8409/jsp/admin/common/ajax_getMenuList.jsp");  
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        URLConnection con = null;
        BufferedReader in = null;
        String inputLine = new String();
        
        try {
            con = ocu.openConnection();                    //URL연결
            in = new BufferedReader(new InputStreamReader(con.getInputStream())); //URLConnection에서 읽어오기
            while ((inputLine = in.readLine()) != null)
            {
                out.println(inputLine);
                System.out.println(inputLine);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
%>        