package network.giantpay.listeners.sitemap;

import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import network.giantpay.listeners.CheckedRunnable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import static network.giantpay.GiantApplication.BASE_URL;

/**
 * This class save available urls from {@link AvailableUrls}
 * to sitemap.xml
 */
@Component
public final class SiteMap implements CheckedRunnable {

    /**
     * Path to store in sitemap
     */
    private final String filaPath;

    /**
     * Urls to store in sitemap
     */
    private final Supplier<List<String>> availableUrls;

    public SiteMap(final Environment environment, final Supplier<List<String>> availableUrls) {
        this.filaPath = environment.getProperty("sitemap.path");
        this.availableUrls = availableUrls;
    }


    /**
     * This method store available urls to sitemap
     */
    @Override
    public void run() throws Exception {
        try {
            final WebSitemapGenerator sitemapGenerator = WebSitemapGenerator.builder(BASE_URL, new File((this.filaPath))).build();
            final Date now = new Date();
            final List<String> urls = this.availableUrls.get();
            if (!urls.isEmpty()) {
                for (final String url : urls) {
                    sitemapGenerator.addUrl(new WebSitemapUrl(new WebSitemapUrl.Options(url).lastMod(now).priority(1.0)));
                }
                sitemapGenerator.write();
            }
        } catch (final IOException exc) {
            throw new UncheckedIOException(exc);
        }
    }
}
