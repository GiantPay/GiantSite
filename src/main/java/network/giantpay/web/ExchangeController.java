package network.giantpay.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExchangeController {

    @GetMapping("/exchange")
    public String exchange() {
        return "exchange";
    }
}
