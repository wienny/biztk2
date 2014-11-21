<%
/*
**************************************************************
* DATE			AUTHOR		DESCRIPTION
* ---------------------------------------------------------------
* 2013. 10. 16	정수현		최초작성
* 
*************************************************************** 
*/
%>
<%@	page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8"
	import="java.io.*,
			nano.biz.*,
			nano.*"
%>
<%
String ss_com_id = session.getAttribute("SS_COM_ID")== null ? "": session.getAttribute("SS_COM_ID").toString();
String ss_com_name = session.getAttribute("SS_COM_NAME")== null ? "": session.getAttribute("SS_COM_NAME").toString();
String ss_com_type = session.getAttribute("SS_COM_TYPE")== null ? "": session.getAttribute("SS_COM_TYPE").toString();

if(ss_com_id.equals("")) {
	response.sendRedirect("/");
}

String sProduct_id = request.getParameter("product_id")== null ? "": request.getParameter("product_id");


String sProduct_name = "";
String sTitle = "";
String sMsg = "";

BizProduct bizP = new BizProduct(request, response);
bizP.getProductItem(sProduct_id);

Record recP = (Record)request.getAttribute("productrecord");
sProduct_name = recP.get("product_title", "");
sTitle = "[연합]제품 안내";
sMsg = "["+sProduct_name+"] 제품을 안내해 드립니다.";


%>
<!DOCTYPE html>

<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8"/>
<title>BIZTK - 상품 정보</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
<meta content="" name="description"/>
<meta content="" name="author"/>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all" rel="stylesheet" type="text/css"/>
<link href="/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<link href="/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="/assets/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES -->
<link href="/assets/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.css" rel="stylesheet"/>
<link rel="stylesheet" type="text/css" href="/assets/plugins/bootstrap-colorpicker/css/colorpicker.css"/>
<link rel="stylesheet" type="text/css" href="/assets/plugins/bootstrap-daterangepicker/daterangepicker-bs3.css"/>
<link href="/assets/plugins/jquery-file-upload/blueimp-gallery/blueimp-gallery.min.css" rel="stylesheet"/>
<!-- END PAGE LEVEL STYLES -->
<!-- BEGIN THEME STYLES -->
<link href="/assets/css/style-metronic.css" rel="stylesheet" type="text/css"/>
<link href="/assets/css/style.css" rel="stylesheet" type="text/css"/>
<link href="/assets/css/style-responsive.css" rel="stylesheet" type="text/css"/>
<link href="/assets/css/plugins.css" rel="stylesheet" type="text/css"/>
<link href="/assets/css/themes/default.css" rel="stylesheet" type="text/css" id="style_color"/>
<link href="/assets/css/custom.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="/assets/plugins/bootstrap-switch/css/bootstrap-switch.min.css"/>
<!-- END THEME STYLES -->
<link rel="shortcut icon" href="favicon.ico"/>
<script src="/lib/ajax_use.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript">
	<!--
	
	$(document).ready(function(){
		//************************************************
		//로그인 확인
		//************************************************
		if('<%=ss_com_id%>' == ""){
			if(confirm){
				alert('로그인이 필요한 서비스입니다.');
				document.location.href="/";
			}
		}
		
		//----------------------
		// 업체 주문상품 목록 조회 . 페이지 로딩 시 최초 1회. ajax call 
		//----------------------				
		getProductInfo('<%=sProduct_id%>');
		getProductImgInfo('<%=sProduct_id%>');
		getProductSubInfo('<%=sProduct_id%>');
		getProductOptionInfo('<%=sProduct_id%>');
		
		//----------------------
		// 업체 주문 리스트 by size group 조회. ajax call 
		//----------------------
		//getCompanyOrderBySizeList('<%=sProduct_id%>');	
		
		//------------------
		// 인터페이스 보임,숨김 Toggle.
		//------------------
		function viewAddMenu(){
			var state = $('div[id=menuinsert_form_div]').css('display');
			if(state=="none") {
				$('div[id=menuinsert_form_div]').css('display','block');
				$('div[id=div_menu_setting]').css('display','none');
			}
			else
				$('div[id=menuinsert_form_div]').css('display','none');
		}	
		
		//----------------------
		// 구매완료 버튼 클릭 시 post.  ajax call 
		//----------------------
		//$('#submit_btn').bind('click', function() {
		//	addSite();
		//});
		
		

	});	

	//-->
