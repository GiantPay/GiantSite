package network.giantpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GiantApplication {

    public static void main(String[] args) {
        SpringApplication.run(GiantApplication.class, args);
    }
}
