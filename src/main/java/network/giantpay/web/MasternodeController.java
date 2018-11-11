package network.giantpay.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
import java.util.function.Consumer;

@Controller
@AllArgsConstructor
public class MasternodeController {

    private final Consumer<Map<String, Object>> rateService;

    @GetMapping("/masternodes")
    public String index(final Map<String, Object> model) {
        this.rateService.accept(model);

        model.put("masternodes", 12);

        return "masternodes";
    }
}
