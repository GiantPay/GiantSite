package network.giantpay.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MasternodeController {

    @GetMapping("/masternodes")
    public String masternodes() {
        return "masternodes/index";
    }

    @GetMapping("/masternodes/guide")
    public String guide() {
        return "masternodes/guide";
    }
}
