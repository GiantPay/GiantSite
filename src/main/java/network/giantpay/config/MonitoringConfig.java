package network.giantpay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MonitoringConfig {

    /**
     * @return RestTemplate with configured timeout
     * Used in monitoring cron to check health of sites
     */
    @Bean
    public RestTemplate restTemplate() {
        final SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5_000);
        factory.setReadTimeout(5_000);

        return new RestTemplate(factory);
    }
}
