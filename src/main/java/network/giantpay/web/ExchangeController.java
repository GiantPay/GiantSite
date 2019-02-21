package network.giantpay.web;

import lombok.AllArgsConstructor;
import network.giantpay.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@AllArgsConstructor
public class ExchangeController {

    private final MonitoringService monitoringService;

    @GetMapping("/exchanges")
    public String exchange(final Map<String, Object> model) {
        model.putAll(this.monitoringService.getMarkets());
        return "exchange";
    }
}
