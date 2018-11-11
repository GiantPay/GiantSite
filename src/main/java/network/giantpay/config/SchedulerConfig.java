package network.giantpay.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

/**
 * We don't need Scheduling in dev environment
 */
@Configuration
@EnableScheduling
@Profile("!dev")
@Slf4j
public class SchedulerConfig {

    @PostConstruct
    private void init(){
        log.info("Scheduler is enabled");
    }

}
