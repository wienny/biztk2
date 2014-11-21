<%@	page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8"
	trimDirectiveWhitespaces="true"
	import="java.io.*,
			nano.biz.*,
			nano.util.*,
			nano.*"
%>
<%
	String product_id  = request.getParameter("product_id");
	String com_id  = request.getParameter("com_id");
	String com_type  = request.getParameter("com_type");
//	product_id = "pro_001";
//	com_id = "v1";
//	com_type = "1";	

	String browser = request.getHeader("User-Agent");
	boolean kakao_btn = false;
	if (browser.indexOf("Android") > 0) {
		kakao_btn = true;
	} else if (browser.indexOf("iPhone") > 0) {
		kakao_btn = true;
	}

	if(product_id == null || com_id == null ) {
		out.println("<script>alert('정상적인 접근이 아닙니다.');</script>");
		return;
	}

	BizUserOrder biz	= new BizUserOrder(request, response);
/***** 임시 *****/

	biz.addProperty("product_id",product_id);
	biz.addProperty("com_id",com_id);
	
	biz.getProductInfo();
	ListData list = (ListData)request.getAttribute("getProductInfo");
	if (list == null) {
		list = new ListData();
	}	
	String product_title = list.get(0).get("product_title","");
	
	// Company 
	BizCompanyOrder bizCom	= new BizCompanyOrder(request, response);
	bizCom.addProperty("product_id",product_id);
	bizCom.addProperty("com_id",com_id);
	bizCom.getExplainInfo();
	Record rec_exp = (Record)request.getAttribute("getExplainInfo");
	if (rec_exp == null) {
		rec_exp = new Record();
	}
	String com_product_title = rec_exp.get("com_product_title","");
	String com_product_detail_view = rec_exp.get("com_product_detail","");
	String com_product_detail = com_product_detail_view.replaceAll("<br />", "\r\n");
	
	// Company name
	String com_name = bizCom.getCompanyName();
	if(com_name.equals("")) com_name = "bizTK";
/**************/

%>
<!DOCTYPE html> 
<html dir="ltr" lang="ko">

<head>
    
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="author" content="SemiColonWeb" />
	<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
    
    <!-- ============================================
        Stylesheets
    ============================================= -->
	<link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700|Merriweather:400,400italic" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="/css/animate.css" type="text/css" />
    <link rel="stylesheet" href="/css/style.css" type="text/css" />
    <link rel="stylesheet" href="/css/font-icons.css" type="text/css" />
    <link rel="stylesheet" href="/css/magnific-popup.css" type="text/css" />
    <link rel="stylesheet" media="only screen and (-webkit-min-device-pixel-ratio: 2)" type="text/css" href="css/retina.css" />
    <link rel="stylesheet" href="/css/custom.css" type="text/css" />
    
    
    <link rel="stylesheet" href="/css/responsive.css" type="text/css" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
    <!--[if lt IE 9]>
    	<script src="http://css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js"></script>
    <![endif]-->
	
	
    <!-- ============================================
        External JavaScripts
    ============================================= -->
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript" src="/js/plugins.js"></script>
	<script type="text/javascript" src="/js/jquery.form.js"></script>	
    <script type="text/javascript" src="/js/jquery.lava.js"></script>
    <script type="text/javascript" src="/js/custom.js"></script>
    <script type="text/javascript" src="/js/event_init.js"></script>

    <script type="text/javascript" src="/js/html2canvas.js"></script>
	<script type="text/javascript" src="/js/kakao.link.js"></script>    
    <script type="text/javascript" src="/js/ZeroClipboard.min.js"></script>
    
    <!-- ============================================
        Document Title
    ============================================= -->
	<title>BizTK</title>
	
	<script type="text/javascript">
		$(document).ready(function(){
			ShopEvent.init();

			
	       // html2canvas(document.body, {            onrendered: function(canvas) {                document.body.appendChild(canvas);            }        });			
		});
	</script>
