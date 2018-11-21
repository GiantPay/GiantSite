package network.giantpay.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WhitepaperController {

    @GetMapping("/whitepaper")
    public String giantWhitepaper() {
        return "whitepapers/giant";
    }

    @GetMapping("/giant-contracts")
    public String giantContractsWhitepaper() {
        return "whitepapers/giant-contracts";
    }

    @GetMapping("/giant-exchange")
    public String giantExchangeWhitepaper() {
        return "whitepapers/giant-exchange";
    }
}
