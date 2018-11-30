package network.giantpay.listeners.sitemap;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;


@Service
public final class SkippedUrl implements Predicate<String> {

    private static final List<Pattern> SKIP_URL_PATTERNS = Arrays.asList(
            Pattern.compile("/error"),
            Pattern.compile("/login"),
            Pattern.compile("/roadmap/.*"),
            Pattern.compile("/pages/edit.*"),
            Pattern.compile("/pages/\\{url}"),
            Pattern.compile("/sitemap.xml")
    );

    /**
     * @param url to check
     * @return true if url should not be put to sitemap
     */
    @Override
    public boolean test(final String url) {
        return SKIP_URL_PATTERNS.stream().anyMatch(pattern -> pattern.matcher(url).find());
    }
}
