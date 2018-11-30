package network.giantpay.listeners.sitemap;

import lombok.RequiredArgsConstructor;
import network.giantpay.repository.PagesUrlsRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static network.giantpay.GiantApplication.BASE_URL;

/**
 * This class Listen for ContextRefreshedEvent to save all available endpoints
 */
@Component
@RequiredArgsConstructor
public final class AvailableUrls implements ApplicationListener<ContextRefreshedEvent>, Supplier<List<String>> {

    /**
     * Available urls
     */
    private final List<String> urls = new ArrayList<>(10);

    /**
     * Predicate to check if url should be skipped
     */
    private final Predicate<String> skipUrl;

    /**
     * Repository to get pages urls
     */
    private final PagesUrlsRepository pagesUrlsRepository;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        final Map<RequestMappingInfo, HandlerMethod> methods = event.getApplicationContext()
                .getBean(RequestMappingHandlerMapping.class)
                .getHandlerMethods();

        final List<PatternsRequestCondition> urlPatterns = methods.entrySet()
                .stream()
                .map(key -> key.getKey().getPatternsCondition())
                .collect(toList());
        this.saveUrls(urlPatterns);
    }

    /**
     * @return list of available urls
     */
    @Override
    public List<String> get() {
        return this.urls;
    }

    /**
     * Save endpoints and news to urls
     *
     * @param urlPatterns of available endpoints
     */
    private void saveUrls(final List<PatternsRequestCondition> urlPatterns) {
        this.urls.addAll(urlPatterns.stream()
                .map(PatternsRequestCondition::getPatterns)
                .flatMap(Set::stream)
                .filter(url -> !this.skipUrl.test(url))
                .map(url -> BASE_URL + url)
                .collect(toList()));
        this.urls.addAll(this.pagesUrlsRepository.get());
    }
}
