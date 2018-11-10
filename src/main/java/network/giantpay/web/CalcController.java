package network.giantpay.web;

import lombok.AllArgsConstructor;
import network.giantpay.dto.RateDto;
import network.giantpay.service.MonitoringService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.RoundingMode;
import java.util.Map;

@Controller
@AllArgsConstructor
public class CalcController {

    private final MonitoringService monitoringService;

    @GetMapping("/calc")
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    public String oldUrl() {
        return "redirect:/";
    }

    @GetMapping("/proof")
    public String proofOfSake(final Map<String, Object> model) {
        final RateDto rates = this.monitoringService.getRates();

        model.put("btcRate", rates.getBtc().setScale(8, RoundingMode.HALF_DOWN));
        model.put("usdRate", rates.getUsd().setScale(2, RoundingMode.HALF_DOWN));

        return "proof";
    }
}
