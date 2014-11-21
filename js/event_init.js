/**
 * ====================================================================
 * ShopEvent 클래스 
 * @param 
 * @return
 * ====================================================================
 */

var ShopEvent = function () {
    return {
    	init: function () {
    		wrapWindowByMask();
    		// 주문신청
	    	$("#order_submit").click(function () {
	//    		capture();
	    		order_process();
	    		return false;
	    	});

	    	// 글 수정 버튼
	    	$("#message_btn_onoff").click(function () {
	    		var m = "";
	    		if( $(this).text() == "글 수정" ) {
	    			m = "닫 기";
	    			$("#message_view").hide();
	    			$("#message_send").show();
	    			$("#message_write div").eq(1).show();
	    		} else {
	    			m = "글 수정";
	    			$("#message_view").show();
	    			$("#message_send").hide();
	    			$("#message_write div").eq(1).hide();
	    		}
	    		$(this).text(m);
	    		return false;
	    	});
	    	
	    	// 메시지 작성
	    	$("#message_send").click(function () {
	    		message_process();
	    		return false;
	    	});
	    	
	    	// Email 전송
	    	$("#email_send").click(function () {
	    		$('#mask, #loadingImg').show();	    		
	    		capture();
	    		
	    		return false;
	    	});
	    	
	    	// 카카오링크 복사
	    	//setCopyClipBoard();
	    	$("#kakao_link").click(function () {
	    		kakaolink_process();
	    		return false;
	    	});
    	
    	}
    }
}();

function kakaolink_process() {
	var s = $("#mesage_view_title strong").text();
	if( s == "") {
		alert("메시지를 작성 하고 시도해 주세요");
		return;
	}
	var u =  location.href;
	u = u.replace("com_type","x");
	var n = $("#page-title div h1").text();
    kakao.link("talk").send({
        msg : s,
        url : u,  
        appid : "m.bizTK.com",
        appver : "2.0",
        appname : n,
        type : "link"
    });
}
function setCopyClipBoard() {
	
  var clip = new ZeroClipboard($("#clip_board"));

  clip.on("load", function (client) {

    client.on("complete", function (client, args) {
    });
  });

  clip.on("noFlash", function (client) {
    $(".demo-area").hide();
  });

  clip.on("wrongFlash", function (client, args) {
    $(".demo-area").hide();
  });


  $("#clear-test").on("click", function () {
    $("#fe_text").val("Copy me!");
    $("#testarea").val("");
  });

/*	
  //플래시화를 위한 swf파일 위치 정의
  ZeroClipboard.moviePath = '/swf/ZeroClipboard.swf'; 
  //객체 생성
  clip = new ZeroClipboard.Client(); 
  clip.setHandCursor( true ); 

  clip.addEventListener('mouseOver', function (client) { 
    copyText  = "어쩌구 복사할 내용을 넣어준다.";
    clip.setText( copyText );
  }); 


  clip.addEventListener('complete', function (client, text) {
    alert("복사완료:" + text );
    //복사 완료 후 크롬에서는 다시 호출하지 않아도 복사가 잘 되었는데 IE8에서는 재호출을 꼭 해주어야 한다;
    setCopyClipBoard();
  });  

  //플래시화 할 객체 대상 및 해당 영역을 정의
  clip.glue( 'btn_copy', 'div_copy' );
*/  
}

function email_process(img_url) {
	var obj = new Object();
	obj.product_id = $("input[name=product_id]").val();
	obj.com_id = $("input[name=com_id]").val();	
	obj.product_title = $("input[name=product_title]").val();
	obj.product_detail = "gggggggggggggggggggggggggggggggggg";
	//obj.email_image_name = "http://113.130.69.111:8099/images/conti02.png";
	obj.email_image_name = img_url;

	for( i in obj) {
		if(obj[i] == "") {
//			alert("항목이 입력되지 않았습니다.");
			return;
		}
	}
	
	$.ajax({   
		url: "/setCompanyEmail.jsp",
		type: "POST",
		dataType: "json",
		data: {email_set: JSON.stringify(obj)},
		success: function(data) {
			var msg = "";
			switch(data.RESULT_KEY) {
				case "OK" 	: msg = "이메일을 보냈습니다.";
						break;
				case "NO" 		: msg = "정상 적인 접근이 아닙니다.";
						break;
				default		: msg = "잠시 후 다시 시도해 주십시요.";
			}
			//로딩 해제
			$('#mask, #loadingImg').hide();
			alert(msg);
		},
		error: function(e) {
			//로딩 해제
			$('#mask, #loadingImg').hide();			
			alert("통신중 에러"); 
		}		
	});
		
}

