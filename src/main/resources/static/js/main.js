"use strict";

(function ($) {

    $(window).on('load', function () {
        $('body').addClass('loaded');
    });

    /*=========================================================================
        Sticky Ticker
    =========================================================================*/
    $(function () {
        var ticker = $(".ticker_wrapper"),
            yOffset = 0,
            triggerPoint = $('#header').height();
        $(window).on('scroll', function () {
            yOffset = $(window).scrollTop();

            if (yOffset >= triggerPoint) {
                ticker.addClass("ticker-fixed-top");
            } else {
                ticker.removeClass("ticker-fixed-top");
            }

        });
    });

    /*=========================================================================
        Main Slider
    =========================================================================*/
    $('#main-slider').nivoSlider({
        effect: 'random',
        animSpeed: 300,
        pauseTime: 5000,
        directionNav: true,
        manualAdvance: false,
        controlNavThumbs: false,
        pauseOnHover: true,
        controlNav: true,
        prevText: "<i class='fas fa-arrow-left'></i>",
        nextText: "<i class='fas fa-arrow-right'></i>"
    });

        /*=========================================================================
                Mobile Menu
        =========================================================================*/
    $(function () {
        $('#mainmenu').slicknav({
            prependTo: '.bottom_content_wrap',
            label: '',
            allowParentLinks: true
        });
    });

    /*=========================================================================
        Webticker Active
    =========================================================================*/
    $.getJSON("/api/info", function (info) {
        var webTicker = $('#webTicker');
        webTicker.append("<li>GIC/BTC (presale): <span>" + info.rate + "</span></li>");
        webTicker.append("<li>Current Block: <span>" + info.height + "</span></li>");
        webTicker.append("<li>Block Reward: <span>" + info.reward + " GIC</span></li>");
        webTicker.append("<li>Network: <span>" + info.networkHashrate + " H/s</span></li>");
        webTicker.append("<li>Difficulty: <span>" + info.networkDifficulty + "</span></li>");
        webTicker.append("<li>Coin Supply: <span>" + info.coinSupply.toFixed(2) + " GIC</span></li>");
        webTicker.append("<li>Masternodes online: <span>" + info.masternodes + "</span></li>");
        webTicker.append("<li>ROI: <span>" + (info.masternodeRoi).toFixed(1) + "% / " + info.masternodeRoiDays + " days</span></li>");

        webTicker.webTicker({
            height: "40px",
        });
    });

    /*=========================================================================
        Counter Up Active
    =========================================================================*/
    var counterSelector = $('.counter');
    counterSelector.counterUp({
        delay: 10,
        time: 1000
    });


    /*=========================================================================
        Team Carousel Active
    =========================================================================*/
    $('#team_carousel').owlCarousel({
        loop: true,
        margin: 10,
        autoplay: true,
        smartSpeed: 500,
        nav: true,
        navText: ['<i class="fas fa-arrow-left"></i>', '<i class="fas fa-arrow-right"></i>'],
        dots: false,
        responsive: {
            0: {
                items: 1
            },
            480: {
                items: 2,
            },
            768: {
                items: 3,
            },
            900: {
                items: 4,
            }
        }
    });

    /*=========================================================================
        Project Carousel Active
    =========================================================================*/
    $('#project_carousel').owlCarousel({
        loop: true,
        margin: 10,
        autoplay: true,
        smartSpeed: 500,
        nav: true,
        navText: ['<i class="fas fa-arrow-left"></i>', '<i class="fas fa-arrow-right"></i>'],
        dots: false,
        responsive: {
            0: {
                items: 1
            },
            480: {
                items: 2,
            },
            768: {
                items: 3,
            },
            900: {
                items: 3,
            }
        }
    });
    /*=========================================================================
        Isotope Active
    =========================================================================*/
    $('.project_items').imagesLoaded(function () {

        // Add isotope click function
        $('.project_filter li').on('click', function () {
            $(".project_filter li").removeClass("active");
            $(this).addClass("active");

            var selector = $(this).attr('data-filter');
            $(".project_items").isotope({
                filter: selector,
                animationOptions: {
                    duration: 750,
                    easing: 'linear',
                    queue: false,
                }
            });
            return false;
        });

        $(".project_items").isotope({
            itemSelector: '.single_item',
            layoutMode: 'masonry',
        });
    });


    /*=========================================================================
            Initialize smoothscroll plugin
    =========================================================================*/
    // smoothScroll.init({
    //     offset: 60
    // });


    /*=========================================================================
        Testimonial Carousel
    =========================================================================*/
    $('#testimonial_carousel').owlCarousel({
        loop: true,
        autoplay: true,
        smartSpeed: 500,
        items: 1,
        nav: false
    });

    /*=========================================================================
        Sponsor Carousel
    =========================================================================*/
    $('#sponsor_carousel').owlCarousel({
        loop: true,
        margin: 10,
        autoplay: true,
        smartSpeed: 500,
        nav: false,
        dots: false,
        // responsive: true,
        responsive: {
            0: {
                items: 2,
            },
            480: {
                items: 3,
            },
            768: {
                items: 6,
            }
        }
    });

    /*=========================================================================
            Active venobox
    =========================================================================*/
    $('.img_popup').venobox({
        numeratio: true,
        infinigall: true
    });

    /*=========================================================================
      Scroll To Top
    =========================================================================*/
    $(window).on('scroll', function () {
        if ($(this).scrollTop() > 100) {
            $('#scroll-to-top').fadeIn();
        } else {
            $('#scroll-to-top').fadeOut();
        }
    });

    new WOW().init();

})(jQuery);