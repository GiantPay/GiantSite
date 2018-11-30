package network.giantpay.web;

import lombok.AllArgsConstructor;
import network.giantpay.views.SiteMapView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@Controller
@AllArgsConstructor
public final class SiteMapController {

    private final SiteMapView siteMapView;

    @GetMapping(path = "/sitemap.xml", produces = APPLICATION_XML_VALUE)
    public SiteMapView create() {
        return siteMapView;
    }
}
