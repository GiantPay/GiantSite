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

    function formatNetworkHashrate(networkHashrate) {
        if (networkHashrate >= 1000000000000) {
            return numeral(networkHashrate / 1000000000000.0).format('0,0.00') + " TH/s";
        } else if (networkHashrate >= 1000000000) {
            return numeral(networkHashrate / 1000000000.0).format('0,0.00') + " GH/s";
        } else if (networkHashrate >= 1000000) {
            return numeral(networkHashrate / 1000000.0).format('0,0.00') + " MH/s";
        } else if (networkHashrate >= 1000) {
            return numeral(networkHashrate / 1000.0).format('0,0.00') + " kH/s";
        } else {
            return numeral(networkHashrate).format('0,0.00') + " H/s";
        }
    }

    /*=========================================================================
        Webticker Active
    =========================================================================*/
    function updateInfo(first) {
        $.getJSON("/api/info", function (info) {
            var webTicker = $('#webTicker');

            if (first) {
                var $html = "";
                $html += "<li>GIC/BTC: <span id='infoRate'>" + info.rate + " BTC</span></li>";
                $html += "<li>Volume 24h: <span id='volume'>" + info.volume.toFixed(3) + " BTC</span></li>";
                $html += "<li>Current Block: <span id='infoHeight'>" + numeral(info.height).format('0') + "</span></li>";
                $html += "<li>Block Reward: <span id='infoReward'>" + info.reward + " GIC</span></li>";
                $html += "<li>POS Difficulty: <span id='infoDifficulty'>" + numeral(info.networkDifficulty).format('0,0.00') + "</span></li>";
                $html += "<li>Coin Supply: <span id='infoSupply'>" + numeral(info.coinSupply).format('0,0.00') + " GIC</span></li>";
                $html += "<li>Masternodes online: <span id='infoMasternodes'>" + info.masternodes + "</span></li>";
                $html += "<li>ROI: <span id='infoRoi'>" + (info.masternodeRoi).toFixed(1) + "% / " + info.masternodeRoiDays + " days</span></li>";
                webTicker.html($html);

                webTicker.webTicker({
                    height: "40px",
                });
            } else {
                webTicker.find('infoRate').text(info.rate);
                webTicker.find('infoHeight').text(numeral(info.height).format('0'));
                webTicker.find('infoReward').text(info.reward + " GIC");
                webTicker.find('infoDifficulty').text(numeral(info.networkDifficulty).format('0,0.00'));
                webTicker.find('infoSupply').text(numeral(info.coinSupply).format('0,0.00') + " GIC");
                webTicker.find('infoMasternodes').text(info.masternodes);
                webTicker.find('infoRoi').text((info.masternodeRoi).toFixed(1) + "% / " + info.masternodeRoiDays + " days");
            }
        });
    }

    updateInfo(true);
    setInterval(updateInfo, 60000);

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