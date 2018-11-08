$(function () {

    var rates = {
        btc: 0.00002872,
        usd: 0.18
    };
    var values = {
        gic: 0,
        btc: 0,
        usd: 0
    };

    $("#chooseBTC").click(function () {
        $("[id$=\"IncomeBtc\"]").removeClass("d-none");
        $("[id$=\"IncomeUsd\"]").addClass("d-none");
    });

    $("#chooseUSD").click(function () {
        $("[id$=\"IncomeBtc\"]").addClass("d-none");
        $("[id$=\"IncomeUsd\"]").removeClass("d-none");
    });

    $("#btcAmountGIC, #usdAmountGIC").on("keypress keyup", function (e) {
        var $this = $(this);
        var oldGicValue = parseFloat($this.val());

        if(!oldGicValue){
            clear();
            return;
        }

        setTimeout(function () {
            var newGicValue = parseFloat($this.val());
            if (oldGicValue === newGicValue) {
                setGic(newGicValue);
            }
        }, 1000);
    });

    $("#btcAmountBTC").on("keypress keyup", function (e) {
        var $this = $(this);
        var oldBtcValue = parseFloat($this.val());

        if(!oldBtcValue){
            clear();
            return;
        }

        setTimeout(function () {
            var newBtcValue = parseFloat($this.val());
            if (oldBtcValue === newBtcValue) {
                setBtc(newBtcValue);
            }
        }, 500);
    });

    $("#usdAmountUSD").on("keypress keyup", function (e) {
        var $this = $(this);
        var oldUsdValue = parseFloat($this.val());

        if(!oldUsdValue){
            clear();
            return;
        }

        setTimeout(function () {
            var newUsdValue = parseFloat($this.val());
            if (oldUsdValue === newUsdValue) {
                setUsd(newUsdValue);
            }
        }, 500);
    });
    
    function clear() {
        $("#btcAmountGIC").val('');
        $("#btcAmountBTC").val('');
        $("#usdAmountUSD").val('');
    }

    function setGic(gic) {
        values.gic = gic.toFixed(0);
        values.btc = (gic * rates.btc).toFixed(8);
        values.usd = (gic * rates.usd).toFixed(2);
        $("#btcAmountGIC").val(values.gic);
        $("#usdAmountGIC").val(values.gic);
        $("#btcAmountBTC").val(values.btc);
        $("#usdAmountUSD").val(values.usd);

    }

    function setBtc(btc) {
        values.btc = btc.toFixed(8);
        values.gic = (values.btc / rates.btc).toFixed(0);
        values.usd = (values.gic * rates.usd).toFixed(2);

        $("#btcAmountGIC").val(values.gic);
        $("#usdAmountGIC").val(values.gic);
        $("#btcAmountBTC").val(values.btc);
        $("#usdAmountUSD").val(values.usd);

    }

    function setUsd(usd) {
        values.usd = usd.toFixed(2);
        values.gic = (values.usd / rates.usd).toFixed(0);
        values.btc = (values.gic * rates.btc).toFixed(8);
        $("#btcAmountGIC").val(values.gic);
        $("#usdAmountGIC").val(values.gic);
        $("#btcAmountBTC").val(values.btc);
        $("#usdAmountUSD").val(values.usd);

    }

    setGic(1000);
});