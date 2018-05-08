package network.giantpay.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MiningController {

    @GetMapping("/mining")
    public String mining() {
        return "mining";
    }
}
