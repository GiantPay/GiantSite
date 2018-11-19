package network.giantpay.web;

import lombok.AllArgsConstructor;
import network.giantpay.service.MonitoringService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
import java.util.function.Consumer;

@Controller
@AllArgsConstructor
public class MasternodeController {

    private final Consumer<Map<String, Object>> rateService;

    private final MonitoringService monitoringService;

    @GetMapping("/masternodes")
    public String index(final Map<String, Object> model) {
        this.rateService.accept(model);

        model.put("masternodes", this.monitoringService.getMasternodeInfo().getMasternodeCount());

        return "masternodes";
    }
}