</head>

<body class="stretched">


    <div id="wrapper" class="clearfix">
    
        <!-- ============================================
            Top Bar
        ============================================= -->
        <div id="top-bar" style="display:none">

            <div id="top-bar-wrapper" data-sticky="true" >
        
                <div class="container clearfix" >
                
                    
                    <div class="top-links fright" >
                    
                        <ul>
                        
                            <li><a href="#"><i class="icon-cart-5"></i> Cart (3)</a>

                                <div class="top-link-section clearfix" id="top-cart-info">

                                    <div>
                                        <div class="top-cart-item clearfix">
                                            <div class="top-cart-item-image">
                                                <a href="#"><img src="/images/shop/cart/1.jpg" alt="Orange Belt" /></a>
                                            </div>
                                            <div class="top-cart-item-desc">
                                                <a href="#">Orange Belt</a>
                                                <span class="top-cart-item-price">$19.99</span>
                                                <span class="top-cart-item-quantity">x 2</span>
                                            </div>
                                        </div>
                                        <div class="top-cart-item clearfix">
                                            <div class="top-cart-item-image">
                                                <a href="#"><img src="/images/shop/cart/2.jpg" alt="Trekking Shoes" /></a>
                                            </div>
                                            <div class="top-cart-item-desc">
                                                <a href="#">Trekking Shoes</a>
                                                <span class="top-cart-item-price">$39</span>
                                                <span class="top-cart-item-quantity">x 1</span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="top-checkout-wrap">
                                        <span class="fleft top-checkout-price">$78.98</span>
                                        <a href="#" class="fright nomargin simple-button">Checkout</a>
                                    </div>

                                </div>

                            </li>
                            <li><a href="#"><i class="icon-lock-6"></i> Login</a>

                                <div class="top-link-section">
                                    <form action="#" method="post" id="top-login-form">
                                        <label>Username:</label>
                                        <input type="text" class="input-block-level" name="top-login-username" id="top-login-username" value="" />
                                        <label>Password:</label>
                                        <input type="password" class="input-block-level" name="top-login-password" id="top-login-password" value="" />
                                        <button class="simple-button nomargin">Login</button>
                                        <a href="#" class="forgot-password-link">Forgot Password?</a>
                                    </form>
                                </div>

                            </li>
                            <li><a href="#"><i class="icon-share"></i> Log Out</a></li>
                            <li><a href="#"><i class="icon-pencil"></i> Member Join</a></li>
                            <li><a href="#"><i class="icon-user"></i> 김길동</a></li>
                            
                            <li class="full-icon"><a href="#"><i class="icon-search-3"></i></a>

                                <div class="top-link-section" id="top-search-wrap">
                                    <form action="search.html" method="get" id="top-search">
                                        <input type="text" class="input-block-level" name="q" id="top-search-input" value="" placeholder="Type to Search" />
                                    </form>
                                </div>

                            </li>
                        
                        </ul>
                    
                    </div>
                
                
                </div>

            </div>
        
        </div>
        
        
        <div id="header">
        
        
            <div id="header-wrap" class="header-sticky" data-sticky="true">

                <div class="container clearfix">
                
                    
                    <div class="col_one_third nobottommargin">
                        
                        <!-- ============================================
                            Logo
                        ============================================= -->
                        <div id="logo">
                    
                        <a href="index.html" class="standard-logo"><img src="/images/logo.png" alt="BizTK" title="BizTK" height="120" /></a>
                        <a href="index.html" class="retina-logo"><img src="/images/logo-retina.png" alt="BizTK" title="BizTK" width="154" height="120" /></a>
                    
                    </div>
                    
                    </div>
                    
                    <div class="col_two_third col_last nobottommargin">
                    
                        <!-- ============================================
                            Primary Menu
                        ============================================= -->
                        <div id="primary-menu" class="primary-menu3" style="display:none">
                            

                            <div class="responsive-menu"><i class="icon-menu-6"></i></div>
                        
                            <ul>
                        
                            <li class="current"><a href="index.html"><div>Home</div></a></li>
                            <li><a href="shop.html"><div>Shop</div></a></li>
                            <li><a href="side-navigation.html"><div>Event</div></a></li>
                            <li><a href="portfolio-4.html"><div>Member</div></a>

                                <ul>
                                    <li><a href="portfolio-3.html"><div>개인정보</div></a></li>
                                    <li><a href="portfolio-4.html"><div>주문정보</div></a></li>
                                </ul>

                            </li>
                            <li><a href="contact.html"><div>Contact Us</div></a></li>
                        
                        </ul>
                        
                        </div>
                    
                    </div>
                
                
                </div>

            </div>
        
        
        </div>
        <!-- ============================================
                END Top Bar
            ============================================= -->
        
        <div id="content">
        
            <!-- ============================================
                Page Title
            ============================================= -->
            <div id="page-title" class="title-bg-image01">
            
                <div class="container">
                
                    <h1><%=com_name %></h1>
                </div>
            
            </div>
        
            
            <div class="content-wrap">
            
                <div class="container clearfix">
                
                    <!-- ============================================
                        Content Start
                    ============================================= -->
                    <div class="col_half nobottommargin">
                    
                        <div class="fslider shop-product-slider" data-slideshow="false" data-animation="fade" data-speed="500" data-easing="easeOutQuad" data-arrows="false" data-thumbs="true" data-lightbox="gallery">
                        
                            <div class="flexslider">
                            
                            
                                <div class="slider-wrap">
                                
				<%
					// 이미지 갤러리
					for(int i=0; i < list.size(); i++)	{
						Record row = list.get(i);
						String full_image = "/file/images/full/" + row.get("image_filename","");
						String thumb_image = "/file/images/thumb/" + row.get("image_filename","");
				%>                                
                                    <div class="slide" data-thumb="<%=thumb_image %>">
                                    
                                        <a href="<%=full_image %>" data-lightbox="gallery-item"><img src="<%=full_image %>" alt="Shop Image 1" /></a>
                                    
                                    </div>
				<% 	}%>
                                    
                                
                                
                                </div>
                            
                            
                            </div>
                        
                        </div>

                    </div>

                <form  method="post" id="order_form" name="order_form" >
					<input type="hidden" name="com_id" value="<%=com_id %>" />
					<input type="hidden" name="product_id" value="<%=product_id %>" />
					<input type="hidden" name="product_title" value="<%=product_title %>" />
					<input type="hidden" name="com_type" value="<%=com_type %>" />
					<input type="hidden" name="image_data" value="" />
                    <div class="product-single entry_content col_half nobottommargin col_last">


                        <h2>통풍 점퍼 NEPA Wind Break Red2146784</h2>

                        <div class="product-meta clearfix">
                        
                            <div class="product-price">￦60,000</div>

                            <div class="product-rating" style="display:none">
                                <i class="icon-star-6"></i>
                                <i class="icon-star-6"></i>
                                <i class="icon-star-6"></i>
                                <i class="icon-star-5"></i>
                                <i class="icon-star-4"></i>
                            </div>
                            <div class="clear margin-bottom-10"></div>
							<span style="font-size:11px">- 본 제품은 체육관용으로 제작한 것으로 NEPA 매장에서는 판매하지 않습니다.</span>
                            <div class="product-availability"></div>
                            
                        </div>
                        
                    <%if( "0".equals(com_type) ) { %>
                        <!--============================================================
                             send e-mail
                        =============================================================-->
                        <div class="margin-bottom-10">
                        
                                    <div class="clear"></div>
                                    
                                    <div class="col_full nobottommargin">
                                        <button class="simple-button nomargin" type="submit" id="email_send" name="email_send" value="submit">Send e-mail</button>
                                    </div>
                        </div>
                        <!--============================================================
                             send e-mail
                        =============================================================-->
                    <%} %>
                    <%if( !"0".equals(com_type) || "1".equals(com_type) ) { %>
                        <!--============================================================
                             message input start
                        =============================================================-->
                      <div id="message_view">
                        <div class="margin-bottom-10">
                        
                                    <div class="clear"></div>
                                            
                                    <div id="mesage_view_title" class="col_full nobottommargin">
                                        <strong style="color:#000;"><%=com_product_title %></strong>
                                    </div>
                                    
                                    <div class="clear margin-bottom-10"></div>
                                            
                                    <div id="mesage_view_detail" class="col_full nobottommargin"><%=com_product_detail_view %>
                                    </div>
                        </div>
                     </div>
                        <!--============================================================
                             message input end
                        =============================================================-->
                    <%} %>
                    <%if( !("0".equals(com_type) || "1".equals(com_type)) ) { %>
                      <div id="user_page">
                        <div class="col_half nobottommargin">
                         	<label for="template-quoteform-name">사이즈 <small>*</small></label>
                            <select name="" id="order_size" class="input-block-level">
                                <option value="">-- Select Size --</option>
				<%
						String [] size = list.get(0).get("size_list").toString().split(",");
						for(int i=0; i<size.length; i++) {
							String v = size[i].substring(0,3);
				%>                                
                                <option value="<%=v %>"><%=size[i] %></option>
				<%		} %>                                
                            </select>
                        </div>

<!--                        <div class="col_half nobottommargin col_last">-->
<!--                            <select name="" id="order_color" class="input-block-level">-->
<!--                                <option value="">-- Select Color --</option>-->
<!--                                <option value="Blue">Blue</option>-->
<!--                                <option value="Red">Red</option>-->
<!--                                <option value="Maroon">Maroon</option>-->
<!--                                <option value="Black">Black</option>-->
<!--                                <option value="White">White</option>-->
<!--                                <option value="Beige">Beige</option>-->
<!--                            </select>-->
<!--                        </div>-->

                        <div class="clear"></div>
                        
						 <div class="col_half nobottommargin">
							<label for="template-quoteform-name">수량 <small>*</small></label>
						</div>
						<div class="clear"></div>
                        <div class="product-quantity clearfix">
                            <span id="product-quantity-minus" class="icon-minus"></span>
                            <input type="text" value="1" id="product-quantity" name="product-quantity" />
                            <span id="product-quantity-plus" class="icon-plus"></span>
                        </div>

                        <div class="clear bottommargin"></div>

                        <div class="clear"></div>
                        
                        <div class="col_half nobottommargin">
                                <label for="template-quoteform-name">부모님 성함 <small>*</small></label>
                                <input type="text" class="required input-block-level" id="order_parent_name" name="order_parent_name" value="" placeholder="부모님 성함" />
                        </div>
                        
                        <div class="col_half nobottommargin col_last">
                                <label for="template-quoteform-name">학생 이름 <small>*</small></label>
                                <input type="text" class="required input-block-level" id="order_student_name" name="order_student_name" value="" placeholder="학생이름" />
                        </div>
                        
                        <div class="col_half nobottommargin">
                                <label for="template-quoteform-name">연락처 <small>*</small>예)01022223333</label>
                                <input type="text" class="required input-block-level" id="order_user_hp" name="order_user_hp" maxlength="11" value="" placeholder="01xyyyyzzzz" />
                        </div>
                        
                        <div class="clear"></div>
                        
                        <div id="order_submit">
                        <a href="#" class="simple-button nomargin fleft"><i class="icon-cart-add"></i> 주문완료</a>
                        
						</div>
                    </div>
                  	<%} %>
                  	<%if( "1".equals(com_type) ) {
                  		String message_write_style = "display:block";
                  		String message_btn = "닫 기";
                  		String kakao_style = "display:none";
                  		if(kakao_btn) kakao_style = "display:inline-block";
                  		
                  		if(com_product_title.length() > 0) {
                  			message_write_style = "display:none";
                  			message_btn = "글 수정";
                  		}
                  	%>
                        <!--============================================================
                             message input start
                        =============================================================-->
                        <div id="message_write">
                                    <div class="clear"></div>
								<div style="<%=message_write_style %>">                                            
                                    <div class="col_full nobottommargin">
                                        <label for="template-contactform-subject">Subject <small>*</small></label>
                                        <input type="text" name="com_product_title" value="<%=com_product_title %>" class="required input-block-level" />
                                    </div>
                                    
                                    <div class="clear"></div>
                                            
                                    <div class="col_full nobottommargin">
                                        <label for="template-contactform-message">Message <small>*</small></label>
                                        <textarea class="required input-block-level" id="com_product_detail" name="com_product_detail" rows="10" cols="30"><%=com_product_detail %></textarea>
                                    </div>
                                    
                                    <div class="col_full nobottommargin hidden">
                                        <input type="text" id="template-contactform-botcheck" name="template-contactform-botcheck" value="" class="input-block-level" />
                                    </div>
                                </div>
									<div class="clear margin-bottom-30"></div>	                                
                                    <div class="col_full nobottommargin">
                                    	<button class="simple-button nomargin" id="message_btn_onoff" name="message_btn_onoff" value="submit">글 수정</button>
                                        <button class="simple-button nomargin" id="message_send" name="message_send" value="submit" style="<%=message_write_style %>">글 등록</button>
                                        <button class="simple-button-kakao nomargin" id="kakao_link" name="kakao_link" value="submit" style="<%=kakao_style %>">카카오톡 보내기</button>
                                    </div>
                                    
                        </div>
                        <!--============================================================
                             message input end
                        =============================================================-->
					<%} %>                        
						<div class="clear bottommargin"></div>
					</div>
					
				<div class="clear margin-bottom-30"></div>											                                            
                <div class="clear margin-bottom-10 line-dark"></div>
                <!--===================================================
                      begin detail tab
                =====================================================-->
                <div class="tabs" id="product-tabs">
                            

                            
                            <div class="tab-container">

                                <div class="tab-content clearfix" id="tabs-1">
                                <%=list.get(0).get("product_detail") %>
                                </div>


                            </div>
                        
                        </div>

                        <script>
                        $(function() {
                            $( "#product-tabs" ).tabs({ show: { effect: "fade", duration: 400 } });
                        });
                        </script>


                        <div class="entry_share clearfix">
                        
                            <span><strong>Share this Post:</strong></span>
                            
                            <a href="#" class="ntip" title="Share on RSS"><img src="/images/icons/social/post/rss.png" alt="RSS" /></a>
                            <a href="#" class="ntip" title="Email this Post"><img src="/images/icons/social/post/email.png" alt="Email" /></a>
                        
                        </div>


                        <div class="line"></div>
                <!--====================================================
                      end detail tab
                ======================================================-->
                </div>

                

            <!-- ============================================
                Content End
            ============================================= -->
            </div>
        
        
        </div>
        
        
        
        
        
        <div class="clear"></div>
        
        
        <!-- ============================================
            Copyrights
        ============================================= -->
        <div id="copyrights" class="copyrights-dark">
        
            <div class="container clearfix">
        
            
                <div class="col_half">
                
                    Copyrights &copy; 2014 &amp; All Rights Reserved by NANODMS Inc.
                
                </div>
                
                <div class="col_half col_last tright">
                
                    <a href="#">Privacy Policy</a><span class="link-divider">/</span><a href="#">Terms of Service</a><span class="link-divider">/</span><a href="#">FAQs</a>
                
                </div>
            
            
            </div>
        
        </div>


    </div>


    <div id="gotoTop"><i class="icon-arrow-up"></i></div>




</body>

</html>