package network.giantpay.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WhitepaperController {

    @GetMapping("/whitepaper")
    public String giantWhitepaper() {
        return "whitepapers/giant";
    }

    @GetMapping("/whitepaper/contracts")
    public String giantContractsWhitepaper() {
        return "whitepapers/giant-contracts";
    }

    @GetMapping("/whitepaper/exchange")
    public String giantExchangeWhitepaper() {
        return "whitepapers/giant-exchange";
    }


}