</script>	
</head>
<!-- BEGIN BODY -->
<body class="page-header-fixed">
<!-- BEGIN HEADER -->
<div class="header navbar navbar-fixed-top mega-menu">
	<!-- BEGIN TOP NAVIGATION BAR -->
	<div class="header-inner">
		<!-- BEGIN LOGO -->
		<a class="navbar-brand" href="javascript:;">
			<img src="/assets/img/logo.png" alt="logo" class="img-responsive"/>
		</a>
		<!-- END LOGO -->
		<!-- BEGIN HORIZANTAL MENU -->
		<div class="hor-menu hidden-sm hidden-xs" style="display:none;">
			<ul class="nav navbar-nav">
				<li class="classic-menu-dropdown">
					<a href="/">
						 Home
					</a>
				</li>
		
				<li class="classic-menu-dropdown">
					<a href="/jsp/admin/company/company_list.jsp">
						태권도장 목록 <!-- i class="fa fa-angle-down"></i-->
					</a>
					<!-- a data-toggle="dropdown" data-hover="dropdown" data-close-others="true" href="/jsp/admin/company/company_list.jsp">
						 태권도장 목록
					</a>
					<ul class="dropdown-menu">
						<li>
							<a href="#">
								 Site 신규등록
							</a>
						</li>
					</ul-->
				</li>
							
				<li class="classic-menu-dropdown active">
					<a href="/jsp/admin/company/company_list.jsp">
						상품 목록 <!-- i class="fa fa-angle-down"></i-->
					</a>

					<a data-toggle="dropdown" data-hover="dropdown" data-close-others="true" href="/jsp/admin/company/company_list.jsp">
						 
					</a>
					<ul class="dropdown-menu">
						<li>
							<a href="#">
								 상품 등록
							</a>
						</li>
					</ul>
				</li>				

				<li>
					<span class="hor-menu-search-form-toggler">
						 &nbsp;
					</span>
					<div class="search-form">
						<form class="form-search">
							<div class="input-group">
								<input type="text" placeholder="Search..." class="form-control">
								<div class="input-group-btn">
									<button type="button" class="btn"></button>
								</div>
							</div>
						</form>
					</div>
				</li>
			</ul>
		</div>
		<!-- END HORIZANTAL MENU -->
		<!-- BEGIN RESPONSIVE MENU TOGGLER -->
		<a href="javascript:;" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
			<img src="/assets/img/menu-toggler.png" alt=""/>
		</a>
		<!-- END RESPONSIVE MENU TOGGLER -->
		<!-- BEGIN TOP NAVIGATION MENU -->
		<ul class="nav navbar-nav pull-right">
			
			<!-- BEGIN INBOX DROPDOWN -->
			
			<!-- END INBOX DROPDOWN -->
			
			<!-- BEGIN USER LOGIN DROPDOWN -->
			<li class="dropdown user">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
					<img alt="" src="/assets/img/avatar1_small.jpg"/>
					<span class="username hidden-1024">
						 <%=ss_com_name %>
					</span>
					<i class="fa fa-angle-down"></i>
				</a>
				<ul class="dropdown-menu">
					<li>
						<a href="javascript:;" id="trigger_fullscreen">
							<i class="fa fa-arrows"></i> Full Screen
						</a>
					</li>
					<li>
						<a href="javascript:logoutCompany();">
							<i class="fa fa-key"></i><%=ss_com_id %> Log Out
						</a>
					</li>
				</ul>
			</li>
			<!-- END USER LOGIN DROPDOWN -->
		</ul>
		<!-- END TOP NAVIGATION MENU -->
	</div>
	<!-- END TOP NAVIGATION BAR -->
