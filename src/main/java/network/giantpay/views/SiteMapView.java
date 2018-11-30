package network.giantpay.views;

import network.giantpay.listeners.CheckedRunnable;
import org.apache.commons.io.FileUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.Writer;
import java.util.Map;

@Component
public final class SiteMapView extends AbstractView {

    private final CheckedRunnable sitemap;

    private final String filePath;

    private static final String SITE_MAP = "sitemap.xml";

    public SiteMapView(final CheckedRunnable sitemap, final Environment environment) {
        this.sitemap = sitemap;
        this.filePath = environment.getProperty("sitemap.path");
    }

    @Override
    protected void renderMergedOutputModel(final Map<String, Object> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_XML_VALUE);
        this.sitemap.run();
        try (Writer writer = response.getWriter()) {
            writer.append(FileUtils.readFileToString(new File(this.filePath + SITE_MAP), "UTF-8"));
        }
    }
}
