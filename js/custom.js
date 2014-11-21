var $ = jQuery.noConflict();

function image_preload(selector, parameters) {
	var params = {
		delay: 250,
		transition: 400,
		easing: 'linear'
	};
	$.extend(params, parameters);
		
	$(selector).each(function() {
		var image = $(this);
		image.css({visibility:'hidden', opacity: 0, display:'block'});
		image.wrap('<span class="preloader" />');
		image.one("load", function(evt) {
			$(this).delay(params.delay).css({visibility:'visible'}).animate({opacity: 1}, params.transition, params.easing, function() {
				$(this).unwrap('<span class="preloader" />');
			});
		}).each(function() {
			if(this.complete) $(this).trigger("load");
		});
	});
}


function grayscale(src){
    var canvas = document.createElement('canvas');
    var ctx = canvas.getContext('2d');
    var imgObj = new Image();
    imgObj.src = src;
    canvas.width = imgObj.width;
    canvas.height = imgObj.height; 
    ctx.drawImage(imgObj, 0, 0); 
    var imgPixels = ctx.getImageData(0, 0, canvas.width, canvas.height);
    for(var y = 0; y < imgPixels.height; y++){
        for(var x = 0; x < imgPixels.width; x++){
            var i = (y * 4) * imgPixels.width + x * 4;
            var avg = (imgPixels.data[i] + imgPixels.data[i + 1] + imgPixels.data[i + 2]) / 3;
            imgPixels.data[i] = avg; 
            imgPixels.data[i + 1] = avg; 
            imgPixels.data[i + 2] = avg;
        }
    }
    ctx.putImageData(imgPixels, 0, 0, 0, 0, imgPixels.width, imgPixels.height);
    return canvas.toDataURL();
}