</div>
<!-- END HEADER -->
<div class="clearfix">
</div>
<!-- BEGIN CONTAINER -->
<div class="page-container" >
	<!-- BEGIN HORIZONTAL MENU PAGE SIDEBAR1 -->
	<div class="page-sidebar navbar-collapse collapse">
		<!--BEGIN: SIDEBAR MENU FOR DESKTOP 데스크탑용-->
		<ul class="page-sidebar-menu hidden-sm hidden-xs" data-auto-scroll="true" data-slide-speed="200">
			<li>
				<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
				<div class="sidebar-toggler hidden-sm hidden-xs">
				</div>
				<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
			</li>			
<%
if(ss_com_type.equals("11")) {
%> 				
			<li class="" >
				<a href="javascript:goPushMessage('<%=ss_com_id%>','<%=ss_com_type%>','<%=sProduct_id%>','<%=sTitle%>','<%=sMsg%>');">
					<i class="fa fa-globe"></i>
					<span class="title">
						 관원들에게 메시지 전송
					</span>
				</a>
			</li>
<%
}
%>			
		
			<!-- END ONLY LEVEL ONE MENU -->

			<!-- li class="last">
				<a href="login.html">
					<i class="fa fa-lock"></i>
					<span class="title">
						 Login Page
					</span>
				</a>
			</li-->
		</ul>
		<!--END: SIDEBAR MENU FOR DESKTOP-->
		<!--BEGIN: HORIZONTAL AND SIDEBAR MENU FOR MOBILE & TABLETS 모바일 & 테블릿용-->
		<ul class="page-sidebar-menu visible-sm visible-xs" data-auto-scroll="true" data-slide-speed="200" style="display:none;">
<%
if(ss_com_type.equals("11")) {
%>			
			<li>
				<a href="javascript:goPushMessage('<%=ss_com_id%>','<%=ss_com_type%>','<%=sProduct_id%>','<%=sTitle%>','<%=sMsg%>');">
					 !관원들에게 메시지 전송
				</a>
			</li>
<%
}
%>			
		</ul>
		<!--END: HORIZONTAL AND SIDEBAR MENU FOR MOBILE & TABLETS-->
	</div>
	<!-- END BEGIN HORIZONTAL MENU PAGE SIDEBAR -->
    
    
	<!-- BEGIN CONTENT -->
	<div class="page-content-wrapper">
		<div class="page-content">
			<!-- BEGIN SAMPLE PORTLET CONFIGURATION MODAL FORM-->
			<div class="modal fade" id="portlet-config" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
							<h4 class="modal-title">팝업창 타이틀</h4>
						</div>
						<div class="modal-body">
							 Widget settings form goes here
						</div>
						<div class="modal-footer">
							<button type="button" class="btn blue">Save changes</button>
							<button type="button" class="btn default" data-dismiss="modal">Close</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
			<!-- END SAMPLE PORTLET CONFIGURATION MODAL FORM-->
			
			<!-- BEGIN PAGE HEADER-->
			<div class="row">
				<div class="col-md-12">
					<!-- BEGIN PAGE TITLE & BREADCRUMB-->
					<h3 class="page-title">
					PRODUCT Information <small>상품 상세정보 입니다.</small>
					</h3>
					<ul class="page-breadcrumb breadcrumb">
						
						<li>
							<i class="fa fa-home"></i>
							<a href="/">
								Home
							</a>
							<i class="fa fa-angle-right"></i>
						</li>
						<li>
							<a href="/jsp/admin/company/company_list.jsp">
								상품 정보
							</a>
							<i class="fa fa-angle-right"></i>
						</li>
						<li>
							<%=sProduct_name %>
						</li>
					</ul>
					<!-- END PAGE TITLE & BREADCRUMB-->
				</div>
			</div>
			<!-- END PAGE HEADER-->
			<!-- BEGIN PAGE CONTENT-->
		
			<div class="row">
				<div class="col-md-6-ws1">
				
					<!-- BEGIN SAMPLE FORM PORTLET-->
					<div class="portlet box yellow">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-reorder"></i>[<%=sProduct_name %>] 기본정보
							</div>
						</div>						
						<div class="portlet-body">
							<table class="table table-striped table-bordered table-advance table-hover">
							<thead>
								<tr>
									<th>
										<i class="fa fa-check"></i> 상품ID
									</th>
									<th>
										<i class="fa fa-gift"></i> 명칭
									</th>
									<th>
										<i class="fa fa-sitemap"></i> 유형
									</th>
									<!-- th>
										<i class="fa fa-calendar"></i> 등록일
									</th>	
									<th>
										<i class="fa fa-bar-chart-o"></i> 가격
									</th -->									
									
								</tr>
							</thead>
							<tbody id="product_info_tbody" >
							
								<!-- ajax call when page loaded. -->
							
							</tbody>
							</table>
						</div>
						
						 
						<div class="portlet-body">
							<table class="table table-striped table-bordered table-advance table-hover">
							<tbody id="product_info_tbody2" >
							
								<!-- ajax call when page loaded. -->
							
							</tbody>
							</table>
							
						</div>	
						

					
						<div class="portlet-body form">
							<!-- BEGIN FORM-->
							<form action="#" id="frmSendOrder" class="form-horizontal">
								<div class="form-body">
                                   <!----------------------------------------------------------------------BEGIN Form Data-------------------------------------------------------------------------------->     
                                    <!-- div class="row">
										<div class="col-md-12">
											<div class="form-group">
												<label class="control-label col-md-2">옵션1</label>
												<div class="col-md-10">
													<select class="form-control" id="p_sub_id" name="p_sub_id" onchange="SetOption(this.value);" />
														
														-- ajax call when page loaded. --
														
													</select>
												</div>
											</div>
										</div>
									</div-->
									<!--/row-->
                                    <!-- div class="row" id="option2" >
										<div class="col-md-12">
											<div class="form-group">
												<label class="control-label col-md-2">상품선택</label>
												<div class="col-md-10">
													<select class="form-control" id="p_option_id" name="p_option_id" onchange="SetOption(this.value);" />
														
												
														
													</select>
												</div>
											</div>
										</div>

									<div class="row">
										<div class="col-md-6">
											<div class="col-md-offset-3 col-md-9">
												<div type="button" class="btn yellow" id="submit_btn" >선택완료</div>
												
												
											</div>
										</div>
										<div class="col-md-6">
										</div>
									</div>

									</div-->
									<!--/row-->									
                                    <!----------------------------------------------------------------------END Form Data-------------------------------------------------------------------------------->
									
