package network.giantpay.web;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ProductsController {

    @GetMapping("/products")
    public String products(@RequestParam(value = "page", required = false) final String page, final Map<String, Object> model) {
        setActiveProduct(page, model);
        return "products";
    }

    private static void setActiveProduct(final String page, final Map<String, Object> model) {
        if (!StringUtils.isEmpty(page)) {
            if (page.equals("bet")) {
                model.put("bet", true);
            } else if (page.equals("coldstake")) {
                model.put("coldstake", true);
            }
        } else {
            model.put("exchange", true);
        }
    }
}