jQuery(document).ready(function($) {
                
        
        if ( $().superfish ) {
        
            $('#primary-menu ul,.top-links > ul').superfish({
                popUpSelector: 'ul,.top-link-section',
    			delay: 250,
                speed: 300,
                animation: {opacity:'show',height:'show'},
                animationOut:  {opacity:'hide',height:'hide'},
    			cssArrows: false
    		});
        
        }
        
        
        $( '#primary-menu ul li:has(ul)' ).addClass('sub-menu');
        $( '.sub-menu ul li' ).addClass( 'noLava' );
        $( '#primary-menu li.current' ).addClass( 'selectedLava' );
        
        
        if ( $().tipsy ) { nTip=function(){ $('.ntip').tipsy({gravity: 's', fade:true}); }; nTip(); }
		if ( $().tipsy ) { sTip=function(){ $('.stip').tipsy({gravity: 'n', fade:true}); }; sTip(); }
		if ( $().tipsy ) { eTip=function(){ $('.etip').tipsy({gravity: 'w', fade:true}); }; eTip(); }
		if ( $().tipsy ) { wTip=function(){ $('.wtip').tipsy({gravity: 'e', fade:true}); }; wTip(); }


        //Scroll to top
		$(window).scroll(function() {
			if($(this).scrollTop() > 450) {
                $('#gotoTop').fadeIn();
			} else {
				$('#gotoTop').fadeOut();
			}
		});


        $('.responsive-menu').click(function() {
            $( '#primary-menu > ul, #primary-menu > div > ul' ).slideToggle("normal");
            return false;
        });


        responsiveMenuToggle=function(){
        if( $(window).width() < 979 ) {
            $( '#primary-menu > ul, #primary-menu > div > ul' ).css( 'display', 'none' );
        } else { $( '#primary-menu > ul, #primary-menu > div > ul' ).css( 'display', 'block' ); }
        };
        responsiveMenuToggle();
        
        
		$('#gotoTop').click(function() {
			$('body,html').animate({scrollTop:0},400);
            return false;
		});
        
        
        siblingsFader=function(){
		$(".siblings_fade,.flickr_badge_image").hover(function() {
			$(this).siblings().stop().fadeTo(400,0.5);
		}, function() {
			$(this).siblings().stop().fadeTo(400,1);
		});
		};
		siblingsFader();
        
        
        image_preload('.portfolio-image img');
        
        
		imgFade=function(){
		$('.image_fade').hover(function(){
			$(this).filter(':not(:animated)').animate({opacity: 0.6}, 400);
		}, function () {
			$(this).animate({opacity: 1}, 400);
		});
		};
		imgFade();
        
        
        $(".togglec").hide();
    	
    	$(".togglet").click(function(){
    	
    	   $(this).toggleClass("toggleta").next(".togglec").slideToggle("normal");
    	   return true;
        
    	});
        
        
        $("html").niceScroll({
            cursorcolor: '#000',
            cursorwidth: '7px',
            cursoropacitymax: 0.6,
            cursorborder: '0',
            cursorborderradius: '3px'
        });


        var headerOneHeight = $('#top-bar-wrapper[data-sticky="true"]').outerHeight() - 50;

        if( $('#primary-menu-wrap[data-sticky="true"]').find('#primary-menu').hasClass('primary-menu2') ) {

            var headerTwoHeight = $('#primary-menu-wrap[data-sticky="true"]').offset().top;

        } else { var headerTwoHeight = 0; }

        if( $('#header-wrap[data-sticky="true"]').hasClass('header-sticky') ) {

            var headerThreeHeight = $('#header-wrap[data-sticky="true"]').offset().top;

        } else { var headerThreeHeight = 0; }

        $('#header-wrap[data-sticky="true"]').data('size','big');

        var headerThreeLogoHeight = $('#header-wrap[data-sticky="true"]').find('#logo').find('img').outerHeight();


        stickyMenuFunction=function(){

            if( $(window).width() < 979 ) {
                $('[data-sticky="true"]').attr('data-sticky', 'false');
            } else {
                $('[data-sticky="false"]').attr('data-sticky', 'true');
            }
            
            $('#top-bar-wrapper[data-sticky="true"]').css({ 'z-index': '299' });
            
            $(window).scroll(function () {
                
                if( $(window).width() > 979 ) {

                    if ($(window).scrollTop() > headerOneHeight) {
                        $('#top-bar-wrapper[data-sticky="true"]').addClass('sticky-menu');
                    } else {
                        $('#top-bar-wrapper[data-sticky="true"]').removeClass('sticky-menu');
                    }

                    if ($(window).scrollTop() > headerTwoHeight) {
                        $('#primary-menu-wrap[data-sticky="true"]').find('#primary-menu.primary-menu2').addClass('sticky-menu');
                    } else {
                        $('#primary-menu-wrap[data-sticky="true"]').find('#primary-menu.primary-menu2').removeClass('sticky-menu');
                    }

                    if ($(window).scrollTop() > headerThreeHeight) {

                        $('#header-wrap[data-sticky="true"]').addClass('sticky-menu');

                        if( $('#header-wrap[data-sticky="true"]').data('size') == 'big' ) {
                            
                            $('#header-wrap[data-sticky="true"]').data('size','small');

                            $('#header-wrap[data-sticky="true"]').parent('#header').stop().animate( { height:'60px' }, 400 );

                            $('#header-wrap[data-sticky="true"]').find('#logo').find('img').stop().animate( { height: headerThreeLogoHeight / 2 }, 400 );

                        }
                    
                    } else {

                        if( $('#header-wrap[data-sticky="true"]').data('size') == 'small' ) {

                            $('#header-wrap[data-sticky="true"]').data('size','big');

                            $('#header-wrap[data-sticky="true"]').parent('#header').stop().animate( { height:'120px' }, 400 );

                            $('#header-wrap[data-sticky="true"]').find('#logo').find('img').stop().animate( { height: headerThreeLogoHeight }, 400 );
                        
                        }

                        $('#header-wrap[data-sticky="true"]').removeClass('sticky-menu');

                    }

                }

            });
        
        };
        stickyMenuFunction();


        sliderCaptionPos=function(){

        $('#slider .flexslider').find('.slider-caption').each(function() {

            var windowWidth = $(window).width();

            if( windowWidth < 1200 && windowWidth > 979 ) {

                var topPos = $(this).attr('data-x-ds');
                var leftPos = $(this).attr('data-y-ds');

                if( !topPos ) { topPos = $(this).attr('data-x'); }
                if( !leftPos ) { leftPos = $(this).attr('data-y'); }

                $(this).css({ 'top': topPos, 'left': leftPos });

            } else if( windowWidth < 980 && windowWidth > 767 ) {

                var topPos = $(this).attr('data-x-tab');
                var leftPos = $(this).attr('data-y-tab');

                if( !topPos ) { topPos = $(this).attr('data-x'); }
                if( !leftPos ) { leftPos = $(this).attr('data-y'); }

                $(this).css({ 'top': topPos, 'left': leftPos });

            } else {

                var topPos = $(this).attr('data-x');
                var leftPos = $(this).attr('data-y');

                $(this).css({ 'top': topPos, 'left': leftPos });

            }

        });

        };
        sliderCaptionPos();


        $(window).resize(function() {
            stickyMenuFunction();
            responsiveMenuToggle();
            sliderCaptionPos();
        });


        // Accordions
    
        loadAccordions=function(){

        $('.acc_content').hide(); //Hide/close all containers
        $('.acctitle:first').addClass('acctitlec').next().show(); //Add "active" class to first trigger, then show/open the immediate next container

        //On Click
        $('.acctitle').click(function(){
            if( $(this).next().is(':hidden') ) { //If immediate next container is closed...
                $('.acctitle').removeClass('acctitlec').next().slideUp(250); //Remove all "active" state and slide up the immediate next container
                $(this).toggleClass('acctitlec').next().slideDown(250); //Add "active" state to clicked trigger and slide down the immediate next container
            }
            return false; //Prevent the browser jump to the link anchor
        });

        };
        loadAccordions();
        
        
        imgHoverlay=function(){
		$('.portfolio-item,#portfolio-related-items li').hover(function(){
			$(this).find('.portfolio-overlay,.portfolio-overlay-icons').filter(':not(:animated)').animate({opacity: 'show'}, 400);
            $(this).find('.portfolio-overlay-icons a.left-icon').filter(':not(:animated)').animate({opacity: 'show',marginLeft: '-45px'}, 300);
            $(this).find('.portfolio-overlay-icons a.right-icon').filter(':not(:animated)').animate({opacity: 'show',marginRight: '-45px'}, 300);
		}, function () {
			$(this).find('.portfolio-overlay,.portfolio-overlay-icons').animate({opacity: 'hide'}, 400);
            $(this).find('.portfolio-overlay-icons a.left-icon').animate({opacity: 'hide',marginLeft: '-75px'}, 300);
            $(this).find('.portfolio-overlay-icons a.right-icon').animate({opacity: 'hide',marginRight: '-75px'}, 300);
		});
		};
		imgHoverlay();


        $('.product-image [data-order="1"]').css( { 'position':'absolute', 'z-index':'1' } );


        $('.product-image').hover(function(){

            $(this).find('[data-order="1"]').filter(':not(:animated)').animate({opacity: 'hide'}, 400);
            
        }, function () {
            
            $(this).find('[data-order="1"]').animate({opacity: 'show'}, 400);

        });
        
        
        reLoadFitvids=function(){
            if ( $().fitVids ) { $("#content").fitVids( { customSelector: "iframe[src^='http://www.dailymotion.com/embed']"} ); }
        };
        reLoadFitvids();
        

        $('.shop-product-slider a').zoom();
        
        
        // if( $().uniform ) { $("#primary-menu select").uniform({selectClass: 'rs-menu'}); }
        
        
        $("a[data-scrollto]").click(function(){
    	
            var divScrollToAnchor = $(this).attr('data-scrollto');
            
            if( $().scrollTo ) { jQuery.scrollTo( $( divScrollToAnchor ) , 400, {offset:-20} ); }
            
            return false;
        
    	});


        $('.skills li span').hide();
        $('.progress .progress-percent').hide();


        $('.progress').each(function(){
            
            $(this).appear(function () {

                var skillsBar = $(this).parent('li'),
                skillValue = skillsBar.attr('data-percent');
                if (!skillsBar.hasClass('skills-animated')) {
                    skillsBar.addClass('skills-animated');
                    skillsBar.find('.progress-percent').fadeIn(400);
                    skillsBar.find('.progress').animate({
                        width: skillValue + "%"
                    }, 800, function() {
                        skillsBar.find('span').fadeIn(400);
                    });
                }

            },{accX: 0, accY: -80},'easeInCubic');
        
        });


        $('.rounded-skill').each(function() {

            var roundSkillSize = $(this).attr('data-size');
            var roundSkillAnimate = $(this).attr('data-animate');
            var roundSkillWidth = $(this).attr('data-width');
            var roundSkillColor = $(this).attr('data-color');

            if( !roundSkillSize ) { roundSkillSize = 140; }
            if( !roundSkillAnimate ) { roundSkillAnimate = 2000; }
            if( !roundSkillWidth ) { roundSkillWidth = 8; }
            if( !roundSkillColor ) { roundSkillColor = '#57B3DF'; }

            $(this).css({'width':roundSkillSize+'px','height':roundSkillSize+'px'}).animate({opacity: '0'}, 10);

            $(this).appear(function () {

                if (!$(this).hasClass('skills-animated')) {

                    $(this).animate({opacity: '1'}, 500);

                    $(this).easyPieChart({
                        size: Number(roundSkillSize),
                        animate: Number(roundSkillAnimate),
                        scaleColor: false,
                        lineWidth: Number(roundSkillWidth),
                        lineCap: 'round',
                        barColor: roundSkillColor
                    });

                    $(this).addClass('skills-animated');

                }

            },{accX: 0, accY: -155},'easeInCubic');

        });


        $('.counter').each( function() {

            $(this).appear(function () {

                var intRegex = /^\d+$/;
                var floatRegex = /^((\d+(\.\d *)?)|((\d*\.)?\d+))$/;
                var counterNumber = $(this).attr('data-number');

                if (!$(this).hasClass('skills-animated')) {

                    if(intRegex.test(counterNumber) || floatRegex.test(counterNumber)){
                        
                        $(this).empty();

                        var number = counterNumber;
                        
                        if( number ) {

                            var numArray = number.split("");

                            for(var i=0; i<numArray.length; i++) { 
                                numArray[i] = parseInt(numArray[i], 10);
                                $(this).append('<span class="digit-con"><span class="digit'+i+'">0<br>1<br>2<br>3<br>4<br>5<br>6<br>7<br>8<br>9<br></span></span>');
                            }

                            var increment = $(this).find('.digit-con').outerHeight();
                            $(this).css({'height':increment+'px'});
                            var speed = 1400;

                            for(var i=0; i<numArray.length; i++) {
                                $(this).find('.digit'+i).animate({top: -(increment * numArray[i])}, speed, 'easeOutCirc');
                            }

                            $(this).addClass('skills-animated');

                        }

                    }

                }

            },{accX: 0, accY: -80},'easeInCubic');

        });


        $('[data-animate]').each(function(){

            var $toAnimateElement = $(this);

            var toAnimateDelay = $(this).attr('data-delay');

            var toAnimateDelayTime = 0;

            if( toAnimateDelay ) { toAnimateDelayTime = Number( toAnimateDelay ) + 500; } else { toAnimateDelayTime = 500; }

            if( !$toAnimateElement.hasClass('animated') ) {

                $toAnimateElement.addClass('not-animated');

                var elementAnimation = $toAnimateElement.attr('data-animate');
                
                $toAnimateElement.appear(function () {

                    setTimeout(function() {
                        $toAnimateElement.removeClass('not-animated').addClass( elementAnimation + ' animated');
                    }, toAnimateDelayTime);

                },{accX: 0, accY: -80},'easeInCubic');

            }
        
        });


        loadMagnific=function(){

        $('[data-lightbox="image"]').magnificPopup({
            type: 'image',
            closeOnContentClick: true,
            closeBtnInside: false,
            fixedContentPos: true,
            mainClass: 'mfp-no-margins mfp-with-zoom', // class to remove default margin from left and right side
            image: {
                verticalFit: true
            },
            zoom: {
                enabled: true, // By default it's false, so don't forget to enable it

                duration: 300, // duration of the effect, in milliseconds
                easing: 'ease-in-out', // CSS transition easing function
                opener: function(openerElement) {
                  return openerElement.is('img') ? openerElement : openerElement.parent().parent().find('img');
                }
              }
        });


        $('[data-lightbox="gallery"]').each(function() {

            if( $(this).find('a[data-lightbox="gallery-item"]').parent('.clone').hasClass('clone') ) {
                $(this).find('a[data-lightbox="gallery-item"]').parent('.clone').find('a[data-lightbox="gallery-item"]').attr('data-lightbox','');
            }

            $(this).magnificPopup({
                delegate: 'a[data-lightbox="gallery-item"]',
                type: 'image',
                closeOnContentClick: true,
                closeBtnInside: false,
                fixedContentPos: true,
                mainClass: 'mfp-no-margins mfp-with-zoom', // class to remove default margin from left and right side
                image: {
                    verticalFit: true
                },
                gallery: {
                    enabled: true,
                    navigateByImgClick: true,
                    preload: [0,1] // Will preload 0 - before current, and 1 after the current image
                },
                zoom: {
                    enabled: true, // By default it's false, so don't forget to enable it
                    duration: 300, // duration of the effect, in milliseconds
                    easing: 'ease-in-out', // CSS transition easing function
                    opener: function(openerElement) {
                      return openerElement.is('img') ? openerElement : openerElement.parent().parent().find('img');
                    }
                  }
            });

        });


        $('[data-lightbox="iframe"]').magnificPopup({
            disableOn: 700,
            type: 'iframe',
            mainClass: 'mfp-fade',
            removalDelay: 160,
            preloader: false,
            fixedContentPos: false
        });

        };
        loadMagnific();


        $("#product-quantity-plus").click(function(){
        
            var productQuantity = $('#product-quantity').val();

            var intRegex = /^\d+$/;

            if( intRegex.test( productQuantity ) ) {

                var productQuantityPlus = Number(productQuantity) + 1;

                $('#product-quantity').val( productQuantityPlus );
            
            } else {
                $('#product-quantity').val( 1 );
            }
            
            return false;
        
        });

        
        $("#product-quantity-minus").click(function(){
        
            var productQuantity = $('#product-quantity').val();

            var intRegex = /^\d+$/;

            if( intRegex.test( productQuantity ) ) {

                if( Number(productQuantity) > 1 ) {

                    var productQuantityMinus = Number(productQuantity) - 1;

                    $('#product-quantity').val( productQuantityMinus );

                }

            } else {
                $('#product-quantity').val( 1 );
            }
            
            return false;
        
        });
        

        if( $().carouFredSel ) {
            
            $('.testimonials').each(function() {
                
                var testimonialLeftKey = $(this).parent('.testimonial-scroller').attr('data-prev');
                var testimonialRightKey = $(this).parent('.testimonial-scroller').attr('data-next');
                var testimonialSpeed = $(this).parent('.testimonial-scroller').attr('data-speed');
                
                if( !testimonialSpeed ) { testimonialSpeed = 300; }
                
                $(this).carouFredSel({
                	circular : true,
                    responsive : true,
                    auto : false,
                    items : 1,
                	scroll : {
                		items : "page",
                        fx : "fade",
                        duration : Number(testimonialSpeed),
                        wipe : true
                	},
                	prev : {
                		button : testimonialLeftKey,
                		key : "left"
                	},
                	next : {
                		button : testimonialRightKey,
                		key : "right"
                	}
                });
            
            });
        
        }
        
        
        if( $().jflickrfeed ) {
            
            $('.flickrfeed').each(function() {
                
                var flickrFeedID = $(this).attr('data-id');
                var flickrFeedCount = $(this).attr('data-count');
                var flickrFeedType = $(this).attr('data-type');
                var flickrFeedTypeGet = 'photos_public.gne';
                
                if( flickrFeedType == 'group' ) { flickrFeedTypeGet = 'groups_pool.gne'; }
                
                if( !flickrFeedCount ) { flickrFeedCount = 9; }
            
                $(this).jflickrfeed({
                    feedapi: flickrFeedTypeGet,
            		limit: Number(flickrFeedCount),
            		qstrings: {
            			id: flickrFeedID
            		},
            		itemTemplate: '<div class="flickr_badge_image">'+
            						'<a rel="prettyPhoto[galflickr]" href="{{image}}" title="{{title}}">' +
            							'<img src="{{image_s}}" alt="{{title}}" />' +
            						'</a>' +
            					  '</div>'
            	}, function(data) {
            		if ( $().prettyPhoto ) { initprettyPhoto(); }
            	});
            
            });
            
        }
        
        
        if( $().spectragram ) {
        
            $.fn.spectragram.accessData = {
                accessToken: '36286274.b9e559e.4824cbc1d0c94c23827dc4a2267a9f6b', // your Instagram Access Token
                clientID: 'b9e559ec7c284375bf41e9a9fb72ae01' // Your Client ID
            };
            
            $('.instagram').each(function() {
                
                var instaGramUsername = $(this).attr('data-user');
                var instaGramTag = $(this).attr('data-tag');
                var instaGramCount = $(this).attr('data-count');
                var instaGramType = $(this).attr('data-type');
                
                if( !instaGramCount ) { instaGramCount = 9; }
                
                if( instaGramType == 'tag' ) {
                
                    $(this).spectragram('getRecentTagged',{
                        query: instaGramTag,
                        max: Number( instaGramCount ),
                        size: 'small',
                        wrapEachWith: '<div class="flickr_badge_image"></div>'
                    });
                
                } else if( instaGramType == 'user' ) {
                    
                    $(this).spectragram('getUserFeed',{
                        query: instaGramUsername,
                        max: Number( instaGramCount ),
                        size: 'small',
                        wrapEachWith: '<div class="flickr_badge_image"></div>'
                    });
                    
                } else {
                    
                    $(this).spectragram('getPopular',{
                        max: Number( instaGramCount ),
                        size: 'small',
                        wrapEachWith: '<div class="flickr_badge_image"></div>'
                    });
                    
                }
            
            });
        
        }
        
        
        if( $().jribbble ) {
            
            
            $('.dribbble').each(function() {
                
                var dribbbleWrap = $(this);
                var dribbbleUsername = $(this).attr('data-user');
                var dribbbleCount = $(this).attr('data-count');
                var dribbbleList = $(this).attr('data-list');
                var dribbbleType = $(this).attr('data-type');
                
                if( !dribbbleCount ) { dribbbleCount = 9; }
            
                if( dribbbleType == 'follows' ) {
                
                    $.jribbble.getShotsThatPlayerFollows( dribbbleUsername , function (followedShots) {
                        var html = [];
                    
                        $.each(followedShots.shots, function (i, shot) {
                            html.push('<div class="flickr_badge_image"><a href="' + shot.url + '" target="_blank">');
                            html.push('<img src="' + shot.image_teaser_url + '" ');
                            html.push('alt="' + shot.title + '"></a></div>');
                        });
                    
                        $(dribbbleWrap).html(html.join(''));
                    }, {page: 1, per_page: Number(dribbbleCount)});
                
                } else if( dribbbleType == 'user' ) {
                
                    $.jribbble.getShotsByPlayerId( dribbbleUsername , function (playerShots) {
                        var html = [];
                    
                        $.each(playerShots.shots, function (i, shot) {
                            html.push('<div class="flickr_badge_image"><a href="' + shot.url + '" target="_blank">');
                            html.push('<img src="' + shot.image_teaser_url + '" ');
                            html.push('alt="' + shot.title + '"></a></div>');
                        });
                    
                        $(dribbbleWrap).html(html.join(''));
                    }, {page: 1, per_page: Number(dribbbleCount)});
                
                } else if( dribbbleType == 'list' ) {
                
                    $.jribbble.getShotsByList( dribbbleList , function (listDetails) {
                        var html = [];
                    
                        $.each(listDetails.shots, function (i, shot) {
                            html.push('<div class="flickr_badge_image"><a href="' + shot.url + '" target="_blank">');
                            html.push('<img src="' + shot.image_teaser_url + '" ');
                            html.push('alt="' + shot.title + '"></a></div>');
                        });
                    
                        $(dribbbleWrap).html(html.join(''));
                    }, {page: 1, per_page: Number(dribbbleCount)});
                
                }
            
            });
            
            
        }


        $('.fslider').addClass('preloader2');


        // Portfolio Details
        
        var portfolioItems = $('.portfolio-ajax .portfolio-item');
        var portfolioDetails = $('#portfolio-ajax-wrap');
        var prevPostPortId = '';
        
        
        $('.portfolio-ajax .portfolio-item').click( function(e) {
        
            var portPostId = $(this).attr('id').split('portfolio-')[1];
        
            if( !$(this).hasClass('portfolio-active') ) {
                $(".intro-slider").hide();
                portfolioDetails.fadeIn();
                loadPortfolio(portPostId,prevPostPortId);
                if($().scrollTo) { jQuery.scrollTo(portfolioDetails, 650, {easing:'easeOutBack',offset:-56}); }
            }

            e.preventDefault();
        
        });
        
        
        function newNextPrev(portPostId) {
        
            var portNext = getNextPortfolio(portPostId);
            var portPrev = getPrevPortfolio(portPostId);
            $('#next-portfolio').attr('data-id', portNext);
            $('#prev-portfolio').attr('data-id', portPrev);

        }
        
        
        function loadPortfolio(portPostId, prevPostPortId, getIt) {
        
            if(!getIt) { getIt = false; }
            var portNext = getNextPortfolio(portPostId);
            var portPrev = getPrevPortfolio(portPostId);
            if(getIt == false) {
                $(".intro-slider").hide();
                closePortfolio(portfolioDetails);
                portfolioDetails.fadeIn().addClass('preloader2');
                var portfolioDataLoader = $("#portfolio-" + portPostId).attr('data-loader');
                portfolioDetails.find('#portfolio-ajax-show').load(portfolioDataLoader, { portid: portPostId, portnext: portNext, portprev: portPrev },
                function() {
                    initializePortfolio(portPostId,portfolioDetails);
                    loadAccordions();
                    loadFlexSlider();
                    loadMagnific();
                    reLoadFitvids();
                    openPortfolio(portfolioDetails);
                    portfolioItems.removeClass('portfolio-active');
                    $('#portfolio-' + portPostId).addClass('portfolio-active');
                    portfolioDetails.removeClass('preloader2');
                });
            }
        
        }
        
        
        function closePortfolio(portfolioDetails) {
        
            if( portfolioDetails && portfolioDetails.height() > 32 ) {
                portfolioDetails.find('#portfolio-ajax-show').stop(true).animate({ opacity: 0 }, 200);
                portfolioDetails.stop(true).animate({ height: 32 }, 500, 'easeOutQuad');
                portfolioDetails.find('#portfolio-ajax-single').remove();
            }
        
        }
        
        
        function openPortfolio(portfolioDetails) {
            portfolioDetails.stop(true).animate({ opacity: 1, height: portfolioDetails.find('#portfolio-ajax-show').outerHeight() }, 650, 'easeOutQuad', function () {
                portfolioDetails.css({ height: 'auto' });
                portfolioDetails.find('#portfolio-ajax-show').stop(true).animate({ opacity: 1 }, 200, function() {
                    var t=setTimeout(function(){ $( '.flexslider .slide' ).resize(); },1000);
                });
            });
        }
        
        
        function getNextPortfolio(portPostId) {
        
            var portNext = '';
            var hasNext = $('#portfolio-' + portPostId).next();
            if(hasNext.length != 0) {
                portNext = hasNext.attr('id').split('portfolio-')[1];
            }
            return portNext;

        }
        
        
        function getPrevPortfolio(portPostId) {
        
            var portPrev = '';
            var hasPrev = $('#portfolio-' + portPostId).prev();
            if(hasPrev.length != 0) {
                portPrev = hasPrev.attr('id').split('portfolio-')[1];
            }
            return portPrev;
            
        }
        
        
        function initializePortfolio(portPostId,portfolioDetails) {
            
            prevPostPortId = $('#portfolio-' + portPostId);
            
            $('#next-portfolio, #prev-portfolio').click( function() {
                var portPostId = $(this).attr('data-id');
                portfolioItems.removeClass('portfolio-active');
                $('#portfolio-' + portPostId).addClass('portfolio-active');
                loadPortfolio(portPostId,prevPostPortId);
                return false;
            });
            
            $('#close-portfolio').click( function() {
                portfolioDetails.stop(true).animate({ opacity: 'hide', height: 0 }, 500, 'easeOutQuad', function(){
                    portfolioDetails.find('#portfolio-ajax-single').remove();
                    if($().scrollTo) { jQuery.scrollTo('#portfolio', 900, {easing:'easeOutBack',offset:-160}); }
                });
                portfolioItems.removeClass('portfolio-active');
                $(".intro-slider").show();
                return false;
            });
            
        }


        $window = $(window);

        $('[data-type="background"]').each(function(){

            var $bgobj = $(this); // assigning the object
            
            $(window).scroll(function() {
            
                // Scroll the background at var speed
                // the yPos is a negative value because we're scrolling it UP!
                var yPos = -($bgobj.scrollTop() / $bgobj.data('speed')); 

                // Put together our final background position
                var coords = '0 '+ yPos + 'px';

                // Move the background
                $bgobj.css({ backgroundPosition: coords });

                if( $window.width() < 980 ) { $bgobj.css({ 'background-attachment': 'scroll' }); } else { $bgobj.css({ 'background-attachment': 'fixed' }); }

            }); // window scroll Ends

        });
        

});


