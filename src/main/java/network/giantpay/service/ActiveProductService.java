package network.giantpay.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * This class set active product block
 * according to given page
 */
@Service
public final class ActiveProductService implements BiConsumer<String, Map<String, Object>> {

    @Override
    public void accept(final String page, final Map<String, Object> model) {
        if (page == null) {
            model.put("exchange", "active");//default
        } else if (page.equals("bet")) {
            model.put("bet", "active");
        } else if (page.equals("coldstake")) {
            model.put("coldstake", "active");
        }
    }
}
