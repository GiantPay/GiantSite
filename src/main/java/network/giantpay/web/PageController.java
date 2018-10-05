package network.giantpay.web;

import network.giantpay.model.Page;
import network.giantpay.service.PageService;
import network.giantpay.utils.ImageUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class PageController {

    private final static Logger logger = LoggerFactory.getLogger(PageController.class);

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

        try {
            Parser parser = Parser.builder().build();
            Node document = parser.parse(page.getText());
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            page.setHtml(renderer.render(document));
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            page.setHtml(page.getText());
        }

        return "pages/view";
    }

    @GetMapping("/pages/edit/{url}")
    public String edit(@PathVariable("url") String url,
                       Map<String, Object> model) {
        Page page = pageService.getPage("/" + url);
        if (page == null) {
            page = new Page();
        }
        model.put("page", page);
        model.put("categories", pageService.findCategories());
        model.put("images", ImageUtils.getImages(page.getImages()));

        return "pages/edit";
    }


    @PostMapping(value = "/pages/edit")
    public String edit(@RequestBody MultiValueMap params,
                       Map<String, Object> model) {
        Page page = pageService.edit(params);
        return "redirect:/pages" + page.getUrl();
    }

    @GetMapping("/pages")
    public String list(
            @RequestParam(value = "c", required = false) String category,
            Map<String, Object> model) {
        model.put("pages", pageService.findAll(category));
        model.put("categories", pageService.findCategories());
        model.put("currentCategory", category);

        return "pages/list";
    }

}
