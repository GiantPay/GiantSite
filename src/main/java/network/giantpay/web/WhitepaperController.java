package network.giantpay.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class WhitepaperController {

    @GetMapping("/whitepaper")
    public String giantWhitepaper(final Map<String, Object> model) {
        model.put("giant",true);
        return "whitepapers/giant";
    }

    @GetMapping("/whitepaper/contracts")
    public String giantContractsWhitepaper(final Map<String, Object> model) {
        model.put("contracts",true);
        return "whitepapers/giant-contracts";
    }

    @GetMapping("/whitepaper/exchange")
    public String giantExchangeWhitepaper(final Map<String, Object> model) {
        model.put("exchange",true);
        return "whitepapers/giant-exchange";
    }


}
