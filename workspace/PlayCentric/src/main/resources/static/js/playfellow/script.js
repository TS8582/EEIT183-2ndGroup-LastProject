(function($) {

  "use strict";

  var initPreloader = function() {
    $(document).ready(function($) {
    var Body = $('body');
        Body.addClass('preloader-site');
    });
    $(window).load(function() {
        $('.preloader-wrapper').fadeOut();
        $('body').removeClass('preloader-site');
    });
  }

  // init Chocolat light box
	var initChocolat = function() {
		Chocolat(document.querySelectorAll('.image-link'), {
		  imageSize: 'contain',
		  loop: true,
		})
	}

  var initSwiper = function() {

    var swiper = new Swiper(".main-swiper", {
      speed: 500,
      pagination: {
        el: ".swiper-pagination",
        clickable: true,
      },
    });

    var bestselling_swiper = new Swiper(".bestselling-swiper", {
      slidesPerView: 4,
      spaceBetween: 30,
      speed: 500,
      breakpoints: {
        0: {
          slidesPerView: 1,
        },
        768: {
          slidesPerView: 3,
        },
        991: {
          slidesPerView: 4,
        },
      }
    });

    var testimonial_swiper = new Swiper(".testimonial-swiper", {
      slidesPerView: 1,
      speed: 500,
      pagination: {
        el: ".swiper-pagination",
        clickable: true,
      },
    });

    var products_swiper = new Swiper(".products-carousel", {
      slidesPerView: 4,
      spaceBetween: 30,
      speed: 500,
      breakpoints: {
        0: {
          slidesPerView: 1,
        },
        768: {
          slidesPerView: 3,
        },
        991: {
          slidesPerView: 4,
        },

      }
    });

  }

  var initProductQty = function(){

    $('.product-qty').each(function(){

      var $el_product = $(this);
      var quantity = 0;

      $el_product.find('.quantity-right-plus').click(function(e){
          e.preventDefault();
          var quantity = parseInt($el_product.find('#quantity').val());
          $el_product.find('#quantity').val(quantity + 1);
      });

      $el_product.find('.quantity-left-minus').click(function(e){
          e.preventDefault();
          var quantity = parseInt($el_product.find('#quantity').val());
          if(quantity>0){
            $el_product.find('#quantity').val(quantity - 1);
          }
      });

    });

  }

  // init jarallax parallax
  var initJarallax = function() {
    jarallax(document.querySelectorAll(".jarallax"));

    jarallax(document.querySelectorAll(".jarallax-keep-img"), {
      keepImg: true,
    });
  }

  // document ready
  $(document).ready(function() {
    
    initPreloader();
    initSwiper();
    initProductQty();
    initJarallax();
    initChocolat();

        // product single page
        var thumb_slider = new Swiper(".product-thumbnail-slider", {
          spaceBetween: 8,
          slidesPerView: 3,
          freeMode: true,
          watchSlidesProgress: true,
        });
    
        var large_slider = new Swiper(".product-large-slider", {
          spaceBetween: 10,
          slidesPerView: 1,
          effect: 'fade',
          thumbs: {
            swiper: thumb_slider,
          },
        });

    window.addEventListener("load", (event) => {
      //isotope
      $('.isotope-container').isotope({
        // options
        itemSelector: '.item',
        layoutMode: 'masonry'
      });


      var $grid = $('.entry-container').isotope({
        itemSelector: '.entry-item',
        layoutMode: 'masonry'
      });


      // Initialize Isotope
      var $container = $('.isotope-container').isotope({
        // options
        itemSelector: '.item',
        layoutMode: 'masonry'
      });

	  $(document).ready(function () {
	     // 初始化 Isotope
	     var $container = $('.isotope-container').isotope({
	       itemSelector: '.item',
	       layoutMode: 'fitRows'
	     });

	     // 過濾按鈕的點擊事件
	     $('.filter-button').click(function () {
	       // 切換 active 樣式
	       $('.filter-button').removeClass('active');
	       $(this).addClass('active');

	       // 過濾項目
	       var filterValue = $(this).attr('data-filter');
	       if (filterValue === '*') {
	         // 顯示所有項目
	         $container.isotope({ filter: '*' });
	       } else {
	         // 顯示過濾後的項目
	         $container.isotope({ filter: filterValue });
	       }
	     });
	   });

    });

  }); // End of a document
  
  $(document).ready(function () {
  			// 初始化 Swiper
  			var swiper = new Swiper('.main-swiper', {
  				loop: true,
  				autoplay: {
  					delay: 2000,
  				},
  				pagination: {
  					el: '.swiper-pagination',
  					clickable: true,
  				},
  				navigation: {
  					nextEl: '.swiper-button-next',
  					prevEl: '.swiper-button-prev',
  				},
  			});

  			// 初始化 Isotope for PlayFellow
  			var $playfellowContainer = $('#playfellow-container').isotope({
  				itemSelector: '.item',
  				layoutMode: 'fitRows'
  			});

  			// 過濾按鈕的點擊事件 for PlayFellow
  			$('.filter-button').click(function () {
  				// 切換 active 樣式
  				$('.filter-button').removeClass('active');
  				$(this).addClass('active');

  				// 過濾項目
  				var filterValue = $(this).attr('data-filter');
  				if (filterValue === '*') {
  					// 顯示所有項目
  					$playfellowContainer.isotope({
  						filter: '*'
  					});
  				} else {
  					// 顯示過濾後的項目
  					$playfellowContainer.isotope({
  						filter: filterValue
  					});
  				}
  			});
  		});

})(jQuery);