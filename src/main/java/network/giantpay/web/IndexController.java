package network.giantpay.web;

import network.giantpay.dto.InfoDto;
import network.giantpay.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private MonitoringService monitoringService;

    @GetMapping({"/"})
    public String index(Map<String, Object> model) {
        model.put("btc", monitoringService.getCoinInfo("BTC"));
        model.put("eth", monitoringService.getCoinInfo("ETH"));
        model.put("xrp", monitoringService.getCoinInfo("XRP"));
        model.put("ltc", monitoringService.getCoinInfo("LTC"));
        model.put("dash", monitoringService.getCoinInfo("DASH"));
        model.put("bch", monitoringService.getCoinInfo("BCH"));

        InfoDto info = monitoringService.getInfo();
        model.put("currentHeight", info.getHeight());
        model.put("currentHashrate", info.getNetworkHashrate());
        model.put("currentDifficulty", info.getNetworkDifficulty());
        model.put("coinSupply", info.getCoinSupply());
        return "index";
    }
}