<%
if(ss_com_type.equals("1")) {
%>										
									<div class="row">
										<div class="col-md-12">
											<div class="form-group">
												<label class="control-label col-md-2">메시지</label>
												<div class="col-md-10">
													<input type="text" class="form-control"  id="p_msg" name="p_msg" value="안녕하세요~ <%=sMsg%>">
												</div>
											</div>
										</div>
										<!--/span-->
									</div>										
									<div class="row">
										<div class="col-md-12">
											<div class="">
												<div type="button" class="btn yellow" id="submit_btn" onclick="goPushMessage('<%=ss_com_id%>','<%=ss_com_type%>','<%=sProduct_id%>','<%=sTitle%>','<%=sMsg%>'); ">
												학부모님들께 메시지 전송
												</div>
											</div>
										</div>
										<div class="col-md-6">
										</div>
									</div>
<%
}
%>																
								</div>
								<!-- div class="form-actions fluid">
									<div class="row">
										<div class="col-md-6">
											<div class="col-md-offset-3 col-md-9">
												<div type="button" class="btn yellow" id="submit_btn" >선택완료</div>
								
											</div>
										</div>
										<div class="col-md-6">
										</div>
									</div>
								</div-->
							</form>
							<!-- END FORM-->
						</div>					
					
					</div>					
					
					
					<!-- div class="portlet box green">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-reorder"></i>상품 세부정보
							</div>
						</div>						
						<div class="portlet-body">
							<table class="table table-striped table-bordered table-advance table-hover">
							<tbody id="product_info_sub_tbody" >
							
								-- ajax call when page loaded. --
							
							</tbody>
							</table>
							
						</div>							
					</div -->					
					
					<div class="portlet box green">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-reorder"></i>상품 세부정보
							</div>
						</div>						
						<div class="portlet-body">
							<table class="table table-striped table-bordered table-advance table-hover">
							<tbody id="product_info_img_tbody" >
							
								<!-- ajax call when page loaded. -->
							
							</tbody>
							</table>
							
						</div>							
					</div>						
					
					
					<div class="portlet box green">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-reorder"></i>사이즈별 주문 목록
							</div>
						</div>					
						<div class="portlet-body">
							<table class="table table-striped table-bordered table-advance table-hover">
							<thead>
							<tr>
								<th>
									<i class="fa fa-check"></i> 상품ID
								</th>
								<th class="hidden-xs">
									<i class="fa fa-gift"></i> 상품명칭
								</th>								
								<th>
									<i class="fa fa-arrows"></i> 사이즈
								</th>
								<th>
									<i class="fa fa-shopping-cart"></i> 수량
								</th>
								
							</tr>
							</thead>
							<tbody id="companysize_order_list_tbody" >
							
								<!-- ajax call when page loaded. -->
							
							</tbody>
							</table>

						</div>	
					</div>									
					
					

                
				<!-- div class="col-md-6-ws2 cm6-w">
					<!-- BEGIN PHONE AJAX TABLE->
					<div class="portlet box blue">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-apple"></i><i class="fa fa-android"></i><i class="fa fa-windows"></i>Preview
							</div>
							<div class="tools">
								<a href="javascript:;" class="collapse">
								</a>
								<a href="javascript:;" class="reload">
								</a>
							</div>
						</div>
						<div class="portlet-body phone-body">
							<table align="center">
							<tbody>
							<tr>
								<td>
                                   <div class="phone_contents">
                                   </div>
                                </td>
							</tr>
							</tbody>
							</table>
						</div>
					</div>
					<!-- END PHONE AJAX TABLE->
				</div-->
                
			</div>
			<!-- END PAGE CONTENT-->
		</div>
	</div>
	<!-- END CONTENT -->
