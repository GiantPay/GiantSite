package network.giantpay.web;

import network.giantpay.api.graviex.GraviexApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class ExchangeController {

    @Autowired
    private GraviexApi graviexApi;

    @GetMapping("/exchange")
    public String exchange(Map<String, Object> model) {
        model.put("graviex", graviexApi.getMarketInfo());

        return "exchange";
    }
}
