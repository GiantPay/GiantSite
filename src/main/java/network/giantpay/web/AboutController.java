package network.giantpay.web;

import network.giantpay.dto.CoinInfoDto;
import network.giantpay.dto.InfoDto;
import network.giantpay.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Controller
public class AboutController {

    @Autowired
    private MonitoringService monitoringService;

    @GetMapping("/about")
    public String about(Map<String, Object> model) {

        InfoDto info = monitoringService.getInfo();
        model.put("currentHeight", info.getHeight());
        model.put("currentDifficulty", info.getNetworkDifficulty().setScale(1, RoundingMode.HALF_DOWN));
        model.put("coinSupply", info.getCoinSupply().longValue());
        model.put("masternodes", info.getMasternodes());

        return "about";
    }
}
