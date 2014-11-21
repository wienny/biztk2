<%@	page contentType="text/html;charset=utf-8" %>
<%
String menu_type1 = request.getParameter("menu_type1")==null? "A":request.getParameter("menu_type1");		//메뉴타입1
String menu_type2 = request.getParameter("menu_type2")==null? "A":request.getParameter("menu_type2");		//메뉴타입2
%>
<%
if(menu_type1.equals("A")){
%>
		<div id="leftbody">
			<ul id="leftmenus">
				<li class="lm"><span class="lmonl"></span><span class="lmonr">Report</span></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('A','A','/jsp/admin/report/report_li.jsp')" target="_self"><span class="lmoffr">Special Reports</span></a></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('A','B','/jsp/admin/report/report_li.jsp')" target="_self"><span class="lmoffr">Industry Review</span></a></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('A','C','/jsp/admin/report/report_li.jsp')" target="_self"><span class="lmoffr">Stakeholder Analysis</span></a></li>
				<!-- <li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('A','D','/jsp/admin/report/report_li.jsp')" target="_self"><span class="lmoffr">Custom Intelligence</span></a></li> -->
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('A','E','/jsp/admin/report/upcomming_li.jsp')" target="_self"><span class="lmoffr">Upcomming</span></a></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('A','F','/jsp/admin/report/sample_li.jsp')" target="_self"><span class="lmoffr">Sample File Upload</span></a></li>
			</ul>
		</div>
<%
}else if(menu_type1.equals("B")){
%>
		<div id="leftbody">
			<ul id="leftmenus">
				<li class="lm"><span class="lmonl"></span><span class="lmonr">Inquiry</span></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('B','A','/jsp/admin/inquiry/order_li.jsp')" target="_self"><span class="lmoffr">Custom Intelligence</span></a></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('B','B','/jsp/admin/inquiry/inquiry_li.jsp')" target="_self"><span class="lmoffr">Become a Partner</span></a></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('B','C','/jsp/admin/inquiry/inquiry_li.jsp')" target="_self"><span class="lmoffr">Contact US</span></a></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('B','D','/jsp/admin/inquiry/subscription_li.jsp')" target="_self"><span class="lmoffr">Subscription</span></a></li>
			</ul>
		</div>
<%
}else if(menu_type1.equals("C")){
%>
		<div id="leftbody">
			<ul id="leftmenus">
				<li class="lm"><span class="lmonl"></span><span class="lmonr">Category</span></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('C','A','/jsp/admin/category/category_li.jsp')" target="_self"><span class="lmoffr">List</span></a></li>
			</ul>
		</div>
<%
}else if(menu_type1.equals("D")){
%>
		<div id="leftbody">
			<ul id="leftmenus">
				<li class="lm"><span class="lmonl"></span><span class="lmonr">Purchase</span></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('D','A','/jsp/admin/purchase/purchase_li.jsp')" target="_self"><span class="lmoffr">List</span></a></li>
			</ul>
		</div>
<%
}else if(menu_type1.equals("E")){
%>
		<div id="leftbody">
			<ul id="leftmenus">
				<li class="lm"><span class="lmonl"></span><span class="lmonr">Member</span></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('E','A','/jsp/admin/member/member_li.jsp')" target="_self"><span class="lmoffr">Member List</span></a></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('E','B','/jsp/admin/member/sampledown_li.jsp')" target="_self"><span class="lmoffr">SampleDown Info</span></a></li>
			</ul>
		</div>
<%
}else if(menu_type1.equals("F")){
%>
		<div id="leftbody">
			<ul id="leftmenus">
				<li class="lm"><span class="lmonl"></span><span class="lmonr">Sales Promotion</span></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('F','A','/jsp/admin/promotion/promotion_li.jsp')" target="_self"><span class="lmoffr">1</span></a></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('F','B','/jsp/admin/promotion/promotion_li.jsp')" target="_self"><span class="lmoffr">2</span></a></li>
			</ul>
		</div>
		<%
}else if(menu_type1.equals("G")){
%>
		<div id="leftbody">
			<ul id="leftmenus">
				<li class="lm"><span class="lmonl"></span><span class="lmonr">Visit Statistics</span></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('G','A','/jsp/admin/statistics/visit_day_stats.jsp')" target="_self"><span class="lmoffr">Day</span></a></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('G','B','/jsp/admin/statistics/visit_month_stats.jsp')" target="_self"><span class="lmoffr">Month</span></a></li>
				<li class="lmSub"><span class="lmoffl"></span><a href="javascript:menuLink('G','C','/jsp/admin/statistics/visit_year_stats.jsp')" target="_self"><span class="lmoffr">Year</span></a></li>
			</ul>
		</div>
<%} %>
		