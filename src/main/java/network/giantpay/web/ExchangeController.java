package network.giantpay.web;

import network.giantpay.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class ExchangeController {

    @Autowired
    private MonitoringService monitoringService;

    @GetMapping("/exchange")
    public String exchange(Map<String, Object> model) {
        model.putAll(monitoringService.getMarkets());
        return "exchange";
    }
}
