package network.giantpay.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class WhitepaperController {

    @GetMapping("/whitepaper")
    public String giantWhitepaper(Map<String, Object> model) {
        model.put("ready", true);
        model.put("title", "Giant WP");
        model.put("whitepaperUrl", "/whitepaper.pdf");

        return "whitepapers/index";
    }

    @GetMapping("/whitepaper/contracts")
    public String giantContractsWhitepaper(Map<String, Object> model) {
        model.put("ready", true);
        model.put("title", "GiantContracts WP");
        model.put("whitepaperUrl", "/GiantContractsWhitepaper.pdf");

        return "whitepapers/index";
    }

    @GetMapping("/whitepaper/exchange")
    public String giantExchangeWhitepaper(Map<String, Object> model) {
        model.put("ready", false);
        model.put("title", "Giant.Exchange WP");
        model.put("text", "Giant.Exchange (Coming soon)");

        return "whitepapers/index";
    }
}
