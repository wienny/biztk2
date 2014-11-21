<?php


header ("Content-Type:text/css");


/** ===============================================================
 *
 *      Edit your Color Configurations below:
 *      You should only enter 6-Digits HEX Colors.
 *
 ================================================================== */


$color = "#665588"; // Change your Color Here


/** ===============================================================
 *
 *      Do not Edit anything below this line if you do not know
 *      what you are trying to do..!
 *
 ================================================================== */


function checkhexcolor($color) {

    return preg_match('/^#[a-f0-9]{6}$/i', $color);

}


/** ===============================================================
 *
 *      Primary Color Scheme
 *
 ================================================================== */


if( isset( $_GET[ 'color' ] ) AND $_GET[ 'color' ] != '' ):

    $color = "#" . $_GET[ 'color' ];
    
endif;

if( !$color OR !checkhexcolor( $color ) ) {
    
    $color = "#FF8300";
    
}


?>


/* ----------------------------------------------------------------
    Colors
-----------------------------------------------------------------*/


a,
h1 span,
h2 span,
h3 span,
h4 span,
h5 span,
h6 span,
.top-cart-item-desc a:hover,
.top-checkout-wrap span.top-checkout-price,
.slider-caption span,
#portfolio-filter li a:hover,
#portfolio-filter li.activeFilter a,
.portfolio-item h3 a:hover,
.product-title h3 a:hover,
.product-price,
#portfolio-navigation a:hover,
.product-single .product-price,
.entry_format,
.comments-icon,
.post-navigation a:hover,
.comment-content .comment-author a:hover,
.promo h3 > span,
.error-404,
.product-feature3:hover span,
.hi-icon-effect-3b.hi-icon,
.hi-icon-effect-4.hi-icon,
.hi-icon-effect-5.hi-icon,
.hi-icon-effect-6.hi-icon,
.hi-icon-effect-7.hi-icon,
.hi-icon-effect-9.hi-icon,
.counter,
.best-price .pricing-price,
.widget-scroll-prev:hover,
.widget-scroll-next:hover { color: <?php echo $color; ?>; }


.button:hover,
.button.inverse { color: <?php echo $color; ?> !important; }


/* ----------------------------------------------------------------
    Background Colors
-----------------------------------------------------------------*/


#primary-menu.primary-menu2 ul li.backLava,
#primary-menu.primary-menu3 ul li.backLava,
.product-sale,
.product-overlay a:hover,
span#product-quantity-plus:hover,
span#product-quantity-minus:hover,
.error-404-meta input[type="submit"],
.product-feature img,
.product-feature > span,
.hi-icon-effect-1.hi-icon,
.hi-icon-effect-2.hi-icon:after,
.hi-icon-effect-3.hi-icon:after,
.hi-icon-effect-8.hi-icon,
.sidenav > .ui-tabs-active > a,
.sidenav > .ui-tabs-active > a:hover,
.lp-subscribe input[type="submit"],
.team-image span,
.i-rounded:hover,
.i-circled:hover,
.simple-button.inverse,
.simple-button:hover,
.best-price .pricing-title,
ul.tab-nav.tab-nav2 li.ui-state-active a,
#gotoTop:hover,
a.twitter-follow-me:hover,
#footer.footer-dark a.twitter-follow-me:hover,
.sposts-list .spost-image:hover,
#footer.footer-dark .sposts-list .spost-image:hover,
.tagcloud a:hover,
#footer.footer-dark .tagcloud a:hover { background-color: <?php echo $color; ?>; }

.responsive-menu { background-color: <?php echo $color; ?> !important; }


/* ----------------------------------------------------------------
    Border Colors
-----------------------------------------------------------------*/


.top-cart-item-image a:hover,
.flex-control-thumbs li img.flex-active,
#portfolio-navigation a:hover,
.entry_format,
.comments-icon,
.comment-content .comment-author a:hover,
.hi-icon-effect-4.hi-icon:after,
.widget-scroll-prev:hover,
.widget-scroll-next:hover { border-color: <?php echo $color; ?>; }


.button:hover,
.button.inverse { border-color: <?php echo $color; ?> !important; }


#top-bar-wrapper,
#primary-menu.primary-menu2 ul li.selectedLava,
#primary-menu.primary-menu3 ul li.selectedLava,
#primary-menu ul ul,
.top-links ul ul,
.top-links ul div.top-link-section,
#footer,
#copyrights { border-top-color: <?php echo $color; ?>; }


span.page-divider span { border-bottom-color: <?php echo $color; ?>; }


#primary-menu ul ul ul { border-left-color: <?php echo $color; ?>; }


.hi-icon-effect-2.hi-icon,
.hi-icon-effect-3.hi-icon,
.hi-icon-effect-3a.hi-icon,
.hi-icon-effect-4.hi-icon,
.hi-icon-effect-5.hi-icon,
.hi-icon-effect-6.hi-icon,
.hi-icon-effect-7.hi-icon,
.hi-icon-effect-7a.hi-icon:after,
.hi-icon-effect-9.hi-icon:after { box-shadow: 0 0 0 3px <?php echo $color; ?>; }


/* ----------------------------------------------------------------
    Selection Colors
-----------------------------------------------------------------*/


::selection,
::-moz-selection,
::-webkit-selection { background-color: <?php echo $color; ?>; }