$(window).load(function() {
    
    
    $( '#primary-menu > ul, #primary-menu > div > ul' ).lavaLamp( {fx: "easeOutBack", speed: 700, returnDelay: 600} );
    
    
    siblingsFader();


    $('.our-clients li a').BlackAndWhite({
        hoverEffect : true, // default true
        // set the path to BnWWorker.js for a superfast implementation
        webworkerPath : false,
        // for the images with a fluid width and height 
        responsive:true,
        // to invert the hover effect
        invertHoverEffect: false,
        speed: { //this property could also be just speed: value for both fadeIn and fadeOut
            fadeIn: 400, // 200ms for fadeIn animations
            fadeOut: 700 // 800ms for fadeOut animations
        },
        onImageReady:function(img) {
            // this callback gets executed anytime an image is converted
        }
    });
    
    
    if ( $().flexslider ) {
        
        loadFlexSlider=function(){

        $('.fslider .flexslider').each(function() {

            var $flexsSlider = $(this);
            
            var flexsAnimation = $flexsSlider.parent('.fslider').attr('data-animation');
            var flexsEasing = $flexsSlider.parent('.fslider').attr('data-easing');
            var flexsDirection = $flexsSlider.parent('.fslider').attr('data-direction');
            var flexsSlideshow = $flexsSlider.parent('.fslider').attr('data-slideshow');
            var flexsPause = $flexsSlider.parent('.fslider').attr('data-pause');
            var flexsSpeed = $flexsSlider.parent('.fslider').attr('data-speed');
            var flexsVideo = $flexsSlider.parent('.fslider').attr('data-video');
            var flexsPagi = $flexsSlider.parent('.fslider').attr('data-pagi');
            var flexsArrows = $flexsSlider.parent('.fslider').attr('data-arrows');
            var flexsThumbs = $flexsSlider.parent('.fslider').attr('data-thumbs');
            var flexsSheight = true;
            var flexsUseCSS = false;
            
            if( !flexsAnimation ) { flexsAnimation = 'slide'; }
            if( !flexsEasing || flexsEasing == 'swing' ) {
                flexsEasing = 'swing';
                flexsUseCSS = true;
            }
            if( !flexsDirection ) { flexsDirection = 'horizontal'; }
            if( !flexsSlideshow ) { flexsSlideshow = true; } else { flexsSlideshow = false; }
            if( !flexsPause ) { flexsPause = 5000; }
            if( !flexsSpeed ) { flexsSpeed = 600; }
            if( !flexsVideo ) { flexsVideo = false; }
            if( flexsDirection == 'vertical' ) { flexsSheight = false; }
            if( flexsPagi == 'false' ) { flexsPagi = false; } else { flexsPagi = true; }
            if( flexsThumbs == 'true' ) { flexsPagi = 'thumbnails'; } else { flexsPagi = flexsPagi; }
            if( flexsArrows == 'false' ) { flexsArrows = false; } else { flexsArrows = true; }
            
            $flexsSlider.flexslider({
                
                selector: ".slider-wrap > .slide",
                animation: flexsAnimation,
                easing: flexsEasing,
                direction: flexsDirection,
                slideshow: flexsSlideshow,
                slideshowSpeed: Number(flexsPause),
                animationSpeed: Number(flexsSpeed),
                pauseOnHover: true,
                video: flexsVideo,
                controlNav: flexsPagi,
                directionNav: flexsArrows,
                smoothHeight: flexsSheight,
                useCSS: flexsUseCSS,
                start: function(slider){
                    slider.parent().removeClass('preloader2');
                    var t = setTimeout( function(){ $('#posts').isotope('reLayout'); }, 750 );
                    loadMagnific();
                }
                
            });
        
        });

        };
        loadFlexSlider();

        
        $('.shop-product-slider').find( '.flex-control-thumbs li' ).hover(function(){
            var getShopThumbIndex = $('.shop-product-slider').find( '.flex-control-thumbs li' ).index(this);
            $('.shop-product-slider').find('.flexslider').flexslider( getShopThumbIndex );
        });


        $('#slider .flexslider').flexslider({
            
            selector: ".slider-wrap > .slide",
            animation: "fade",
            easing: "swing",
            direction: "horizontal",
            slideshow: true,
            slideshowSpeed: 6000,
            animationSpeed: 1500,
            pauseOnHover: true,
            video: true,
            controlNav: true,
            directionNav: true,
            start: function(slider){
                slider.parent('#slider').removeClass('preloader2');
                slider.find('.animated').each(function(){

                    var toAnimateElement = $(this);

                    var elementAnimation = toAnimateElement.attr('data-animater');

                    toAnimateElement.addClass('not-animated').removeClass('animated ' + elementAnimation);
                
                });
                slider.find('[data-animater]').each(function(){

                    var $toSliderAnimateCaption = $(this);

                    var toAnimateDelay = $(this).attr('data-delay');

                    var toAnimateDelayTime = 0;

                    if( toAnimateDelay ) { toAnimateDelayTime = Number( toAnimateDelay ) + 300; } else { toAnimateDelayTime = 300; }

                    if( !$toSliderAnimateCaption.hasClass('animated') ) {

                        $toSliderAnimateCaption.addClass('not-animated');

                        var elementAnimation = $toSliderAnimateCaption.attr('data-animater');
                        
                        setTimeout(function() {
                                $toSliderAnimateCaption.removeClass('not-animated').addClass( elementAnimation + ' animated');
                        }, toAnimateDelayTime);

                    }
                
                });
            },
            before: function(slider){
                slider.find('.animated').each(function(){

                    var toAnimateElement = $(this);

                    var elementAnimation = toAnimateElement.attr('data-animater');

                    toAnimateElement.addClass('not-animated').removeClass('animated ' + elementAnimation);
                
                });
            },
            after: function(slider){

                var currentSlide = slider.slides.eq(slider.currentSlide);
                currentSlide.find('[data-animater]').each(function(){

                    var $toSliderAnimateCaption = $(this);

                    var toAnimateDelay = $(this).attr('data-delay');

                    var toAnimateDelayTime = 0;

                    if( toAnimateDelay ) { toAnimateDelayTime = Number( toAnimateDelay ) + 300; } else { toAnimateDelayTime = 300; }

                    if( !$toSliderAnimateCaption.hasClass('animated') ) {

                        $toSliderAnimateCaption.addClass('not-animated');

                        var elementAnimation = $toSliderAnimateCaption.attr('data-animater');
                        
                        setTimeout(function() {
                                $toSliderAnimateCaption.removeClass('not-animated').addClass( elementAnimation + ' animated');
                        }, toAnimateDelayTime);

                    }
                
                });
            }
            
        });
    
    }

});