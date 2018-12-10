package network.giantpay.web;

import lombok.AllArgsConstructor;
import network.giantpay.service.ActiveProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@AllArgsConstructor
public class ProductsController {

    private final ActiveProductService activeProductService;

    @GetMapping("/products")
    public String products(@RequestParam(value = "page", required = false) final String page, final Map<String, Object> model) {
        this.activeProductService.accept(page, model);
        return "products";
    }
}
