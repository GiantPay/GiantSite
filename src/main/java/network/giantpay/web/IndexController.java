package network.giantpay.web;

import network.giantpay.dto.CoinInfoDto;
import network.giantpay.dto.InfoDto;
import network.giantpay.dto.RateDto;
import network.giantpay.service.MonitoringService;
import network.giantpay.service.PageService;
import network.giantpay.utils.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private MonitoringService monitoringService;
    @Autowired
    private PageService pageService;

    @GetMapping({"/"})
    public String index(Map<String, Object> model) {
        RateDto rates = monitoringService.getRates();
        InfoDto info = monitoringService.getInfo();
        BigDecimal btcRate = rates.getBtc().setScale(8, RoundingMode.HALF_DOWN);
        BigDecimal usdRate = rates.getUsd().setScale(2, RoundingMode.HALF_DOWN);

        CoinInfoDto btcInfo = monitoringService.getCoinInfo("BTC");

        model.put("currentHeight", info.getHeight());
        model.put("currentDifficulty", info.getNetworkDifficulty().setScale(1, RoundingMode.HALF_DOWN));
        model.put("coinSupply", info.getCoinSupply().longValue());
        model.put("masternodes", info.getMasternodes());
        model.put("btcVolume", info.getVolume().setScale(4, RoundingMode.HALF_DOWN));
        if (btcInfo != null) {
            model.put("usdVolume", info.getVolume().multiply(btcInfo.getPrice()).setScale(2, RoundingMode.HALF_DOWN));
        } else {
            model.put("usdVolume", BigDecimal.ZERO);
        }
        model.put("changePrice", info.getChangePrice24h());
        model.put("btcRate", btcRate);
        model.put("usdRate", usdRate);

        BigDecimal masternodeCoinLocked = BigDecimal.valueOf(info.getMasternodes() * 1000);
        BigDecimal premineCoinLocked = BigDecimal.valueOf(150000);
        BigDecimal coinLocked = masternodeCoinLocked.add(premineCoinLocked);

        model.put("masternodeCoinLocked", FormatUtils.formatInteger(masternodeCoinLocked));
        if (info.getCoinSupply().doubleValue() > 0) {
            model.put("masternodeCoinLockedPercent", FormatUtils.formatPercent(
                    masternodeCoinLocked
                            .divide(info.getCoinSupply(), 6, RoundingMode.HALF_DOWN)
                            .multiply(BigDecimal.valueOf(100))
            ));
        } else {
            model.put("masternodeCoinLockedPercent", 0.0);
        }

        model.put("premineCoinLocked", FormatUtils.formatInteger(premineCoinLocked));
        if (info.getCoinSupply().doubleValue() > 0) {
            model.put("premineCoinLockedPercent", FormatUtils.formatPercent(
                    premineCoinLocked
                            .divide(info.getCoinSupply(), 6, RoundingMode.HALF_DOWN)
                            .multiply(BigDecimal.valueOf(100))
            ));
        } else {
            model.put("premineCoinLockedPercent", 0.0);
        }

        model.put("coinLocked", FormatUtils.formatInteger(coinLocked));
        if (info.getCoinSupply().doubleValue() > 0) {
            model.put("coinLockedPercent", FormatUtils.formatPercent(
                    coinLocked
                            .divide(info.getCoinSupply(), 6, RoundingMode.HALF_DOWN)
                            .multiply(BigDecimal.valueOf(100))
            ));
        } else {
            model.put("coinLockedPercent", 0.0);
        }

        model.put("lastPages", pageService.findLastPages());
        return "index";
    }
}