/*
 * Bootstrap Cookie Alert by Wruczek
 * https://github.com/Wruczek/Bootstrap-Cookie-Alert
 * Released under MIT license
 */
(function () {

    var hide = function () {
        document.getElementsByClassName("cookies")[0].style.visibility ='hidden';
    }

    var acceptCookies = document.querySelector(".cookie-btn");

    if (getCookie("acceptCookies")) {
        hide();
    }
    acceptCookies.addEventListener("click", function () {
        setCookie("acceptCookies", true, 365);
        hide();
    });

    document.getElementsByClassName("close-cookie")[0].addEventListener("click",function () {
        hide();
    })

    function setCookie(cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    }

    function getCookie(cname) {
        var name = cname + "=";
        var decodedCookie = decodeURIComponent(document.cookie);
        var ca = decodedCookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) === 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }
})();