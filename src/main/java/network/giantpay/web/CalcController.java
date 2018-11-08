package network.giantpay.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class CalcController {

    @GetMapping("/calc")
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    public String oldUrl() {
        return "redirect:/";
    }

    @GetMapping("/proof")
    public String proofOfSake() {
        return "proof";
    }
}
