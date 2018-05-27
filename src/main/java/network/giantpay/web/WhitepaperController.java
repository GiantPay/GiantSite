package network.giantpay.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WhitepaperController {

    @GetMapping("/whitepaper")
    public String wallet() {
        return "whitepaper";
    }
}
