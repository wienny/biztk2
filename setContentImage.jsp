<%@	page contentType="text/html;charset=utf-8" trimDirectiveWhitespaces="true" 
	import="java.io.*,
			java.util.*,
			nano.*,
			nano.biz.*,
			nano.util.*,
			nano.ListData"
%>
<%@page import="com.oreilly.servlet.MultipartRequest,
com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>

<%@page import="org.json.simple.JSONObject"%>
<%
	String tmpPath	= request.getRealPath("/")+"file";
	int size = 100000*1024*1024;//최대 업로드 사이즈 설정
	MultipartRequest multi = new MultipartRequest(request, tmpPath, size, "utf-8", new DefaultFileRenamePolicy());
	//String data  = request.getParameter("image_data");
//System.out.println("d:"+multi.getParameter("image_data"));
	String data = multi.getParameter("image_data");
	String savePath = "file" + File.separator + "images" + File.separator + "contents" + File.separator; // 저장할 디렉토리
	String saveFullPath	= request.getRealPath("/") + savePath;
	data = data.replaceAll("data:image/png;base64,", "");
	
	byte[] DataFile = new sun.misc.BASE64Decoder().decodeBuffer(data);
	long _timestamp = System.currentTimeMillis();
	String save_file = saveFullPath + _timestamp+".png";
	FileOutputStream osf = new FileOutputStream(new File(save_file)); 
	osf.write(DataFile); 
	osf.flush();
	osf.close();

	String saveImage = "http://" + request.getServerName();
	if(request.getServerPort() != 80) {
		saveImage = saveImage + ":" + request.getServerPort();	
	}
	saveImage += ("/"+savePath + _timestamp+".png");
	saveImage = saveImage.replaceAll("\\\\","/");

	
	
	JSONObject json = new JSONObject();
	json.put("RESULT_KEY", "OK");
	json.put("imgPath", saveImage);

	response.setContentType("text/html; charset=utf-8");
 	response.getWriter().write(json.toJSONString());
	
//	response.setContentType("application/x-json; charset=UTF-8");
//	response.getWriter().print(json);

%>