package network.giantpay.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.function.Consumer;

@Controller
@AllArgsConstructor
public class CalcController {

    private final Consumer<Map<String, Object>> rateService;

    @GetMapping("/calc")
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    public String oldUrl() {
        return "redirect:/";
    }

    @GetMapping("/proof")
    public String proofOfSake(final Map<String, Object> model) {
        this.rateService.accept(model);

        return "proof";
    }
}
