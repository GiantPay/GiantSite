package network.giantpay.web;

import network.giantpay.model.Page;
import network.giantpay.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping("/pages/{url}")
    public String view(
            @PathVariable("url") String url,
            Map<String, Object> model) {
        Page page = pageService.getPage("/" + url);
        if (page == null) {
            return "redirect:/pages";
        }
        model.put("page", page);
        model.put("categories", pageService.findCategories());

        return "pages/view";
    }

    @GetMapping("/pages")
    public String list(
            @RequestParam(value = "c", required = false) String category,
            Map<String, Object> model) {
        model.put("pages", pageService.findAll(category));
        model.put("categories", pageService.findCategories());

        return "pages/list";
    }
}
