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

    @GetMapping("/giant-contracts")
    public String giantContractsWhitepaper() {
        return "whitepapers/giant-contracts";
    }

    @GetMapping("/giant-exchange")
    public String giantExchangeWhitepaper() {
        return "whitepapers/giant-exchange";
    }


    @GetMapping("/whitepaper/contracts")
    public String oldContactsUrl(final HttpServletRequest request) {
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.MOVED_PERMANENTLY);

        return "redirect:/giant-contracts";
    }

    @GetMapping("/whitepaper/exchange")
    public String oldExchangeUrl(final HttpServletRequest request) {
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.MOVED_PERMANENTLY);

        return "redirect:/giant-exchange";
    }
}
