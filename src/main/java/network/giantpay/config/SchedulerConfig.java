package network.giantpay.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * we don't need Scheduling in dev environment
 */
@Configuration
@EnableScheduling
@Profile("!dev")
public  class SchedulerConfig {

}