</div>
<!-- END CONTAINER -->
<!-- BEGIN FOOTER -->
	<div class="footer">
		<div class="footer-inner">
			 2014 &copy; BIZTK.
		</div>
		<div class="footer-tools">
			<span class="go-top">
				<i class="fa fa-angle-up"></i>
			</span>
		</div>
	</div>
	<!-- END FOOTER -->
    <!-- The blueimp Gallery widget -->
    <div id="blueimp-gallery" class="blueimp-gallery blueimp-gallery-controls" data-filter=":even">
        <div class="slides">
        </div>
        <h3 class="title"></h3>
        <a class="prev">
             ‹
        </a>
        <a class="next">
             ›
        </a>
        <a class="close white">
        </a>
        <a class="play-pause">
        </a>
        <ol class="indicator">
        </ol>
    </div>
	<!-- BEGIN CORE PLUGINS -->
	<!--[if lt IE 9]>
<script src="/assets/plugins/respond.min.js"></script>
<script src="/assets/plugins/excanvas.min.js"></script> 
<![endif]-->
	<script src="/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
	<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
	<script src="/assets/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script>
	<script src="/assets/plugins/jquery-ui-touch-punch/jquery.ui.touch-punch.min.js" type="text/javascript"></script>
	<script src="/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="/assets/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
	<script src="/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
	<script src="/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
	<script src="/assets/plugins/jquery.cokie.min.js" type="text/javascript"></script>
	<script src="/assets/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
	<!-- END CORE PLUGINS -->
    <script src="/assets/scripts/core/app.js"></script>
    <script src="/assets/scripts/custom/components-pickers.js"></script>
    
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script src="/assets/scripts/core/app.js"></script>
	<script src="/assets/scripts/custom/components-jqueryui-sliders.js"></script>
    <script src="/assets/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/assets/plugins/bootstrap-colorpicker/js/bootstrap-colorpicker.js"></script>
    <script type="text/javascript" src="/assets/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <!-- END PAGE LEVEL PLUGINS -->
	<script>
	jQuery(document).ready(function() {       
	   // initiate layout and plugins
	   App.init();  // set current page
	   ComponentsjQueryUISliders.init();
	   ComponentsPickers.init();
	});
	</script>
	<!-- END JAVASCRIPTS -->
	</body>
	<!-- END BODY -->
	</html>