function message_process() {
	var obj = new Object();
	obj.product_id = $("input[name=product_id]").val();
	obj.com_id = $("input[name=com_id]").val();
	obj.com_product_title = $("input[name=com_product_title]").val();
	obj.com_product_detail = $("#com_product_detail").val().replace(/\n/g, '<br />');;
	
	for( i in obj) {
		if(obj[i] == "") {
			alert("항목이 입력되지 않았습니다.");
			return;
		}
	}
	
	$.ajax({   
		url: "/setCompanyOrder.jsp",
		type: "POST",
		dataType: "json",
		data: {message_set: JSON.stringify(obj)},
		success: function(data) {
			var msg = "";
			switch(data.RESULT_KEY) {
				case "INSERT" 	: msg = "글이 등록 되었습니다.";
						break;
				case "UPDATE" 	: msg = "글이 수정 되었습니다.";
						break;
				case "NO" 		: msg = "정상 적인 접근이 아닙니다.";
						break;
				default		: msg = "잠시 후 다시 시도해 주십시요.";
			}
			alert(msg);
			if(data.RESULT_KEY == "INSERT" || data.RESULT_KEY == "UPDATE") {
				$("#mesage_view_title strong").text(obj.com_product_title);
				$("#mesage_view_detail").html(obj.com_product_detail);
				$("#message_write div").eq(1).hide(); // 입력창 숨기기
				$("#message_send").hide(); // 글 등록버튼 숨기기
				$("#message_view").show(); // 뷰 보이기
				$("#message_btn_onoff").text("글 수정");
			}
		},
		error: function(e) {
			alert("통신중 에러");
		}		
	});
	
}
function order_process() {
	if(!confirm("신청 하시겠습니까?")) return;
	var obj = new Object();
	obj.product_id = $("input[name=product_id]").val();
	obj.com_id = $("input[name=com_id]").val();
	obj.order_student_name = $("input[name=order_student_name]").val();
	obj.order_parent_name = $("input[name=order_parent_name]").val();
	obj.order_user_hp = $("input[name=order_user_hp]").val();
	obj.order_size = $("#order_size option:selected").val();
	obj.order_color = $("#order_color option:selected").val();
	obj.amount = $("input[name=product-quantity]").val();
	
	for( i in obj) {
		if(obj[i] == "") {
			alert("필수 항목이 입력되지 않았습니다.");
			return;
		}
	}

	$.ajax({   
		url: "/setUserOrder.jsp",
		type: "POST",
		dataType: "json",
		data: {order_set: JSON.stringify(obj)},
		success: function(data) {
			var msg = "";
			switch(data.RESULT_KEY) {
				case "INSERT" 	: msg = "주문신청이 완료 되었습니다.";
						break;
				case "UPDATE" 	: msg = "주문신청이 변경 되었습니다.";
						break;
				case "NO" 		: msg = "정상 적인 접근이 아닙니다.";
					break;
				default		: msg = "잠시 후 다시 시도해 주십시요.";
			}
			alert(msg);
		},
		error: function(e) {
			alert("통신중 에러");
		}		
	});

}


function capture() {
    html2canvas($("#tabs-1"), {
        onrendered: function (canvas) {
            //Set hidden field's value to image data (base-64 string)
    	var img = canvas.toDataURL("image/png");
//    	var s = img.substr(0,1960000);
    	$("input[name=image_data]").val(img);
//    	alert($("input[name=image_data]").val());
/*
    		$.ajax({   
	 			url: "/setContentImage.jsp",
	 			type: "POST",
	 			dataType: "json",
	 			data: {image_data: img},
	 			success: function(data) {
	 				if( data.RESULT_KEY == "OK") {
	 					alert("이미지 저장 완료 : " + data.imgPath);
	 				}
	 			},
	 			error: function(e) {
	 				alert("통신중 에러");
	 			}		 			
	        });
*/
//    	$("form[name=order_form]").submit();
    	//$("form[name=order_form]").attr("action","/setContentImage.jsp");
    	$("#order_form").attr("enctype","multipart/form-data");
    	$("#order_form").ajaxSubmit({
    		dataType: "json",
 			url: "/setContentImage.jsp",
            beforeSend: function () {
//                alert("시작전");
            },
            uploadProgress: function (event, position, total, percentComplete) {
//                alert("업로드중");
            },
            complete: function (xhr) {
//                alert("완료");
            },
            success: function(data){
 				if( data.RESULT_KEY == "OK") {
 					//alert("이미지 저장 완료 : " + data.imgPath);
 					email_process(data.imgPath); // 이메일 전송 프로세스.
 				}            	
            },
    		error: function(e) {
    			//로딩 해제
    			$('#mask, #loadingImg').hide();
    			alert("통신중 에러");
    		}	            
        });
    	$("#order_form").attr("application/x-www-form-urlencoded");

    	
    }	
    });
}

function wrapWindowByMask() {
	//화면의 높이와 너비를 구한다.
	var maskHeight = $(document).height();  
	var maskWidth = $(document).width();
	
	var mask = "<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
	var loadingImg = '';
	
	loadingImg += "<div id='loadingImg' style='position:absolute; left:50%; top:40%; display:none; z-index:10000;'>";
	loadingImg += "	<img src='/images/loading-animation-3.gif'/>"; 
	loadingImg += "</div>";	
	//화면에 레이어 추가 
	$('body')
		.append(mask)
		.append(loadingImg)
      
	//마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
	$('#mask').css({
			'width' : maskWidth
			, 'height': maskHeight
			, 'opacity' : '0.3' 
	});
/*
	//마스크 표시
 	$('#mask').show();    
	//로딩중 이미지 표시
	$('#loadingImg').show();
*/	
}
