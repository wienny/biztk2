<%@	page contentType="text/html;charset=utf-8" %>
<%
if(session.getAttribute("SS_ADMIN_ID")==null){
	out.println("<script type=\"text/javascript\">");
	out.println("location.href='/jsp/admin/admin_manager.jsp?cmd=logout';");
	out.println("</script>");
}
%>
<script type="text/javascript">
<!--
//************************************************
//링크
//************************************************
function menuLink(type1, type2, action){
	linkForm.menu_type1.value = type1;
	linkForm.menu_type2.value = type2;
	linkForm.action = action;
	linkForm.submit();
}

//-->
</script>
		<form name="linkForm" method="post">
		<input type="hidden" name="menu_type1" value="" />
		<input type="hidden" name="menu_type2" value="" />
		<input type="hidden" name="cmd" value="" />
		
		</form>
		<div id="tlayer">
			<ul>
				<li><a href="#"><img src="/images/admin/bodytop_bg.png" width="274" height="40" alt="managersystem" /></a></li>
				<li>
					<div id="topmenu">
						<ul>
							<li><a href="javascript:menuLink('A', 'A','/jsp/admin/report/report_li.jsp')"><span>Reports</span></a></li>
							<li><a href="javascript:menuLink('B', 'A','/jsp/admin/inquiry/order_li.jsp')"><span>Inquires</span></a></li>
							<li><a href="javascript:menuLink('C', 'A','/jsp/admin/category/category_li.jsp')"><span>Category</span></a></li>
							<li><a href="javascript:menuLink('D', 'A','/jsp/admin/purchase/purchase_li.jsp')"><span>Purchase</span></a></li>
							<li><a href="javascript:menuLink('E', 'A','/jsp/admin/member/member_li.jsp')"><span>Members</span></a></li>
							<li><a href="javascript:menuLink('F', 'A','/jsp/admin/promotion/promotion_li.jsp')"><span>Sales Promotion</span></a></li>
							<li><a href="javascript:menuLink('G', 'A','/jsp/admin/statistics/visit_day_stats.jsp')"><span>Visit Statistics</span></a></li>
						</ul>
					</div>
				</li>
				<div id="mename" class="fright">
					<a href="javascript:menuLink('', '','/jsp/admin/admin_manager.jsp?cmd=logout')">Logout</a>
				</div>
			</ul>
		</div>
