package network.giantpay.web;

import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@Controller
public class ProductsController {

    private static final Set<String> PAGES = ImmutableSet.of("exchange", "coldstake", "bet");

    @GetMapping("/products")
    public String products(@RequestParam(value = "page", required = false) final String page, final Map<String, Object> model) {
        setActiveProduct(page, model);
        return "products";
    }

    private static void setActiveProduct(final String page, final Map<String, Object> model) {
        model.put(PAGES.contains(page) ? page : "exchange", true);
    }
}
